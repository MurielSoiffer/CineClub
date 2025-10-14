package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.repository.ButacaRepository;
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
public class ButacaServiceTest {

    @Mock
    private ButacaRepository butacaRepository;

    @InjectMocks
    private ButacaService butacaService;

    private Butaca butacaEjemplo;
    private Butaca butacaEjemplo2;

    @BeforeEach
    void setUp() {
        butacaEjemplo = new Butaca();
        butacaEjemplo.setId(1L);
        butacaEjemplo.setFila("A");
        butacaEjemplo.setNumero(5);
        butacaEjemplo.setEtiqueta("A-5");

        butacaEjemplo2 = new Butaca();
        butacaEjemplo2.setId(2L);
        butacaEjemplo2.setFila("B");
        butacaEjemplo2.setNumero(10);
        butacaEjemplo2.setEtiqueta("B-10");
    }

    // ------------------------------------------
    //             TESTS PARA save()
    // ------------------------------------------

    @Test
    void cuandoGuardarButaca_debeRetornarButacaGuardada() {
        when(butacaRepository.save(butacaEjemplo)).thenReturn(butacaEjemplo);

        Butaca resultado = butacaService.save(butacaEjemplo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("A-5", resultado.getEtiqueta());

        verify(butacaRepository, times(1)).save(butacaEjemplo);
    }

    // ------------------------------------------
    //           TESTS PARA updateById()
    // ------------------------------------------

    @Test
    void cuandoActualizarButacaExistente_debeModificarFilaYNumero() {
        Long id = 1L;

        Butaca requestUpdate = new Butaca();
        requestUpdate.setFila("C");
        requestUpdate.setNumero(3);

        when(butacaRepository.findById(id)).thenReturn(Optional.of(butacaEjemplo));

        when(butacaRepository.save(any(Butaca.class))).thenAnswer(i -> i.getArguments()[0]);

        Butaca resultado = butacaService.updateById(requestUpdate, id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId(), "El ID debe mantenerse.");
        assertEquals("C", resultado.getFila(), "La fila debe actualizarse a 'C'.");
        assertEquals(3, resultado.getNumero(), "El número debe actualizarse a 3.");

        verify(butacaRepository, times(1)).findById(id);
        verify(butacaRepository, times(1)).save(any(Butaca.class));
    }

    // ------------------------------------------
    //             TESTS PARA getAll()
    // ------------------------------------------

    @Test
    void cuandoGetAll_debeRetornarListaDeButacas() {
        List<Butaca> butacas = Arrays.asList(butacaEjemplo, butacaEjemplo2);
        when(butacaRepository.findAll()).thenReturn(butacas);

        List<Butaca> resultado = butacaService.getAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("A-5", resultado.get(0).getEtiqueta());

        verify(butacaRepository, times(1)).findAll();
    }

    // ------------------------------------------
    //             TESTS PARA getById()
    // ------------------------------------------

    @Test
    void cuandoGetByIdExistente_debeRetornarOptionalConButaca() {
        Long id = 1L;
        when(butacaRepository.findById(id)).thenReturn(Optional.of(butacaEjemplo));

        Optional<Butaca> resultado = butacaService.getById(id);

        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());

        verify(butacaRepository, times(1)).findById(id);
    }

    @Test
    void cuandoGetByIdNoExistente_debeRetornarOptionalVacio() {
        Long id = 99L;
        when(butacaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Butaca> resultado = butacaService.getById(id);

        assertFalse(resultado.isPresent());

        verify(butacaRepository, times(1)).findById(id);
    }

    // ------------------------------------------
    //            TESTS PARA deleteById()
    // ------------------------------------------

    @Test
    void cuandoDeleteById_debeLlamarAlMetodoDeleteDelRepositorio() {
        Long id = 1L;

        butacaService.deleteById(id);

        verify(butacaRepository, times(1)).deleteById(id);
    }
}