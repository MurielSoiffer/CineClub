package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Ticket;
import com.proyect.cineclub.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;

    public Ticket save(Ticket butaca){
        return ticketRepository.save(butaca);
    }

    @Transactional
    public Ticket updateById(Ticket request, Long id) {
        Optional<Ticket> ticketExistente = ticketRepository.findById(id);
        if(ticketExistente.isEmpty()) {
            // .orElseThrow(() -> new RuntimeException("Pelicula no encontrada"));
        }

        ticketExistente.get().setEstado(request.getEstado());

        return ticketRepository.save(ticketExistente.get());
    }

    public List<Ticket> getAll(){return ticketRepository.findAll();}

    public Optional<Ticket> getById(Long id){return ticketRepository.findById(id);}

    public void deleteById(Long id){ticketRepository.deleteById(id);}
}
