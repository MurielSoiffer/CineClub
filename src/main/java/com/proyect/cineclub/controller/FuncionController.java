package com.proyect.cineclub.controller;

import com.proyect.cineclub.dto.*;
import com.proyect.cineclub.entity.*;
import com.proyect.cineclub.repository.FuncionRepository;
import com.proyect.cineclub.repository.TicketRepository;
import com.proyect.cineclub.security.EstadoTicket;
import com.proyect.cineclub.service.FuncionService;
import com.proyect.cineclub.service.TicketService;
import com.proyect.cineclub.specification.FuncionSpecificationBuilder;
import com.proyect.cineclub.specification.SalaSpecificationBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/funciones")
public class FuncionController {

    @Autowired
    FuncionService funcionService;

    @Autowired
    TicketService ticketService;

    private final FuncionRepository funcionRepository;
    private final TicketRepository ticketRepository;

    public FuncionController(FuncionRepository funcionRepository,TicketRepository ticketRepository) {
        this.funcionRepository = funcionRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public List<FuncionDto> get(Pageable pageable){
        Page<Funcion> funciones = this.funcionService.getAll(pageable);
        List<FuncionDto> dtos = funciones.stream()
                .map(FuncionDto::fromFuncion)
                .collect(Collectors.toList());
        return dtos;
    }
    @PostMapping
    public FuncionDto save(@RequestBody @Valid Funcion f){
        Funcion funcion = this.funcionService.save(f);
        FuncionDto funcionDto = FuncionDto.fromFuncion(funcion);
        return funcionDto;
    }
    @PostMapping("/buscar")
    public ResponseEntity<Page<FuncionDto>> buscarFuncion(
            @RequestBody FuncionFiltroDto filtro,
            @PageableDefault(size = 10, sort = "id", direction =
                    Sort.Direction.ASC) Pageable pageable
    ) {
        Specification<Funcion> spec =
                FuncionSpecificationBuilder.construirFiltros(filtro);
        Page<Funcion> resultado = funcionRepository.findAll(spec, pageable);
        Page<FuncionDto> resultadoDto = resultado.map(FuncionDto::fromFuncion);
        return ResponseEntity.ok(resultadoDto);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<FuncionDto> getById(@PathVariable("id") Long id){
        Optional<Funcion> funcionOptional = this.funcionService.getById(id);
        if (funcionOptional.isPresent()){
            Funcion funcion = funcionOptional.get();
            FuncionDto funcionDto = FuncionDto.fromFuncion(funcion);
            return new ResponseEntity<>(funcionDto, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{id}/butacas")
    public ResponseEntity<List<String>> getButacasSala(@PathVariable("id") Long id){
        Optional<Funcion> funcionOptional = this.funcionService.getById(id);
        if (funcionOptional.isPresent()) {
            Funcion funcion = funcionOptional.get();
            List<Butaca> sala = funcion.getSala().getButacas();

            List<EstadoTicket> estadosNoLiberados = Arrays.asList(
                    EstadoTicket.HOLD,
                    EstadoTicket.CONFIRMADO
            );

            List<String> butacasStatus = sala.stream()
                    .map(butaca -> {
                        String etiqueta = butaca.getEtiqueta();
                        Optional<Ticket> ticketOptional = ticketRepository.findFirstByFuncionAndButacaAndEstadoIn(
                                funcion,
                                butaca,
                                estadosNoLiberados
                        );

                        String estadoButaca;
                        if (ticketOptional.isPresent()) {
                            EstadoTicket estadoTicket = ticketOptional.get().getEstado();

                            if (estadoTicket == EstadoTicket.CONFIRMADO) {
                                estadoButaca = " :OCUPADO";
                            } else if (estadoTicket == EstadoTicket.HOLD) {
                                estadoButaca = " :RESERVADO";
                            } else {
                                estadoButaca = " :LIBRE";
                            }
                        } else {
                            estadoButaca = " :LIBRE";
                        }

                        return etiqueta + estadoButaca;
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(butacasStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //------------------------------------No funciona actualmente, hay que arreglarlo despues-----------------------------------------
    @PostMapping("/{id}/holds")
    public ResponseEntity<Ticket> createHold(
            @PathVariable Long id,
            @Valid @RequestBody HoldRequest request) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long userId = null;
        if (principal instanceof UsuarioPrincipal) {
            UsuarioPrincipal userPrincipal = (UsuarioPrincipal) principal;
            userId = userPrincipal.getUser().getId();
        } else {
            throw new IllegalStateException("Error al obtener el ID del usuario autenticado.");
        }

        Ticket createdTicket = ticketService.createHold(
        id,
        request.getButacasIds(),
        request.getTtlSeconds(),
        userId
        );
        return new ResponseEntity<>(
                createdTicket,
                HttpStatus.CREATED
        );
    }
    //--------------------------------------------------------------------------------------------------------------------------------

    @PutMapping(path = "/{id}")
    public ResponseEntity<FuncionDto> updateById(@RequestBody Funcion request, @PathVariable("id") long id){
        return ResponseEntity.ok(FuncionDto.fromFuncion(this.funcionService.updateById(request, id)));
    }

    @DeleteMapping(path = "/{id}")
    public void deleteFuncionById(@PathVariable("id") Long id){
        this.funcionService.deleteById(id);
    }
}
