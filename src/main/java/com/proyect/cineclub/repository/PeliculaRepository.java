package com.proyect.cineclub.repository;

import com.proyect.cineclub.entity.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PeliculaRepository extends JpaRepository<Pelicula,Long>, JpaSpecificationExecutor<Pelicula> {
}
