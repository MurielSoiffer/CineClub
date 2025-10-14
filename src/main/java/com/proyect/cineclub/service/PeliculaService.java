package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.entity.Pelicula;
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
        Optional<Pelicula> peliculaExistente = peliculaRepository.findById(id);
        if(peliculaExistente.isEmpty()) {
            // .orElseThrow(() -> new RuntimeException("Pelicula no encontrada"));
        }

        peliculaExistente.get().setTitulo(request.getTitulo());
        peliculaExistente.get().setSinopsis(request.getSinopsis());
        peliculaExistente.get().setDuracion(request.getDuracion());

        return peliculaRepository.save(peliculaExistente.get());
    }

    public Page<Pelicula> getAll(Pageable pageable){return peliculaRepository.findAll(pageable);}

    public Optional<Pelicula> getById(Long id){return peliculaRepository.findById(id);}

    public void deleteById(Long id){peliculaRepository.deleteById(id);}

}
