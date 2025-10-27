package com.proyect.cineclub.specification;

import com.proyect.cineclub.dto.SalaFiltroDto;
import com.proyect.cineclub.entity.Sala;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SalaSpecificationBuilder {
    public static Specification<Sala> construirFiltros(SalaFiltroDto filtro){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filtro.getNombre() != null && !filtro.getNombre().isEmpty()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("nombre")),
                                "%" + filtro.getNombre().toLowerCase() + "%"
                        )
                );
            }
            if (filtro.getCapacidadMinima() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("capacidad"),
                                filtro.getCapacidadMinima()
                        )
                );
            }
            if (filtro.getCapacidadMaxima() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("capacidad"),
                                filtro.getCapacidadMaxima()
                        )
                );
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
