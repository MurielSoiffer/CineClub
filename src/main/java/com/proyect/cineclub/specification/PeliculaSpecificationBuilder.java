package com.proyect.cineclub.specification;

import com.proyect.cineclub.dto.PeliculaFiltroDto;
import com.proyect.cineclub.entity.Pelicula;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PeliculaSpecificationBuilder {
    public static Specification<Pelicula> construirFiltros(PeliculaFiltroDto filtro) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filtro.getTitulo() != null && !filtro.getTitulo().isEmpty()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("titulo")),
                                "%" + filtro.getTitulo().toLowerCase() + "%"
                        )
                );
            }
            if (filtro.getDuracionMinima() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("duracion"),
                                filtro.getDuracionMinima()
                        )
                );
            }
            if (filtro.getDuracionMaxima() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("duracion"),
                                filtro.getDuracionMaxima()
                        )
                );
            }
            if (filtro.getEdadMinima() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("edadMinima"),
                                filtro.getEdadMinima()
                        )
                );
            }

            if (filtro.getFechaEstrenoDesde() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("fechaEstreno"),
                                filtro.getFechaEstrenoDesde()
                        )
                );
            }
// Combinar todos los predicados con AND
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
}
}