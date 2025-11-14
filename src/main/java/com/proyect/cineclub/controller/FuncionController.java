package com.proyect.cineclub.controller;

import com.proyect.cineclub.dto.*;
import com.proyect.cineclub.entity.*;
import com.proyect.cineclub.repository.FuncionRepository;
import com.proyect.cineclub.repository.TicketRepository;
import com.proyect.cineclub.security.EstadoTicket;
import com.proyect.cineclub.service.FuncionService;
import com.proyect.cineclub.service.TicketService;
import com.proyect.cineclub.service.UsuarioService;
import com.proyect.cineclub.specification.FuncionSpecificationBuilder;
import com.proyect.cineclub.configuration.FuncionApi;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class FuncionController implements FuncionApi {

    @Autowired
    FuncionService funcionService;

    @Autowired
    TicketService ticketService;

    @Autowired
    UsuarioService usuarioService;

    private final FuncionRepository funcionRepository;
    private final TicketRepository ticketRepository;

    public FuncionController(FuncionRepository funcionRepository,TicketRepository ticketRepository) {
        this.funcionRepository = funcionRepository;
        this.ticketRepository = ticketRepository;
    }
    @Override
    public FuncionDto save(@RequestBody @Valid Funcion f){
        Funcion funcion = this.funcionService.save(f);
        FuncionDto funcionDto = FuncionDto.fromFuncion(funcion);
        return funcionDto;
    }
    @Override
    public ResponseEntity<Page<FuncionDto>> buscarFuncion(
            @RequestParam(required = false) String pelicula,
            @RequestParam(required = false) String sala,
            @RequestParam(required = false) LocalDateTime fechaYhoraMinima,
            @RequestParam(required = false) Long precioMinimo,
            @RequestParam(required = false) Long precioMaximo,
            @PageableDefault(size = 10, sort = "id", direction =
                    Sort.Direction.ASC) Pageable pageable
    ) {
        FuncionFiltroDto filtro = new FuncionFiltroDto();
        filtro.setPelicula(pelicula);
        filtro.setSala(sala);
        filtro.setFechaYhoraMinima(fechaYhoraMinima);
        filtro.setPrecioMinimo(precioMinimo);
        filtro.setPrecioMaximo(precioMaximo);

        Specification<Funcion> spec =
                FuncionSpecificationBuilder.construirFiltros(filtro);
        Page<Funcion> resultado = funcionRepository.findAll(spec, pageable);
        Page<FuncionDto> resultadoDto = resultado.map(FuncionDto::fromFuncion);
        return ResponseEntity.ok(resultadoDto);
    }

    @Override
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

    @Override
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
                                estadoButaca = " :NO LIBRE";
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

    @Override
    public ResponseEntity<List<TicketDto>> createHold(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody HoldRequest request) {

        String username = authentication.getName();

        Usuario usuario = usuarioService.findByUsername(username);

        Long userId = null;
        if (usuario != null) {
            userId = usuario.getId();
        } else {
            throw new IllegalStateException("Error al obtener el ID del usuario autenticado.");
        }

        List<Ticket> createdTickets = ticketService.createHold(
            id,
            request.getButacas(),
            userId
            );
        List<TicketDto> ticketDtos = createdTickets.stream().map(TicketDto::fromTicket).toList();
        return new ResponseEntity<>(
                ticketDtos,
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<FuncionDto> updateById(@RequestBody Funcion request, @PathVariable("id") long id){
        return ResponseEntity.ok(FuncionDto.fromFuncion(this.funcionService.updateById(request, id)));
    }

    @Override
    public void deleteFuncionById(@PathVariable("id") Long id){
        this.funcionService.deleteById(id);
    }
}
