package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.entity.Pelicula;
import com.proyect.cineclub.entity.Sala;
import com.proyect.cineclub.exception.HorarioSuperpuestoException;
import com.proyect.cineclub.exception.RecursoNoEncontradoException;
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
import java.util.stream.Collectors;

@Service
public class FuncionService {
    @Autowired FuncionRepository funcionRepository;
    @Autowired private PeliculaService peliculaService;
    @Autowired private SalaService salaService;

    public Funcion save(Funcion funcion){
        Pelicula peliculaCompleta = peliculaService.getById(funcion.getPelicula().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Pelicula",funcion.getPelicula().getId()));

        Sala salaCompleta = salaService.getById(funcion.getSala().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Sala", funcion.getSala().getId()));

        funcion.setPelicula(peliculaCompleta);
        funcion.setSala(salaCompleta);

        List<Funcion> superpuestas = funcionRepository.findSuperpuesta( funcion.getSala(), funcion.getInicio(), funcion.getFinalizacion());
        if (!superpuestas.isEmpty()) {
            throw new HorarioSuperpuestoException(funcion.getSala().getId());
        }
        return funcionRepository.save(funcion);
    }

    @Transactional
    public Funcion updateById(Funcion request, Long id) {
        Funcion funcionExistente = funcionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Función", id));

        funcionExistente.setActiva(request.getActiva());
        funcionExistente.setInicio(request.getInicio());
        funcionExistente.setFinalizacion(request.getFinalizacion());
        List<Funcion> superpuestas = funcionRepository.findSuperpuesta( funcionExistente.getSala(), request.getInicio(), request.getFinalizacion());
        List<Funcion> superpuestasCorrecto = superpuestas.stream()
                .filter(f -> !f.getId().equals(id)).toList();
        if (!superpuestasCorrecto.isEmpty()) {
            throw new HorarioSuperpuestoException(funcionExistente.getSala().getId());
        }

        return funcionRepository.save(funcionExistente);
    }

    public Page<Funcion> getAll(Pageable pageable){return funcionRepository.findAll(pageable);}

    public Optional<Funcion> getById(Long id){return funcionRepository.findById(id);}

    public void deleteById(Long id){funcionRepository.deleteById(id);}
}
