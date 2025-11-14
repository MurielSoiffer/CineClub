package com.proyect.cineclub.service;

import com.proyect.cineclub.dto.TicketDto;
import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.entity.Ticket;
import com.proyect.cineclub.entity.Usuario;
import com.proyect.cineclub.exception.*;
import com.proyect.cineclub.repository.ButacaRepository;
import com.proyect.cineclub.repository.FuncionRepository;
import com.proyect.cineclub.repository.TicketRepository;
import com.proyect.cineclub.repository.UsuarioRepository;
import com.proyect.cineclub.security.EstadoTicket;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    FuncionService funcionService;

    @Autowired
    ButacaService butacaService;

    @Autowired
    UsuarioService usuarioService;

    public Ticket save(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket updateById(Ticket request, Long id) {
        Ticket ticketExistente = ticketRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket",id));

        ticketExistente.setEstado(request.getEstado());

        return ticketRepository.save(ticketExistente);
    }

    @Transactional
    public Ticket cancelById(Long id, Usuario usuario){
        Ticket ticketExistente = ticketRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket",id));

        if (usuario.getId() == ticketExistente.getUsuario().getId())
            ticketExistente.setEstado(EstadoTicket.CANCELADO);
        else
            throw new TicketNoPermitidoException(id);

        return ticketRepository.save(ticketExistente);
    }

    @Transactional
    public Ticket confirmById(Long id, Usuario usuario){
        Ticket ticketExistente = ticketRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket",id));


        if (usuario.getId() == ticketExistente.getUsuario().getId())
            if(ticketExistente.getEstado() == EstadoTicket.HOLD)
                ticketExistente.setEstado(EstadoTicket.CONFIRMADO);
            else
                throw new TicketExpiradoException();
        else
            throw new TicketNoPermitidoException(id);

        return ticketRepository.save(ticketExistente);
    }

    public List<Ticket> getTicketsByUserId(Long userId) {
        Usuario usuario = usuarioService.getReferenceById(userId);
        return ticketRepository.findByUsuario(usuario);
    }

    public List<Ticket> getAll(){return ticketRepository.findAll();}

    public Optional<Ticket> getById(Long id){return ticketRepository.findById(id);}

    public void deleteById(Long id){ticketRepository.deleteById(id);}


    @Value("${HOLD_TTL_DEFAULT:300}")
    private int defaultTtlSeconds;

    @Value("${USER_HOLD_LIMIT:6}")
    private int maxButacasPerUser;

    @Value("${PRE_SCREENING_THRESHOLD:10}")
    private int minMinutosAntseFuncions;

    @Transactional
    public List<Ticket> createHold(Long screeningId, List<String> seat, Long userId) {



        Funcion funcion = funcionService.getById(screeningId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Funcion",screeningId));

        List<Butaca> seats = butacaService.getAllByEtiquetaAndSala(seat,funcion.getSala());
        if (seat.size() != seats.size()) {
            throw new ButacaInexistenteException();
        }
        if(seats.size() > maxButacasPerUser){
            throw new DemasiadasButacasException(maxButacasPerUser);
        }

        ZoneId cinemaZone = ZoneId.systemDefault();
        LocalDateTime nowInCinemaZone = LocalDateTime.now(cinemaZone);
        LocalDateTime holdNotAllowedBefore = nowInCinemaZone.plusMinutes(minMinutosAntseFuncions);

        if (funcion.getInicio().isBefore(holdNotAllowedBefore)) {
            throw new HoldTooCloseToStartException(holdNotAllowedBefore);
        }

        int finalTtl = defaultTtlSeconds;
        Instant expirationTime = Instant.now().plusSeconds(finalTtl);

        List<Ticket> createdTickets = new ArrayList<>();

        for (Butaca butaca : seats) {
            Optional<Ticket> existingTicketOptional = ticketRepository.findByFuncionAndButaca(funcion, butaca);

            if (existingTicketOptional.isPresent()) {
                Ticket existingTicket = existingTicketOptional.get();
                EstadoTicket estadoActual = existingTicket.getEstado();

                if (estadoActual == EstadoTicket.CANCELADO || estadoActual == EstadoTicket.EXPIRADO) {
                    existingTicket.setUsuario(usuarioService.getReferenceById(userId));
                    existingTicket.setEstado(EstadoTicket.HOLD);
                    existingTicket.setHoldExpirationTime(expirationTime);

                    createdTickets.add(ticketRepository.save(existingTicket));

                } else if (estadoActual == EstadoTicket.HOLD || estadoActual == EstadoTicket.CONFIRMADO) {
                    throw new ButacaOcuadaException(butaca.getEtiqueta());
                }
            } else {
                Ticket newTicket = new Ticket();
                newTicket.setFuncion(funcion);
                newTicket.setButaca(butaca);
                newTicket.setUsuario(usuarioService.getReferenceById(userId));
                newTicket.setEstado(EstadoTicket.HOLD);
                newTicket.setHoldExpirationTime(expirationTime);

                createdTickets.add(ticketRepository.save(newTicket));
            }
        }

        return createdTickets;
    }

    @Scheduled(fixedRateString = "${app.hold.cleanup-rate-ms:60000}")
    @Transactional
    public void cleanupExpiredHolds() {
        Instant now = Instant.now();

        List<Ticket> expiredHolds = ticketRepository
                .findByEstadoAndHoldExpirationTimeBefore(EstadoTicket.HOLD, now);

        if (!expiredHolds.isEmpty()) {
            System.out.println("Cambiando el estado de " + expiredHolds.size() + " tickets de HOLD a EXPIRADO.");
        }
        for (Ticket ticket : expiredHolds) {
            ticket.setEstado(EstadoTicket.EXPIRADO);
        }
    }

    @Scheduled(fixedRateString = "${app.ticket.cleanup-rate-ms:600000}") // 600000 ms = 10 minutos
    @Transactional
    public void cleanupCanceledAndExpiredTickets() {
        List<EstadoTicket> estadosAEliminar = Arrays.asList(EstadoTicket.CANCELADO, EstadoTicket.EXPIRADO);

        int deletedCount = ticketRepository.deleteByEstadoInBulk(estadosAEliminar);

        if (deletedCount > 0) {
            System.out.println("Se han eliminado " + deletedCount + " tickets en estado CANCELADO o EXPIRADO de la base de datos.");
        }
    }
}
