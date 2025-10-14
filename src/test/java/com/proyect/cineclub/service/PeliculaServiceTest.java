package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Pelicula;
import com.proyect.cineclub.repository.PeliculaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculaServiceTest {

    @Mock
    private PeliculaRepository peliculaRepository;

    @InjectMocks
    private PeliculaService peliculaService;

    private Pelicula createPelicula(Long id, String titulo, Long duracion) {
        Pelicula p = new Pelicula(); // Usa el constructor por defecto (sin argumentos)
        p.setId(id);
        p.setTitulo(titulo);
        p.setSinopsis("Sinopsis de ejemplo para " + titulo);
        p.setDuracion(duracion);
        p.setFechaEstreno(LocalDate.of(2023, 1, 1));
        p.setEdadMinima(7);
        return p;
    }
    private final Pelicula peliculaEjemplo = createPelicula(1L, "Dune", 155L);
    private final Pelicula peliculaEjemplo2 = createPelicula(2L, "Interstellar", 169L);

    // ------------------------------------------
    //             TESTS PARA save()
    // ------------------------------------------

    @Test
    void cuandoGuardarPelicula_debeRetornarPeliculaGuardada() {
        when(peliculaRepository.save(any(Pelicula.class))).thenReturn(peliculaEjemplo);

        Pelicula resultado = peliculaService.save(peliculaEjemplo);

        assertNotNull(resultado);
        assertEquals("Dune", resultado.getTitulo());

        // Verifica que el método save() del repositorio fue llamado exactamente una vez.
        verify(peliculaRepository, times(1)).save(peliculaEjemplo);
    }

    // ------------------------------------------
    //           TESTS PARA updateById()
    // ------------------------------------------

    @Test
    void cuandoActualizarPeliculaExistente_debeRetornarPeliculaActualizada() {
        Long id = 1L;
        Pelicula requestUpdate = new Pelicula();
        requestUpdate.setTitulo("Dune: Parte Dos");
        requestUpdate.setSinopsis("Sinopsis actualizada.");
        requestUpdate.setDuracion(166L);

        when(peliculaRepository.findById(id)).thenReturn(Optional.of(peliculaEjemplo));
        when(peliculaRepository.save(any(Pelicula.class))).thenAnswer(i -> i.getArguments()[0]);

        Pelicula resultado = peliculaService.updateById(requestUpdate, id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId(), "El ID debe mantenerse.");
        assertEquals("Dune: Parte Dos", resultado.getTitulo(), "El título debe actualizarse.");
        assertEquals(166L, resultado.getDuracion(), "La duración debe actualizarse.");

        verify(peliculaRepository, times(1)).findById(id);
        verify(peliculaRepository, times(1)).save(any(Pelicula.class));
    }

    // ------------------------------------------
    //             TESTS PARA getAll()
    // ------------------------------------------

    @Test
    void cuandoGetAll_debeRetornarPaginaDePeliculas() {
        List<Pelicula> peliculas = Arrays.asList(peliculaEjemplo, peliculaEjemplo2);
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Pelicula> pageMock = new PageImpl<>(peliculas, pageable, peliculas.size());

        when(peliculaRepository.findAll(pageable)).thenReturn(pageMock);

        Page<Pelicula> resultado = peliculaService.getAll(pageable);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.getTotalElements());
        verify(peliculaRepository, times(1)).findAll(pageable);
    }

    // ------------------------------------------
    //             TESTS PARA getById()
    // ------------------------------------------

    @Test
    void cuandoGetByIdExistente_debeRetornarOptionalConPelicula() {
        Long id = 1L;
        when(peliculaRepository.findById(id)).thenReturn(Optional.of(peliculaEjemplo));

        Optional<Pelicula> resultado = peliculaService.getById(id);

        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        verify(peliculaRepository, times(1)).findById(id);
    }

    @Test
    void cuandoGetByIdNoExistente_debeRetornarOptionalVacio() {
        Long id = 99L;
        when(peliculaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Pelicula> resultado = peliculaService.getById(id);

        assertFalse(resultado.isPresent());
        verify(peliculaRepository, times(1)).findById(id);
    }

    // ------------------------------------------
    //            TESTS PARA deleteById()
    // ------------------------------------------

    @Test
    void cuandoDeleteById_debeLlamarAlMetodoDeleteDelRepositorio() {
        Long id = 1L;

        peliculaService.deleteById(id);

        verify(peliculaRepository, times(1)).deleteById(id);
    }
}