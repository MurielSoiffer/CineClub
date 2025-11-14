package com.proyect.cineclub.repository;

import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ButacaRepository extends JpaRepository<Butaca, Long> {
    List<Butaca> findAllByEtiquetaInAndSala(List<String> etiquetas, Sala sala);
    List<Butaca> findAllByEtiquetaIn(List<String> etiquetas);
}
