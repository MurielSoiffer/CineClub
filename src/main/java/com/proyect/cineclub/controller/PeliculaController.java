package com.proyect.cineclub.controller;

import com.proyect.cineclub.dto.PeliculaFiltroDto;
import com.proyect.cineclub.entity.Pelicula;
import com.proyect.cineclub.repository.PeliculaRepository;
import com.proyect.cineclub.service.PeliculaService;
import com.proyect.cineclub.specification.PeliculaSpecificationBuilder;
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

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {

    @Autowired
    PeliculaService peliculaService;

    private final PeliculaRepository peliculaRepository;

    public PeliculaController(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Pelicula>> get(@PageableDefault(size = 5, sort = "id") Pageable pageable){
        Page<Pelicula> pagePeliculas = peliculaService.getAll(pageable);
        List<Pelicula> peliculas = pagePeliculas.getContent();
        return ResponseEntity.ok(peliculas);
    }
    @PostMapping
    public Pelicula save(@RequestBody @Valid Pelicula pelicula){
        Pelicula savePelicula = this.peliculaService.save(pelicula);
        return pelicula;
    }

    @PostMapping("/buscar")
    public ResponseEntity<Page<Pelicula>> buscarPeliculas(
            @RequestBody PeliculaFiltroDto filtro,
            @PageableDefault(size = 10, sort = "titulo", direction =
                    Sort.Direction.ASC) Pageable pageable
    ) {
        Specification<Pelicula> spec =
                PeliculaSpecificationBuilder.construirFiltros(filtro);
        Page<Pelicula> resultado = peliculaRepository.findAll(spec, pageable);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Pelicula> getById(@PathVariable("id") Long id){
        Optional<Pelicula> peliculaOptional = this.peliculaService.getById(id);
        if (peliculaOptional.isPresent()){
            Pelicula pelicula = peliculaOptional.get();
            return new ResponseEntity<>(pelicula, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Pelicula> updateById(@RequestBody Pelicula request, @PathVariable("id") long id){
        return ResponseEntity.ok(this.peliculaService.updateById(request, id));
    }

    @DeleteMapping(path = "/{id}")
    public void deletePeliculaById(@PathVariable("id") Long id){
        this.peliculaService.deleteById(id);
    }
}
