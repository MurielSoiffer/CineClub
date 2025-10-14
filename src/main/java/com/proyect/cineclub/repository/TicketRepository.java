package com.proyect.cineclub.repository;

import com.proyect.cineclub.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
