package com.proyect.cineclub.service;

import com.proyect.cineclub.security.Rol;
import com.proyect.cineclub.entity.Usuario;
import com.proyect.cineclub.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioEjemplo;
    private Usuario usuarioRequest;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setId(1L);
        usuarioEjemplo.setUsername("testuser");
        usuarioEjemplo.setApellido("ApellidoTest");
        usuarioEjemplo.setContraseña("hashed_password_123");
        usuarioEjemplo.setRol(Rol.USER);

        usuarioRequest = new Usuario();
        usuarioRequest.setUsername("testuser");
        usuarioRequest.setContraseña("password_original");
        usuarioRequest.setRol(Rol.USER);
    }

    // ------------------------------------------
    //             TESTS PARA save()
    // ------------------------------------------

    @Test
    void cuandoGuardarUsuario_debeEncriptarContrasenaYLlamarSave() {
        when(passwordEncoder.encode(usuarioRequest.getContraseña()))
                .thenReturn("hashed_password_123");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEjemplo);

        Usuario resultado = usuarioService.save(usuarioRequest);

        assertNotNull(resultado);
        assertEquals("hashed_password_123", resultado.getContraseña(), "Debe usar la contraseña encriptada.");

        verify(passwordEncoder, times(1)).encode("password_original");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // ------------------------------------------
    //           TESTS PARA loadUserByUsername()
    // ------------------------------------------

    @Test
    void cuandoLoadUserByUsernameExiste_debeRetornarUsuario() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(usuarioEjemplo);

        UserDetails resultado = usuarioService.loadUserByUsername("testuser");

        assertNotNull(resultado);
        assertEquals("testuser", resultado.getUsername());
        verify(usuarioRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void cuandoLoadUserByUsernameNoExiste_debeLanzarExcepcion() {
        when(usuarioRepository.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            usuarioService.loadUserByUsername("nonexistent");
        });
        verify(usuarioRepository, times(1)).findByUsername("nonexistent");
    }

    // ------------------------------------------
    //           TESTS PARA updateById()
    // ------------------------------------------

    @Test
    void cuandoActualizarUsuarioExistente_debeReemplazarContrasenaEncriptada() {
        Long id = 1L;
        Usuario requestUpdate = new Usuario();
        requestUpdate.setContraseña("new_original_password");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEjemplo));

        when(passwordEncoder.encode("new_original_password")).thenReturn("new_hashed_password_456");

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        Usuario resultado = usuarioService.updateById(requestUpdate, id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("new_hashed_password_456", resultado.getContraseña(), "La contraseña debe actualizarse y encriptarse.");

        verify(passwordEncoder, times(1)).encode("new_original_password");
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // ------------------------------------------
    //             TESTS PARA getAll()
    // ------------------------------------------

    @Test
    void cuandoGetAll_debeRetornarPaginaDeUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuarioEjemplo);
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Usuario> pageMock = new PageImpl<>(usuarios, pageable, usuarios.size());

        when(usuarioRepository.findAll(pageable)).thenReturn(pageMock);

        Page<Usuario> resultado = usuarioService.getAll(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(usuarioRepository, times(1)).findAll(pageable);
    }

    // ------------------------------------------
    //             TESTS PARA deleteById()
    // ------------------------------------------

    @Test
    void cuandoDeleteById_debeLlamarAlMetodoDeleteDelRepositorio() {
        Long id = 1L;

        usuarioService.deleteById(id);

        verify(usuarioRepository, times(1)).deleteById(id);
    }
}