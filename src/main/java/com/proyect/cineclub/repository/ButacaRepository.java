package com.proyect.cineclub.repository;

import com.proyect.cineclub.entity.Butaca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ButacaRepository extends JpaRepository<Butaca, Long> {
    List<Butaca> findAllByEtiquetaIn(List<String> etiquetas);
}
