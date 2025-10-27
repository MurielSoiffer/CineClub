package com.proyect.cineclub.controller;

import com.proyect.cineclub.dto.FuncionDto;
import com.proyect.cineclub.dto.FuncionFiltroDto;
import com.proyect.cineclub.dto.SalaDto;
import com.proyect.cineclub.dto.SalaFiltroDto;
import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.entity.Sala;
import com.proyect.cineclub.repository.FuncionRepository;
import com.proyect.cineclub.service.FuncionService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/funciones")
public class FuncionController {

    @Autowired
    FuncionService funcionService;

    private final FuncionRepository funcionRepository;

    public FuncionController(FuncionRepository funcionRepository) {
        this.funcionRepository = funcionRepository;
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
        if (funcionOptional.isPresent()){
            List<Butaca> sala = funcionOptional.get().getSala().getButacas();
            List<String> butacas = sala.stream()
                    .map(Butaca::getEtiqueta)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(butacas, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<FuncionDto> updateById(@RequestBody Funcion request, @PathVariable("id") long id){
        return ResponseEntity.ok(FuncionDto.fromFuncion(this.funcionService.updateById(request, id)));
    }

    @DeleteMapping(path = "/{id}")
    public void deleteFuncionById(@PathVariable("id") Long id){
        this.funcionService.deleteById(id);
    }
}
