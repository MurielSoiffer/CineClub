package com.proyect.cineclub.controller;

import com.proyect.cineclub.dto.SalaDto;
import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.entity.Sala;
import com.proyect.cineclub.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/salas")
public class SalaController {

    @Autowired
    SalaService salaService;

    @GetMapping
    public List<SalaDto> get(@PageableDefault(size = 5, sort = "id") Pageable pageable){
        Page<Sala> salas = salaService.getAll(pageable);
        List<SalaDto> salasDto = salas.stream()
                .map(SalaDto::fromSala)
                .collect(Collectors.toList());
        return salasDto;
    }
    @PostMapping
    public SalaDto save(@RequestBody @Valid Sala sala){
        Sala saveSala = this.salaService.save(sala);
        SalaDto salaDto = SalaDto.fromSala(saveSala);
        return salaDto;
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<SalaDto> getById(@PathVariable("id") Long id){
        Optional<Sala> salaOptional = this.salaService.getById(id);
        if (salaOptional.isPresent()){
            SalaDto salaDto = SalaDto.fromSala(salaOptional.get());
            return new ResponseEntity<>(salaDto, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping(path = "/{id}/butacas")
    public ResponseEntity<List<String>> getButacasSala(@PathVariable("id") Long id){
        Optional<Sala> salaOptional = this.salaService.getById(id);
        if (salaOptional.isPresent()){
            List<Butaca> sala = salaOptional.get().getButacas();
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
    public ResponseEntity<SalaDto> updateById(@RequestBody Sala request, @PathVariable("id") long id){
        return ResponseEntity.ok(SalaDto.fromSala(this.salaService.updateById(request, id)));
    }

    @DeleteMapping(path = "/{id}")
    public void deleteSalaById(@PathVariable("id") Long id){
        this.salaService.deleteById(id);
    }
}
