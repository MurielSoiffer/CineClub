package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Ticket;
import com.proyect.cineclub.repository.TicketRepository;
import com.proyect.cineclub.security.EstadoTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticketEjemplo;
    private Ticket ticketEjemplo2;

    @BeforeEach
    void setUp() {
        ticketEjemplo = new Ticket();
        ticketEjemplo.setId(101L);
        ticketEjemplo.setEstado(EstadoTicket.HOLD);

        ticketEjemplo2 = new Ticket();
        ticketEjemplo2.setId(102L);
        ticketEjemplo2.setEstado(EstadoTicket.CONFIRMADO);
    }

    // ------------------------------------------
    //             TESTS PARA save()
    // ------------------------------------------

    @Test
    void cuandoGuardarTicket_debeRetornarTicketGuardado() {
        when(ticketRepository.save(ticketEjemplo)).thenReturn(ticketEjemplo);
        Ticket resultado = ticketService.save(ticketEjemplo);

        assertNotNull(resultado);
        assertEquals(101L, resultado.getId());
        assertEquals(EstadoTicket.HOLD, resultado.getEstado());

        verify(ticketRepository, times(1)).save(ticketEjemplo);
    }

    // ------------------------------------------
    //           TESTS PARA updateById()
    // ------------------------------------------

    @Test
    void cuandoActualizarEstadoExistente_debeModificarEstado() {
        Long id = 101L;

        Ticket requestUpdate = new Ticket();
        requestUpdate.setEstado(EstadoTicket.CANCELADO);

        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticketEjemplo));

        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket resultado = ticketService.updateById(requestUpdate, id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId(), "El ID debe mantenerse.");
        assertEquals(EstadoTicket.CANCELADO, resultado.getEstado(), "El estado debe actualizarse a 'CANCELADO'.");

        verify(ticketRepository, times(1)).findById(id);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void cuandoActualizarEstadoAConfirmado_debeModificarEstado() {
        Long id = 101L;

        Ticket requestUpdate = new Ticket();
        requestUpdate.setEstado(EstadoTicket.CONFIRMADO);

        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticketEjemplo));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket resultado = ticketService.updateById(requestUpdate, id);

        assertEquals(EstadoTicket.CONFIRMADO, resultado.getEstado());
    }

    // ------------------------------------------
    //             TESTS ESTÁNDAR
    // ------------------------------------------

    @Test
    void cuandoGetAll_debeRetornarListaDeTickets() {
        List<Ticket> tickets = Arrays.asList(ticketEjemplo, ticketEjemplo2);
        when(ticketRepository.findAll()).thenReturn(tickets);

        List<Ticket> resultado = ticketService.getAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void cuandoGetByIdExistente_debeRetornarOptionalConTicket() {
        Long id = 101L;
        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticketEjemplo));

        Optional<Ticket> resultado = ticketService.getById(id);

        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());

        verify(ticketRepository, times(1)).findById(id);
    }

    @Test
    void cuandoDeleteById_debeLlamarAlMetodoDeleteDelRepositorio() {
        Long id = 101L;

        ticketService.deleteById(id);

        verify(ticketRepository, times(1)).deleteById(id);
    }
}