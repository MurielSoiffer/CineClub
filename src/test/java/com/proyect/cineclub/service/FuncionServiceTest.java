package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.entity.Pelicula;
import com.proyect.cineclub.entity.Sala;
import com.proyect.cineclub.exception.RecursoNoEncontradoException;
import com.proyect.cineclub.repository.FuncionRepository;
import com.proyect.cineclub.repository.PeliculaRepository;
import com.proyect.cineclub.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FuncionServiceTest {

    @Mock
    private FuncionRepository funcionRepository;
    @Mock
    private PeliculaService peliculaService;
    @Mock
    private SalaService salaService;

    @InjectMocks
    private FuncionService funcionService;

    private Pelicula peliculaEjemplo;
    private Sala salaEjemplo;
    private Funcion funcionRequest;
    private Funcion funcionGuardada;

    @BeforeEach
    void setUp() {
        peliculaEjemplo = new Pelicula();
        peliculaEjemplo.setId(1L);
        peliculaEjemplo.setTitulo("Dune");

        salaEjemplo = new Sala();
        salaEjemplo.setId(10L);
        salaEjemplo.setNombre("Sala VIP");

        Pelicula pId = new Pelicula();
        pId.setId(1L);
        Sala sId = new Sala();
        sId.setId(10L);

        funcionRequest = new Funcion();
        funcionRequest.setPelicula(pId);
        funcionRequest.setSala(sId);
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        funcionRequest.setInicio(inicio);
        funcionRequest.setFinalizacion(inicio.plusHours(2));
        funcionRequest.setActiva(true);

        funcionGuardada = new Funcion();
        funcionGuardada.setId(5L);
        funcionGuardada.setPelicula(peliculaEjemplo);
        funcionGuardada.setSala(salaEjemplo);
        funcionGuardada.setActiva(true);
    }

    // ------------------------------------------
    //             TESTS PARA save()
    // ------------------------------------------

    @Test
    void cuandoGuardarFuncion_debeAsociarEntidadesYGuardar() {
        when(peliculaService.getById(1L)).thenReturn(Optional.of(peliculaEjemplo));
        when(salaService.getById(10L)).thenReturn(Optional.of(salaEjemplo));
        when(funcionRepository.save(any(Funcion.class))).thenReturn(funcionGuardada);

        Funcion resultado = funcionService.save(funcionRequest);

        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals(peliculaEjemplo.getTitulo(), resultado.getPelicula().getTitulo());
        assertEquals(salaEjemplo.getNombre(), resultado.getSala().getNombre());

        verify(peliculaService, times(1)).getById(1L);
        verify(salaService, times(1)).getById(10L);
        verify(funcionRepository, times(1)).save(any(Funcion.class));
    }

    @Test
    void cuandoPeliculaNoExisteEnSave_debeLanzarExcepcion() {
        when(peliculaService.getById(1L)).thenReturn(Optional.empty());

        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            funcionService.save(funcionRequest);
        });

        assertEquals("Pelicula no encontrada con ID: " + funcionRequest.getPelicula().getId(), exception.getMessage());

        verify(salaService, never()).getById(anyLong());
        verify(funcionRepository, never()).save(any(Funcion.class));
    }

    @Test
    void cuandoSalaNoExisteEnSave_debeLanzarExcepcion() {
        when(peliculaService.getById(1L)).thenReturn(Optional.of(peliculaEjemplo));
        when(salaService.getById(10L)).thenReturn(Optional.empty());

        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            funcionService.save(funcionRequest);
        });

        assertEquals("Sala no encontrada con ID: " + funcionRequest.getSala().getId(), exception.getMessage());

        verify(peliculaService, times(1)).getById(1L);
        verify(salaService, times(1)).getById(10L);
        verify(funcionRepository, never()).save(any(Funcion.class));
    }

    @Test
    void cuandoFuncionSeSuperpone_debeLanzarExcepcion() {
        // 1. Configurar Mocks para Película y Sala (que sí existen)
        when(peliculaService.getById(1L)).thenReturn(Optional.of(peliculaEjemplo));
        when(salaService.getById(10L)).thenReturn(Optional.of(salaEjemplo));

        // 2. Configurar el Mock para la superposición (debe devolver una lista no vacía)
        Funcion funcionSuperpuesta = new Funcion();
        funcionSuperpuesta.setId(99L); // ID diferente
        funcionSuperpuesta.setSala(salaEjemplo);
        when(funcionRepository.findSuperpuesta(any(Sala.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(funcionSuperpuesta));

        // 3. Ejecutar y verificar la excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            funcionService.save(funcionRequest);
        });

        verify(peliculaService, times(1)).getById(1L);
        verify(salaService, times(1)).getById(10L);
        // Verificamos que se llamó al método de superposición
        verify(funcionRepository, times(1)).findSuperpuesta(any(Sala.class), any(LocalDateTime.class), any(LocalDateTime.class));
        // Verificamos que la función NO se guardó
        verify(funcionRepository, never()).save(any(Funcion.class));
    }

    // ------------------------------------------
    //           TESTS PARA updateById()
    // ------------------------------------------

    @Test
    void cuandoActualizarFuncion_soloDebeModificarActivaYGuardar() {
        Long id = 5L;
        Funcion funcionExistente = funcionGuardada;

        Funcion requestUpdate = new Funcion();
        requestUpdate.setActiva(false);

        when(funcionRepository.findById(id)).thenReturn(Optional.of(funcionExistente));
        when(funcionRepository.save(any(Funcion.class))).thenAnswer(i -> i.getArguments()[0]);

        Funcion resultado = funcionService.updateById(requestUpdate, id);

        assertNotNull(resultado);
        assertFalse(resultado.getActiva(), "El estado Activa debe ser 'false'.");

        verify(funcionRepository, times(1)).findById(id);
        verify(funcionRepository, times(1)).save(any(Funcion.class));
    }

    // ------------------------------------------
    //             TESTS ESTÁNDAR
    // ------------------------------------------

    @Test
    void cuandoGetAll_debeRetornarPaginaDeFunciones() {
        List<Funcion> funciones = Arrays.asList(funcionGuardada);
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Funcion> pageMock = new PageImpl<>(funciones, pageable, funciones.size());

        when(funcionRepository.findAll(pageable)).thenReturn(pageMock);

        Page<Funcion> resultado = funcionService.getAll(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(funcionRepository, times(1)).findAll(pageable);
    }

    @Test
    void cuandoGetByIdExistente_debeRetornarOptionalConFuncion() {
        Long id = 5L;
        when(funcionRepository.findById(id)).thenReturn(Optional.of(funcionGuardada));

        Optional<Funcion> resultado = funcionService.getById(id);

        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        verify(funcionRepository, times(1)).findById(id);
    }

    @Test
    void cuandoDeleteById_debeLlamarAlMetodoDeleteDelRepositorio() {
        Long id = 5L;

        funcionService.deleteById(id);

        verify(funcionRepository, times(1)).deleteById(id);
    }
}