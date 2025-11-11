package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.exception.RecursoNoEncontradoException;
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
        Butaca butacaExistente = butacaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Butaca",id));

        butacaExistente.setFila(request.getFila());
        butacaExistente.setNumero(request.getNumero());

        return butacaRepository.save(butacaExistente);
    }

    public List<Butaca> getAll(){return butacaRepository.findAll();}

    public Optional<Butaca> getById(Long id){return butacaRepository.findById(id);}
    public List<Butaca> getAllById(List<Long> ids){return butacaRepository.findAllById(ids);}

    public List<Butaca> getAllByEtiqueta(List<String> etiquetas){
        return butacaRepository.findAllByEtiquetaIn(etiquetas);
    }
    public void deleteById(Long id){butacaRepository.deleteById(id);}
}
