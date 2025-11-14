package com.proyect.cineclub.controller;

import com.proyect.cineclub.dto.TicketDto;
import com.proyect.cineclub.entity.Usuario;
import com.proyect.cineclub.service.TicketService;
import com.proyect.cineclub.service.UsuarioService;
import com.proyect.cineclub.configuration.TicketApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketController implements TicketApi {
    @Autowired
    TicketService ticketService;

    @Autowired
    UsuarioService usuarioService;

    @Override
    public ResponseEntity<List<TicketDto>> getMyTickets(Authentication authentication) {
        String username = authentication.getName();

        Usuario usuario = usuarioService.findByUsername(username);
        Long userId;
        if (usuario != null) {
            userId = usuario.getId();
        } else {
            return ResponseEntity.notFound().build();
        }

        List<TicketDto> myTickets = ticketService.getTicketsByUserId(userId).stream().map(TicketDto::fromTicket).toList();

        return ResponseEntity.ok(myTickets);
    }
    @Override
    public ResponseEntity<TicketDto> cancelById(@PathVariable("id") long id, Authentication authentication){
        String username = authentication.getName();

        Usuario usuario = usuarioService.findByUsername(username);
        return ResponseEntity.ok(TicketDto.fromTicket(this.ticketService.cancelById(id, usuario)));
    }
    @Override
    public ResponseEntity<TicketDto> confirmById(@PathVariable("id") long id,Authentication authentication){
        String username = authentication.getName();

        Usuario usuario = usuarioService.findByUsername(username);
        return ResponseEntity.ok(TicketDto.fromTicket(this.ticketService.confirmById(id, usuario)));
    }
}
