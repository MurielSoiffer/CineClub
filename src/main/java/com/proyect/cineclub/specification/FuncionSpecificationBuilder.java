package com.proyect.cineclub.specification;

import com.proyect.cineclub.dto.FuncionFiltroDto;
import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.entity.Pelicula;
import com.proyect.cineclub.entity.Sala;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FuncionSpecificationBuilder {
    public static Specification<Funcion> construirFiltros(FuncionFiltroDto filtro){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filtro.getPelicula() != null && !filtro.getPelicula().isEmpty()) {
                Join<Funcion, Pelicula> peliculaJoin = root.join("pelicula");
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(peliculaJoin.get("titulo")),
                                "%" + filtro.getPelicula().toLowerCase() + "%"
                        )
                );
            }
            if (filtro.getSala() != null && !filtro.getSala().isEmpty()) {
                Join<Funcion, Sala> salaJoin = root.join("sala");
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(salaJoin.get("nombre")),
                                "%" + filtro.getSala().toLowerCase() + "%"
                        )
                );
            }
            if (filtro.getPrecioMinimo() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("precio"),
                                filtro.getPrecioMinimo()
                        )
                );
            }
            if (filtro.getPrecioMaximo() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("precio"),
                                filtro.getPrecioMaximo()
                        )
                );
            }
            if (filtro.getFechaYhoraMinima() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("inicio"),
                                filtro.getFechaYhoraMinima()
                        )
                );
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
