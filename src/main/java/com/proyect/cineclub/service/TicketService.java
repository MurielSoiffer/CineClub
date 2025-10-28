package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.entity.Ticket;
import com.proyect.cineclub.repository.ButacaRepository;
import com.proyect.cineclub.repository.FuncionRepository;
import com.proyect.cineclub.repository.TicketRepository;
import com.proyect.cineclub.repository.UsuarioRepository;
import com.proyect.cineclub.security.EstadoTicket;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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


        @Value("${cineclub.hold.default-ttl-seconds:300}")
        private int defaultTtlSeconds;

        @Value("${cineclub.hold.max-seats-per-user:6}")
        private int maxButacasPerUser;

        @Value("${cineclub.hold.min-minutes-before-screening:10}")
        private int minMinutosAntseFuncions;

        @Transactional
        public Ticket createHold(Long screeningId, List<Long> seatIds, Integer ttlSeconds, Long userId) {

            Funcion funcion = funcionService.getById(screeningId)
                    .orElseThrow(() -> new EntityNotFoundException("Función no encontrada"));

            List<Butaca> seats = butacaService.getAllById(seatIds);
            if (seats.size() != seatIds.size()) {
//                throw new ValidationException("Una o más butacas no existen");
            }

            ZoneId cinemaZone = ZoneId.systemDefault();
            LocalDateTime nowInCinemaZone = LocalDateTime.now(cinemaZone);
            LocalDateTime holdNotAllowedBefore = nowInCinemaZone.plus(minMinutosAntseFuncions, ChronoUnit.MINUTES);

            if (funcion.getInicio().isBefore(holdNotAllowedBefore)) {
                // throw new BusinessException(HttpStatus.BAD_REQUEST, "No se permiten holds tan cerca del inicio de la función.");
            }


            int finalTtl = ttlSeconds != null ? ttlSeconds : defaultTtlSeconds;
            Instant expirationTime = Instant.now().plusSeconds(finalTtl);

            Ticket newHold = new Ticket();
            newHold.setFuncion(funcion);
            newHold.setButaca(seats.get(0));
            newHold.setUsuario(usuarioService.getReferenceById(userId));
            newHold.setEstado(EstadoTicket.HOLD);
            newHold.setHoldExpirationTime(expirationTime);

            return ticketRepository.save(newHold);
        }
}
