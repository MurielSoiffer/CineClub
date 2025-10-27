package com.proyect.cineclub.repository;

import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FuncionRepository extends JpaRepository<Funcion,Long>, JpaSpecificationExecutor<Funcion> {
    @Query("SELECT f FROM Funcion f WHERE f.sala = :sala " +
            "AND f.id <> :idExcluir " +
            "AND (" +"    (f.inicio < :finalizacion AND f.finalizacion > :inicio)" +")")
    List<Funcion> findSuperpuesta(
            @Param("sala") Sala sala,
            @Param("inicio") LocalDateTime inicio,
            @Param("finalizacion") LocalDateTime finalizacion,
            @Param("idExcluir") Long idExcluir);

    // Método alternativo para cuando se guarda una función nueva (idExcluir es 0 o -1)
    default List<Funcion> findSuperpuesta(Sala sala, LocalDateTime inicio, LocalDateTime finalizacion) {
        return findSuperpuesta(sala, inicio, finalizacion, 0L);
    }
}
