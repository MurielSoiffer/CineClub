package com.proyect.cineclub.configuration;

import com.proyect.cineclub.dto.SalaDto;
import com.proyect.cineclub.entity.Sala;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;

@Tag(name = "Salas", description = "Gestión de las salas de proyección del cineclub.")
@RequestMapping("/api/salas")
public interface SalaApi {

    /**
     * Crea una nueva sala.
     */
    @Operation(
            summary = "Crear una nueva sala",
            description = "Registra una nueva sala con su configuración inicial (capacidad, nombre, etc.).",
            requestBody = @RequestBody(
                    description = "Ejemplo de POST para registrar una sala",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"nombre\": \"Sala 3\",\"capacidad\": 15}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Created: Sala creada exitosamente.",
                            responseCode = "200", // o 201 Created
                            content = @Content(schema = @Schema(implementation = SalaDto.class))
                    ),
                    @ApiResponse(
                            description = "Bad Request: Datos de entrada inválidos.",
                            responseCode = "400"
                    ),@ApiResponse(
                    description = "Forbidden: Permisos insuficientes (Ej. rol no autorizado).",
                    responseCode = "403",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\",\"path\": \"/api/salas\"}"
                                    )
                            })
            ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/salas\"}"
                                            )
                                    })
                    )
            }
    )
    @PostMapping
    SalaDto save(@RequestBody @Valid Sala sala);

    /**
     * Busca salas aplicando filtros opcionales de nombre y capacidad, con paginación.
     */
    @Operation(
            summary = "Buscar y filtrar salas",
            description = "Obtiene una lista paginada de salas, con filtros opcionales por nombre y rango de capacidad.",
            parameters = {
                    @Parameter(name = "nombre", description = "Filtra por nombre de la sala (coincidencia parcial).", required = false),
                    @Parameter(name = "capacidadMinima", description = "Capacidad mínima requerida para la sala.", required = false),
                    @Parameter(name = "capacidadMaxima", description = "Capacidad máxima permitida para la sala.", required = false),
                    @Parameter(
                            name = "pageable",
                            description = "Parámetros de paginación y ordenación.",
                            example = "{\"page\": 0, \"size\": 5, \"sort\": \"id\"}"
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Success: Lista paginada de salas.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    ),@ApiResponse(
                    description = "Forbidden: Permisos insuficientes (Ej. rol no autorizado).",
                    responseCode = "403",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\",\"path\": \"/api/salas\"}"
                                    )
                            })
            ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/salas\"}"
                                            )
                                    })
                    )
            }
    )
    @GetMapping
    ResponseEntity<Page<SalaDto>> buscarSalas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer capacidadMinima,
            @RequestParam(required = false) Integer capacidadMaxima,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    );

    /**
     * Obtiene la información de una sala específica por su ID.
     */
    @Operation(
            summary = "Obtener sala por ID",
            description = "Busca y retorna una sala por su ID.",
            responses = {
                    @ApiResponse(
                            description = "Success: Sala encontrada.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = SalaDto.class))
                    ),
                    @ApiResponse(
                            description = "Not Found: La sala con el ID especificado no existe.",
                            responseCode = "404"
                    ),@ApiResponse(
                    description = "Forbidden: Permisos insuficientes (Ej. rol no autorizado).",
                    responseCode = "403",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\",\"path\": \"/api/salas\"}"
                                    )
                            })
            ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/salas\"}"
                                            )
                                    })
                    )
            }
    )
    @GetMapping(path = "/{id}")
    ResponseEntity<SalaDto> getById(@PathVariable("id") Long id);

    /**
     * Obtiene las etiquetas de las butacas de una sala específica.
     */
    @Operation(
            summary = "Obtener butacas de una sala",
            description = "Retorna una lista de las etiquetas de las butacas (ej. A1, B5) de la sala especificada.",
            responses = {
                    @ApiResponse(
                            description = "Success: Lista de etiquetas de butacas.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = List.class))
                    ),
                    @ApiResponse(
                            description = "Not Found: La sala no fue encontrada.",
                            responseCode = "404"
                    ),@ApiResponse(
                    description = "Forbidden: Permisos insuficientes (Ej. rol no autorizado).",
                    responseCode = "403",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\",\"path\": \"/api/salas\"}"
                                    )
                            })
            ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/salas\"}"
                                            )
                                    })
                    )
            }
    )
    @GetMapping(path = "/{id}/butacas")
    ResponseEntity<List<String>> getButacasSala(@PathVariable("id") Long id);

    /**
     * Actualiza la información de una sala existente por su ID.
     */
    @Operation(
            summary = "Actualizar sala por ID",
            description = "Actualiza la información de una sala existente.",
            requestBody = @RequestBody(
                    description = "Ejemplo de PUT para modificar una sala",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"nombre\": \"Sala 3\",\"capacidad\": 15}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Success: Sala actualizada.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = SalaDto.class))
                    ),
                    @ApiResponse(
                            description = "Not Found: La sala con el ID especificado no existe.",
                            responseCode = "404"
                    ),@ApiResponse(
                    description = "Forbidden: Permisos insuficientes (Ej. rol no autorizado).",
                    responseCode = "403",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\",\"path\": \"/api/salas\"}"
                                    )
                            })
            ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/salas\"}"
                                            )
                                    })
                    )
            }
    )
    @PutMapping(path = "/{id}")
    ResponseEntity<SalaDto> updateById(@RequestBody Sala request, @PathVariable("id") long id);

    /**
     * Elimina una sala por su ID.
     */
    @Operation(
            summary = "Eliminar sala por ID",
            description = "Elimina permanentemente la sala con el ID especificado.",
            responses = {
                    @ApiResponse(
                            description = "No Content: Sala eliminada exitosamente.",
                            responseCode = "200" // o 204 No Content
                    ),
                    @ApiResponse(
                            description = "Not Found: La sala a eliminar no existe.",
                            responseCode = "404"
                    ),@ApiResponse(
                    description = "Forbidden: Permisos insuficientes (Ej. rol no autorizado).",
                    responseCode = "403",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\",\"path\": \"/api/salas\"}"
                                    )
                            })
            ),
                    @ApiResponse(
                            description = "Unauthorized: Se requiere autenticación.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/salas\"}"
                                            )
                                    })
                    )
            }
    )
    @DeleteMapping(path = "/{id}")
    void deleteSalaById(@PathVariable("id") Long id);
}
