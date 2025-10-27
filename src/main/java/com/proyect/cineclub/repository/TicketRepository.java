package com.proyect.cineclub.repository;

import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.entity.Ticket;
import com.proyect.cineclub.security.EstadoTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    Optional<Ticket> findFirstByFuncionAndButacaAndEstadoIn(
            Funcion funcion,
            Butaca butaca,
            List<EstadoTicket> estadosNoLiberados
    );
}
