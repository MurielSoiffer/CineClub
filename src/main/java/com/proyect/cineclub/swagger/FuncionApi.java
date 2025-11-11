package com.proyect.cineclub.swagger;

import com.proyect.cineclub.dto.FuncionDto;
import com.proyect.cineclub.dto.HoldRequest;
import com.proyect.cineclub.dto.TicketDto;
import com.proyect.cineclub.entity.Funcion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Funcion", description = "Gestión de las funciones (horarios y salas de proyección).")
@RequestMapping("/api/funciones")
public interface FuncionApi {

    /**
     * Crea una nueva función.
     */
    @Operation(
            summary = "Crear una nueva función",
            description = "Registra una nueva función asociando una película, una sala, un horario y un precio.",
            requestBody = @RequestBody(
                    description = "Ejemplo de POST para registrar una funcion",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"pelicula\": {\"id\": 5 }, \"sala\": { \"id\": 2 },\"inicio\": \"2030-11-10T23:55:17.801Z\",\"finalizacion\": \"2030-11-10T23:58:17.801Z\",\"activa\": true, \"precio\": 1500}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Created: Función creada exitosamente.",
                            responseCode = "200", // o 201 Created
                            content = @Content(schema = @Schema(implementation = FuncionDto.class))
                    ),
                    @ApiResponse(
                            description = "Bad Request: Datos de entrada inválidos.",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error falta de autorización",
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/funcion\"}"
                                            )
                                    })
                    ),
                    @ApiResponse(
                            description = "Forbidden: Permisos insuficientes (Ej. rol no autorizado).",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error de Permisos",
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\", \"path\": \"/api/funcion\"}"
                                            )
                                    })
                    )
            }
    )
    @PostMapping
    FuncionDto save(@RequestBody @Valid Funcion f);

    /**
     * Busca funciones aplicando diversos filtros y paginación.
     */
    @Operation(
            summary = "Buscar y filtrar funciones",
            description = "Obtiene una lista paginada de funciones, con filtros opcionales por película, sala, fecha/hora y rango de precio.",
            parameters = {
                    @Parameter(name = "pelicula", description = "Filtra por título de la película.", required = false),
                    @Parameter(name = "sala", description = "Filtra por nombre de la sala.", required = false),
                    @Parameter(name = "fechaYhoraMinima", description = "Fecha y hora mínima de inicio (ISO 8601).", required = false, example = "2025-11-10T19:00:00"),
                    @Parameter(name = "precioMinimo", description = "Precio mínimo del ticket.", required = false),
                    @Parameter(name = "precioMaximo", description = "Precio máximo del ticket.", required = false),
                    @Parameter(
                            name = "pageable",
                            description = "Parámetros de paginación y ordenación.",
                            example = "page=0&size=10&sort=id,ASC"
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Success: Lista paginada de funciones.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error falta de autorización",
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/funcion\"}"
                                            )
                                    })
                    )
            }
    )
    @GetMapping
    ResponseEntity<Page<FuncionDto>> buscarFuncion(
            @RequestParam(required = false) String pelicula,
            @RequestParam(required = false) String sala,
            @RequestParam(required = false) LocalDateTime fechaYhoraMinima,
            @RequestParam(required = false) Long precioMinimo,
            @RequestParam(required = false) Long precioMaximo,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    );

    /**
     * Obtiene la información de una función específica por su ID.
     */
    @Operation(
            summary = "Obtener función por ID",
            description = "Busca y retorna una función por su identificador único.",
            responses = {
                    @ApiResponse(
                            description = "Success: Función encontrada.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = FuncionDto.class))
                    ),
                    @ApiResponse(
                            description = "Not Found: La función con el ID especificado no existe.",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error falta de autorización",
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/funcion\"}"
                                            )
                                    })
                    )
            }
    )
    @GetMapping(path = "/{id}")
    ResponseEntity<FuncionDto> getById(@PathVariable("id") Long id);

    /**
     * Obtiene el estado de las butacas de una función, indicando LIBRE, RESERVADO u OCUPADO.
     */
    @Operation(
            summary = "Obtener estado de butacas por función",
            description = "Retorna una lista del estado actual de las butacas para una función (ej. 'A1 :LIBRE', 'B2 :OCUPADO').",
            responses = {
                    @ApiResponse(
                            description = "Success: Lista de estados de butacas.",
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))
                    ),
                    @ApiResponse(
                            description = "Not Found: La función no fue encontrada.",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error falta de autorización",
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/funcion\"}"
                                            )
                                    })
                    )
            }
    )
    @GetMapping(path = "/{id}/butacas")
    ResponseEntity<List<String>> getButacasSala(@PathVariable("id") Long id);

    /**
     * Crea una reserva temporal (HOLD) para una o más butacas en una función.
     */
    @Operation(
            summary = "Reservar butacas (Hold)",
            description = "Crea una reserva temporal para butacas específicas. Requiere autenticación de usuario.",
            requestBody = @RequestBody(
                    description = "Ejemplo de POST para reservar un ticket",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"butacas\": [\n" +
                                            "    \"A-1\"\n" +
                                            "  ],\n" +
                                            "  \"ttlSeconds\": 600\n" +
                                            "}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Created: Reserva temporal (Hold) creada exitosamente.",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = TicketDto.class))
                    ),
                    @ApiResponse(
                            description = "Bad Request: Butacas inválidas o ya reservadas/ocupadas.",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error falta de autorización",
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/funcion\"}"
                                            )
                                    })
                    ),
                    @ApiResponse(
                            description = "Conflict",
                            responseCode = "409",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"error\": \"Conflict\",\"message\": \"Butaca - no disponible\", \"status\": 409,\"timestamp\": \"2025-11-10T15:00:00Z\"}"
                                            )
                                    })
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"error\": \"Bad Request\",\"message\": \"No se permiten holds tan cerca del inicio de la función. La hora límite es --\", \"status\": 400,\"timestamp\": \"2025-11-10T15:00:00Z\"}"
                                            )
                                    })
                    ),
                    @ApiResponse(
                            description = "Not Found: La función no existe.",
                            responseCode = "404"
                    )
            }
    )
    @PostMapping("/{id}/holds")
    ResponseEntity<TicketDto> createHold(
            @Parameter(description = "ID de la función.") @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody HoldRequest request);

    /**
     * Actualiza la información de una función existente por su ID.
     */
    @Operation(
            summary = "Actualizar función por ID",
            description = "Actualiza la información de una función existente.",
            requestBody = @RequestBody(
                    description = "Ejemplo de PUT para modificar una funcion",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"inicio\": \"2030-11-10T23:55:17.801Z\",\"finalizacion\": \"2030-11-10T23:58:17.801Z\",\"activa\": true}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Success: Función actualizada.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = FuncionDto.class))
                    ),
                    @ApiResponse(
                            description = "Not Found: La función con el ID especificado no existe.",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error falta de autorización",
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/funcion\"}"
                                            )
                                    })
                    ),
                    @ApiResponse(
                            description = "Forbidden: Permisos insuficientes (Ej. rol no autorizado).",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error de Permisos",
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\", \"path\": \"/api/funcion\"}"
                                            )
                                    })
                    )
            }
    )
    @PutMapping(path = "/{id}")
    ResponseEntity<FuncionDto> updateById(@RequestBody Funcion request, @PathVariable("id") long id);

    /**
     * Elimina una función por su ID.
     */
    @Operation(
            summary = "Eliminar función por ID",
            description = "Elimina permanentemente la función con el ID especificado.",
            responses = {
                    @ApiResponse(
                            description = "No Content: Función eliminada exitosamente.",
                            responseCode = "200" // o 204 No Content
                    ),
                    @ApiResponse(
                            description = "Not Found: La función a eliminar no existe.",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error falta de autorización",
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/funcion\"}"
                                            )
                                    })
                    ),
                    @ApiResponse(
                            description = "Forbidden: Permisos insuficientes (Ej. rol no autorizado).",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error de Permisos",
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\", \"path\": \"/api/funcion\"}"
                                            )
                                    })
                    )
            }
    )
    @DeleteMapping(path = "/{id}")
    void deleteFuncionById(@PathVariable("id") Long id);
}
