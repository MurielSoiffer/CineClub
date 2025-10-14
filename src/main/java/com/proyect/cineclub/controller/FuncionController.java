package com.proyect.cineclub.controller;

import com.proyect.cineclub.dto.FuncionDto;
import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.service.FuncionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PutMapping(path = "/{id}")
    public ResponseEntity<FuncionDto> updateById(@RequestBody Funcion request, @PathVariable("id") long id){
        return ResponseEntity.ok(FuncionDto.fromFuncion(this.funcionService.updateById(request, id)));
    }

    @DeleteMapping(path = "/{id}")
    public void deleteFuncionById(@PathVariable("id") Long id){
        this.funcionService.deleteById(id);
    }
}
