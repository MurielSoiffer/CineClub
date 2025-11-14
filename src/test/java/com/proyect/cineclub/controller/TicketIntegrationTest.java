package com.proyect.cineclub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyect.cineclub.entity.*;
import com.proyect.cineclub.repository.*;
import com.proyect.cineclub.security.EstadoTicket;
import com.proyect.cineclub.security.Rol;
import com.proyect.cineclub.service.SalaService;
import com.proyect.cineclub.service.TicketService;
import com.proyect.cineclub.service.UsuarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize; // Importación necesaria para verificar el tamaño de la lista
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TicketIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Repositorios y Servicios para la gestión de datos de prueba
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private FuncionRepository funcionRepository;
    @Autowired
    private ButacaRepository butacaRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private SalaRepository salaRepository;
    @Autowired
    private PeliculaRepository peliculaRepository;
    @Autowired
    private SalaService salaService;

    // Entidades de prueba
    private Usuario user1;
    private Usuario user2;
    private Funcion funcionFutura;
    private Funcion funcionProxima;
    private Sala salaTest;
    private Pelicula peliculaTest;
    private Butaca butacaA1;
    private Butaca butacaA2;

    @BeforeEach
    void setUp() {
        // **PASO CRÍTICO:** Limpiar todos los datos antes de cada prueba.
        ticketRepository.deleteAllInBatch();
        funcionRepository.deleteAllInBatch();
        butacaRepository.deleteAllInBatch();
        usuarioRepository.deleteAllInBatch();
        salaRepository.deleteAllInBatch();
        peliculaRepository.deleteAllInBatch();

        // 1. Configuración de Usuarios
        // Asume que el username en @WithMockUser corresponde al email
        user1 = new Usuario();
        user1.setUsername("user1@test.com");
        user1.setApellido("apellido");
        user1.setContraseña("password");
        user1.setRol(Rol.USER);
        user2 = new Usuario();
        user2.setUsername("user2@test.com");
        user2.setApellido("apellido");
        user2.setContraseña("password");
        user2.setRol(Rol.USER);
        usuarioService.save(user1);
        usuarioService.save(user2);

        salaTest = new Sala();
        salaTest.setCapacidad(10);
        salaTest.setButacas(null);
        salaTest.setNombre("Sala Principal");
        salaTest = salaService.save(salaTest);


        peliculaTest = new Pelicula();
        peliculaTest.setTitulo("Movie Test");
        peliculaTest.setSinopsis("Esta es una sinopsis de prueba para la validación.");
        peliculaTest.setDuracion(120L);
        peliculaTest.setFechaEstreno(LocalDate.now().minusDays(30));
        peliculaTest.setEdadMinima(0);
        peliculaTest = peliculaRepository.save(peliculaTest);


        // 2. Configuración de Butacas (deben persistirse)
        butacaA1 = new Butaca();
        butacaA1.setEtiqueta("A-1");
        butacaA1.setNumero(1);
        butacaA1.setFila("A");
        butacaA1.setSala(salaTest);
        butacaA2 = new Butaca();
        butacaA2.setEtiqueta("A-2");
        butacaA2.setNumero(2);
        butacaA2.setFila("A");
        butacaA2.setSala(salaTest);
        butacaRepository.saveAll(List.of(butacaA1, butacaA2));

        // 3. Configuración de Funciones
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

        // Funcion Futura: Inicia en 20 min (Pasa la validación de umbral >10 min)
        LocalDateTime inicioFuturo = now.plusMinutes(20);
        funcionFutura = new Funcion();
        funcionFutura.setSala(null);
        funcionFutura.setPelicula(null);
        funcionFutura.setInicio(inicioFuturo);
        funcionFutura.setFinalizacion(inicioFuturo.plusHours(2));
        funcionFutura.setActiva(true);
        funcionFutura.setPrecio(null);
        funcionFutura.setPelicula(peliculaTest);
        funcionFutura.setSala(salaTest);
        funcionFutura.setPrecio(1L);
        funcionRepository.save(funcionFutura);

        // Funcion Próxima: Inicia en 5 min (Falla la validación de umbral <10 min)
        LocalDateTime inicioProximo = now.plusMinutes(5);
        funcionProxima = new Funcion();
        funcionProxima.setSala(null);
        funcionProxima.setPelicula(null);
        funcionProxima.setInicio(inicioProximo);
        funcionProxima.setFinalizacion(inicioProximo.plusHours(2));
        funcionProxima.setActiva(true);
        funcionProxima.setPrecio(null);
        funcionProxima.setPelicula(peliculaTest);
        funcionProxima.setSala(salaTest);
        funcionProxima.setPrecio(1L);
        funcionRepository.save(funcionProxima);
    }

    @AfterEach
    void tearDown() {
        // **PASO CRÍTICO:** Limpiar todos los datos después de cada prueba.
        ticketRepository.deleteAllInBatch();
        funcionRepository.deleteAllInBatch();
        butacaRepository.deleteAllInBatch();
        usuarioRepository.deleteAllInBatch();
    }

    // -----------------------------------------------------------------
    // --- FLUJOS FELICES (Happy Path) ---
    // -----------------------------------------------------------------

    /**
     * Requisito: Creación de Hold para UNA butaca y listado de tickets propios.
     * Endpoint: POST /api/funciones/{id}/holds
     * **MODIFICADO para verificar que la respuesta es una lista.**
     */
    @Test
    void test1_CreateHoldAndGetMyTickets_HappyFlow() throws Exception {

        SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority(user1.getRol().name());
        // 1. Preparar Hold Request
        Map<String, Object> holdRequest = Map.of(
                "butacas", List.of(butacaA1.getEtiqueta()),
                "ttlSeconds", 60
        );

        // 2. Creación del Hold (POST /api/funciones/{id}/holds)
        mockMvc.perform(post("/api/funciones/{id}/holds", funcionFutura.getId())
                        .with(user(user1.getUsername()).authorities(userAuthority))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(holdRequest)))
                .andExpect(status().isCreated()) // 201 CREATED
                // Verifica que es una lista con un elemento
                .andExpect(jsonPath("$", hasSize(1)))
                // Verifica el estado del primer (y único) elemento
                .andExpect(jsonPath("$[0].estado", is(EstadoTicket.HOLD.name())));

        // 3. Listar mis tickets (GET /api/tickets/me)
        mockMvc.perform(get("/api/tickets/me")
                        .with(user(user1.getUsername()).authorities(userAuthority))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].estado", is(EstadoTicket.HOLD.name())));
    }

    /**
     * Requisito: Creación de Hold para MULTIPLES butacas.
     * Endpoint: POST /api/funciones/{id}/holds
     */
    @Test
    void test_CreateHold_MultipleSeatsHappyFlow() throws Exception {
        SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority(user1.getRol().name());

        // 1. Preparar Hold Request con dos butacas
        Map<String, Object> holdRequest = Map.of(
                "butacas", List.of(butacaA1.getEtiqueta(), butacaA2.getEtiqueta()),
                "ttlSeconds", 60
        );

        // 2. Creación de los Holds (POST /api/funciones/{id}/holds)
        mockMvc.perform(post("/api/funciones/{id}/holds", funcionFutura.getId())
                        .with(user(user1.getUsername()).authorities(userAuthority))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(holdRequest)))
                .andExpect(status().isCreated()) // 201 CREATED
                // Verifica que la respuesta contiene dos tickets
                .andExpect(jsonPath("$", hasSize(2)))
                // Verifica el estado del primer elemento
                .andExpect(jsonPath("$[0].estado", is(EstadoTicket.HOLD.name())))
                // Verifica el estado del segundo elemento
                .andExpect(jsonPath("$[1].estado", is(EstadoTicket.HOLD.name())));

        // 3. Verificación en la BD
        List<Ticket> tickets = ticketRepository.findByFuncion(funcionFutura);
        assertEquals(2, tickets.size());
        assertEquals(EstadoTicket.HOLD, tickets.get(0).getEstado());
        assertEquals(EstadoTicket.HOLD, tickets.get(1).getEstado());
    }

    /**
     * Requisito: Confirmación de Compra.
     * Endpoint: POST /api/tickets/{id}/confirm
     */
    @Test
    @WithMockUser(username = "user1@test.com", roles = "USER")
    void test2_ConfirmTicket_HappyFlow() throws Exception {
        // Setup: Crear un hold (se usa el service para obtener el ID rápidamente)
        // El service ahora devuelve una lista, tomamos el primer elemento.
        Ticket holdTicket = ticketService.createHold(funcionFutura.getId(), List.of(butacaA1.getEtiqueta()), user1.getId()).getFirst();

        // Acción: Confirmar el hold
        mockMvc.perform(post("/api/tickets/{id}/confirm", holdTicket.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is(EstadoTicket.CONFIRMADO.name())));

        // Verificación en BD
        Ticket confirmedTicket = ticketRepository.findById(holdTicket.getId()).orElseThrow();
        assertEquals(EstadoTicket.CONFIRMADO, confirmedTicket.getEstado());
    }

    /**
     * Requisito: Cancelación de Hold.
     * Endpoint: DELETE /api/tickets/{id}
     */
    @Test
    @WithMockUser(username = "user1@test.com", roles = "USER")
    void test3_CancelHold_HappyFlow() throws Exception {
        // Setup: Crear un hold para user1
        // El service ahora devuelve una lista, tomamos el primer elemento.
        Ticket holdTicket = ticketService.createHold(funcionFutura.getId(), List.of(butacaA1.getEtiqueta()), user1.getId()).getFirst();

        // Acción: Cancelar el hold
        mockMvc.perform(delete("/api/tickets/{id}", holdTicket.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is(EstadoTicket.CANCELADO.name())));

        // Verificación en BD
        Ticket cancelledTicket = ticketRepository.findById(holdTicket.getId()).orElseThrow();
        assertEquals(EstadoTicket.CANCELADO, cancelledTicket.getEstado());
    }

    // -----------------------------------------------------------------
    // --- FLUJOS DE ERROR Y RESTRICCIONES ---
    // -----------------------------------------------------------------

    /**
     * Error 1: Conflicto de Butaca (Butaca Ocupada/en Hold).
     * Endpoint: POST /api/funciones/{id}/holds
     * **Verifica que el error ocurre cuando se pide una butaca ya ocupada.**
     */
    @Test
    @WithMockUser(username = "user2@test.com", roles = "USER")
    void test4_CreateHold_ConflictError() throws Exception {
        // Setup: user1 crea un HOLD activo en butaca A1
        ticketService.createHold(funcionFutura.getId(), List.of(butacaA1.getEtiqueta()), user1.getId());

        // Preparar solicitud de user2 para la misma butaca A1
        Map<String, Object> holdRequest = Map.of(
                "butacas", List.of(butacaA1.getEtiqueta()),
                "ttlSeconds", 60
        );

        // Acción: user2 intenta crear un hold en A1
        mockMvc.perform(post("/api/funciones/{id}/holds", funcionFutura.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(holdRequest)))
                .andExpect(status().isConflict()); // 409 CONFLICT (ButacaOcuadaException)
    }

    /**
     * Error 1b: Conflicto de Butaca (Una ocupada, la otra libre).
     * Endpoint: POST /api/funciones/{id}/holds
     * **Verifica que si una butaca en la lista está ocupada, la transacción falla por completo.**
     */
    @Test
    @WithMockUser(username = "user2@test.com", roles = "USER")
    void test4b_CreateHold_PartialConflictError() throws Exception {
        // Setup: user1 crea un HOLD activo en butaca A1
        ticketService.createHold(funcionFutura.getId(), List.of(butacaA1.getEtiqueta()), user1.getId());

        // Antes de la acción, solo hay 1 ticket
        assertEquals(1, ticketRepository.count());

        // Preparar solicitud de user2 para A1 (ocupada) y A2 (libre)
        Map<String, Object> holdRequest = Map.of(
                "butacas", List.of(butacaA1.getEtiqueta(), butacaA2.getEtiqueta()),
                "ttlSeconds", 60
        );

        // Acción: user2 intenta crear un hold en ambas
        mockMvc.perform(post("/api/funciones/{id}/holds", funcionFutura.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(holdRequest)))
                .andExpect(status().isConflict()); // 409 CONFLICT (ButacaOcuadaException)

        // Verificación: Como la operación es @Transactional, NO se debe haber creado el ticket para A2.
        // El conteo de tickets sigue siendo 1.
        assertEquals(1, ticketRepository.count(), "La operación transaccional debió revertirse al fallar por A1.");
    }

    /**
     * Error 2: Umbral de Tiempo de la Función.
     * Endpoint: POST /api/funciones/{id}/holds
     * Requisito: La función debe comenzar en más de 10 minutos (por defecto).
     */
    @Test
    @WithMockUser(username = "user1@test.com", roles = "USER")
    void test5_CreateHold_TooCloseToStartError() throws Exception {
        // La funcionProxima fue creada para iniciar en 5 minutos (< 10 min, falla).
        Map<String, Object> holdRequest = Map.of(
                "butacas", List.of(butacaA2.getEtiqueta()),
                "ttlSeconds", 60
        );

        // Acción: Intentar crear hold para esta función próxima
        mockMvc.perform(post("/api/funciones/{id}/holds", funcionProxima.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(holdRequest)))
                .andExpect(status().isBadRequest()); // 400 BAD_REQUEST (HoldTooCloseToStartException)
    }

    /**
     * Error 3: Propiedad de Recurso (Operación sobre ticket ajeno).
     * Endpoint: POST /api/tickets/{id}/confirm
     */
    @Test
    @WithMockUser(username = "user2@test.com", roles = "USER") // Autenticado como user2
    void test6_ConfirmTicket_ForbiddenError() throws Exception {
        // Setup: user1 crea un hold activo
        // El service ahora devuelve una lista, tomamos el primer elemento.
        Ticket holdTicket = ticketService.createHold(funcionFutura.getId(), List.of(butacaA1.getEtiqueta()), user1.getId()).getFirst();

        // Acción: user2 intenta confirmar el ticket de user1
        mockMvc.perform(post("/api/tickets/{id}/confirm", holdTicket.getId()))
                .andExpect(status().isForbidden()); // 403 FORBIDDEN (TicketNoPermitidoException)

        // Verificación: El ticket sigue en HOLD
        Ticket ticket = ticketRepository.findById(holdTicket.getId()).orElseThrow();
        assertEquals(EstadoTicket.HOLD, ticket.getEstado());
    }

    /**
     * Error 4: Confirmación de Hold Expirado.
     * Endpoint: POST /api/tickets/{id}/confirm
     */
    @Test
    @WithMockUser(username = "user1@test.com", roles = "USER")
    void test7_ConfirmTicket_ExpiredStateError() throws Exception {
        // Setup: Crear un hold para user1
        // El service ahora devuelve una lista, tomamos el primer elemento.
        Ticket holdTicket = ticketService.createHold(funcionFutura.getId(), List.of(butacaA1.getEtiqueta()), user1.getId()).getFirst();

        // **Simular Vencimiento Manual (Precondición para la prueba):**
        holdTicket.setEstado(EstadoTicket.EXPIRADO);
        ticketRepository.save(holdTicket);

        // Acción: Intentar confirmar el ticket EXPIRED
        mockMvc.perform(post("/api/tickets/{id}/confirm", holdTicket.getId()))
                .andExpect(status().isUnprocessableEntity()); // 422 Unprocessable Entity (TicketExpiradoException)
    }
}