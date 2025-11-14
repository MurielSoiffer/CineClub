package com.proyect.cineclub.controller;

import com.proyect.cineclub.dto.SalaDto;
import com.proyect.cineclub.dto.SalaFiltroDto;
import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.entity.Sala;
import com.proyect.cineclub.configuration.SalaApi;
import com.proyect.cineclub.repository.SalaRepository;
import com.proyect.cineclub.service.SalaService;
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
public class SalaController implements SalaApi {

    @Autowired
    SalaService salaService;

    private final SalaRepository salaRepository;

    public SalaController(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    @Override
    public SalaDto save(@RequestBody @Valid Sala sala){
        Sala saveSala = this.salaService.save(sala);
        SalaDto salaDto = SalaDto.fromSala(saveSala);
        return salaDto;
    }
    @Override
    public ResponseEntity<Page<SalaDto>> buscarSalas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer capacidadMinima,
            @RequestParam(required = false) Integer capacidadMaxima,
            @PageableDefault(size = 10, sort = "id", direction =
                    Sort.Direction.ASC) Pageable pageable
    ) {
        SalaFiltroDto filtro = new SalaFiltroDto();
        filtro.setNombre(nombre);
        filtro.setCapacidadMaxima(capacidadMaxima);
        filtro.setCapacidadMinima(capacidadMinima);
        Specification<Sala> spec =
                SalaSpecificationBuilder.construirFiltros(filtro);
        Page<Sala> resultado = salaRepository.findAll(spec, pageable);
        Page<SalaDto> resultadoDto = resultado.map(SalaDto::fromSala);
        return ResponseEntity.ok(resultadoDto);
    }

    @Override
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
    @Override
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

    @Override
    public ResponseEntity<SalaDto> updateById(@RequestBody Sala request, @PathVariable("id") long id){
        return ResponseEntity.ok(SalaDto.fromSala(this.salaService.updateById(request, id)));
    }

    @Override
    public void deleteSalaById(@PathVariable("id") Long id){
        this.salaService.deleteById(id);
    }
}
