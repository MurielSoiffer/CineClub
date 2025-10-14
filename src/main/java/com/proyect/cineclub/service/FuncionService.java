package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.entity.Pelicula;
import com.proyect.cineclub.entity.Sala;
import com.proyect.cineclub.repository.FuncionRepository;
import com.proyect.cineclub.repository.PeliculaRepository;
import com.proyect.cineclub.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionService {
    @Autowired FuncionRepository funcionRepository;
    @Autowired private PeliculaRepository peliculaRepository;
    @Autowired private SalaRepository salaRepository;

    public Funcion save(Funcion funcion){
        Pelicula peliculaCompleta = peliculaRepository.findById(funcion.getPelicula().getId())
                .orElseThrow(() -> new RuntimeException("Pelicula no encontrada"));

        Sala salaCompleta = salaRepository.findById(funcion.getSala().getId())
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        funcion.setPelicula(peliculaCompleta);
        funcion.setSala(salaCompleta);

        Funcion nuevaFuncion = funcionRepository.save(funcion);
        return nuevaFuncion;
    }

    @Transactional
    public Funcion updateById(Funcion request, Long id) {
        Optional<Funcion> funcionExistente = funcionRepository.findById(id);
        if(funcionExistente.isEmpty()) {
            // .orElseThrow(() -> new RuntimeException("Pelicula no encontrada"));
        }

        funcionExistente.get().setActiva(request.getActiva());

        return funcionRepository.save(funcionExistente.get());
    }

    public Page<Funcion> getAll(Pageable pageable){return funcionRepository.findAll(pageable);}

    public Optional<Funcion> getById(Long id){return funcionRepository.findById(id);}

    public void deleteById(Long id){funcionRepository.deleteById(id);}
}
