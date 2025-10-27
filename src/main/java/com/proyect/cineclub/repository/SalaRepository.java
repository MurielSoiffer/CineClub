package com.proyect.cineclub.repository;

import com.proyect.cineclub.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SalaRepository extends JpaRepository<Sala,Long>, JpaSpecificationExecutor<Sala> {
}
