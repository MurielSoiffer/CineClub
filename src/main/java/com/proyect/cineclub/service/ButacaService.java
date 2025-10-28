package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.repository.ButacaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ButacaService {
    @Autowired
    ButacaRepository butacaRepository;

    public Butaca save(Butaca butaca){
        return butacaRepository.save(butaca);
    }

    public List<Butaca> saveAll(List<Butaca> butacas){
        return butacaRepository.saveAll(butacas);
    }

    @Transactional
    public Butaca updateById(Butaca request, Long id) {
        Optional<Butaca> butacaExistente = butacaRepository.findById(id);
        if(butacaExistente.isEmpty()) {
            // .orElseThrow(() -> new RuntimeException("Pelicula no encontrada"));
        }

        butacaExistente.get().setFila(request.getFila());
        butacaExistente.get().setNumero(request.getNumero());

        return butacaRepository.save(butacaExistente.get());
    }

    public List<Butaca> getAll(){return butacaRepository.findAll();}

    public Optional<Butaca> getById(Long id){return butacaRepository.findById(id);}
    public List<Butaca> getAllById(List<Long> ids){return butacaRepository.findAllById(ids);}

    public void deleteById(Long id){butacaRepository.deleteById(id);}
}
