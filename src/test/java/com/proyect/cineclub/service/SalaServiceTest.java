package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.entity.Sala;
import com.proyect.cineclub.repository.ButacaRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SalaServiceTest {
    @Mock
    private SalaRepository salaRepository;

    @Mock
    private ButacaRepository butacaRepository;

    @InjectMocks
    private SalaService salaService;

    private Sala salaEjemplo;

    private Sala createButaca(Long id, String nombre, int capacidad) {
        Sala s = new Sala();
        s.setId(id);
        s.setNombre(nombre);
        s.setCapacidad(capacidad);
        s.setButacas(new ArrayList<>());
        return s;
    }

    @BeforeEach
    void setUp() {
        salaEjemplo = new Sala();
        salaEjemplo.setNombre("Sala Test");
        salaEjemplo.setCapacidad(20);
        salaEjemplo.setId(1L);
        salaEjemplo.setButacas(new ArrayList<>());
    }

    private Butaca createButaca(Sala sala, String fila, int numero) {
        Butaca b = new Butaca();
        b.setSala(sala);
        b.setFila(fila);
        b.setNumero(numero);
        b.setEtiqueta(fila + "-" + numero);
        return b;
    }

    // ------------------------------------------
    //             TESTS PARA save()
    // ------------------------------------------

    @Test
    void cuandoGuardarSala_debeGenerarYGuardarButacas() {
        Sala nuevaSala = new Sala();
        nuevaSala.setNombre("Sala Cine");
        nuevaSala.setCapacidad(15);

        Sala salaConId = new Sala();
        salaConId.setNombre("Sala Cine");
        salaConId.setCapacidad(15);
        salaConId.setId(2L);
        salaConId.setButacas(new ArrayList<>());

        when(salaRepository.save(any(Sala.class))).thenReturn(salaConId);

        Sala resultado = salaService.save(nuevaSala);

        assertNotNull(resultado);
        assertEquals(15, resultado.getButacas().size(), "Debe tener 15 butacas generadas.");
        assertEquals("A-1", resultado.getButacas().get(0).getEtiqueta(), "Verifica la primera butaca.");
        assertEquals("B-5", resultado.getButacas().get(14).getEtiqueta(), "Verifica la última butaca (Fila B, Asiento 5).");

        verify(salaRepository, times(1)).save(any(Sala.class));
        verify(butacaRepository, times(1)).saveAll(argThat(iterable -> {
            Collection<Butaca> butacas = (Collection<Butaca>) iterable;

            return butacas.size() == 15;
        }));
    }

    // ------------------------------------------
    //           TESTS PARA updateById()
    // ------------------------------------------

    @Test
    void cuandoActualizarCapacidadAumenta_debeAgregarButacas() {
        Long id = 1L;
        List<Butaca> butacasOriginales = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String fila = (i <= 10) ? "A" : "B";
            int num = (i <= 10) ? i : i - 10;
            butacasOriginales.add(createButaca(salaEjemplo, fila, num));
        }
        salaEjemplo.setCapacidad(20);
        salaEjemplo.setButacas(butacasOriginales);

        Sala requestUpdate = new Sala();
        requestUpdate.setNombre("Sala Grande");
        requestUpdate.setCapacidad(25);

        when(salaRepository.findById(id)).thenReturn(Optional.of(salaEjemplo));
        when(salaRepository.save(any(Sala.class))).thenAnswer(i -> i.getArguments()[0]);

        Sala resultado = salaService.updateById(requestUpdate, id);

        assertEquals(25, resultado.getCapacidad(), "La capacidad debe ser 25.");
        assertEquals(25, resultado.getButacas().size(), "Debe haber 5 butacas añadidas.");
        assertEquals("C-1", resultado.getButacas().get(20).getEtiqueta());
        assertEquals("C-5", resultado.getButacas().get(24).getEtiqueta());

        verify(salaRepository, times(1)).findById(id);
        verify(salaRepository, times(1)).save(any(Sala.class));
    }

    @Test
    void cuandoActualizarCapacidadDisminuye_debeEliminarExcesoDeButacas() {
        Long id = 1L;
        List<Butaca> butacasOriginales = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String fila = (i <= 10) ? "A" : "B";
            int num = (i <= 10) ? i : i - 10;
            butacasOriginales.add(createButaca(salaEjemplo, fila, num));
        }
        salaEjemplo.setCapacidad(20);
        salaEjemplo.setButacas(butacasOriginales);

        Sala requestUpdate = new Sala();
        requestUpdate.setNombre("Sala Pequeña");
        requestUpdate.setCapacidad(15);

        when(salaRepository.findById(id)).thenReturn(Optional.of(salaEjemplo));
        when(salaRepository.save(any(Sala.class))).thenAnswer(i -> i.getArguments()[0]);

        Sala resultado = salaService.updateById(requestUpdate, id);

        assertEquals(15, resultado.getCapacidad(), "La capacidad debe ser 15.");
        assertEquals(15, resultado.getButacas().size(), "Debe haber 5 butacas eliminadas.");

        assertEquals("B-5", resultado.getButacas().get(14).getEtiqueta());

        verify(salaRepository, times(1)).findById(id);
        verify(salaRepository, times(1)).save(any(Sala.class));
    }

    // ------------------------------------------
    //             TESTS PARA getAll()
    // ------------------------------------------

    @Test
    void cuandoGetAll_debeRetornarPaginaDeSalas() {
        Sala sala2 = createButaca(2L, "Sala B", 10);
        List<Sala> salas = Arrays.asList(salaEjemplo, sala2);
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Sala> pageMock = new PageImpl<>(salas, pageable, salas.size());

        when(salaRepository.findAll(pageable)).thenReturn(pageMock);

        Page<Sala> resultado = salaService.getAll(pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        verify(salaRepository, times(1)).findAll(pageable);
    }

}