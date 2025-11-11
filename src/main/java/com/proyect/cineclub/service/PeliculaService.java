package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.entity.Pelicula;
import com.proyect.cineclub.exception.RecursoNoEncontradoException;
import com.proyect.cineclub.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PeliculaService {
    @Autowired
    PeliculaRepository peliculaRepository;

    public Pelicula save(Pelicula pelicula){return peliculaRepository.save(pelicula);}

    @Transactional
    public Pelicula updateById(Pelicula request, Long id) {
        Pelicula peliculaExistente = peliculaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Pelicula",id));

        peliculaExistente.setTitulo(request.getTitulo());
        peliculaExistente.setSinopsis(request.getSinopsis());
        peliculaExistente.setDuracion(request.getDuracion());

        return peliculaRepository.save(peliculaExistente);
    }

    public Page<Pelicula> getAll(Pageable pageable){return peliculaRepository.findAll(pageable);}

    public Optional<Pelicula> getById(Long id){return peliculaRepository.findById(id);}

    public void deleteById(Long id){peliculaRepository.deleteById(id);}

}
