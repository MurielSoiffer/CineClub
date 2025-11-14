package com.proyect.cineclub.configuration;

import com.proyect.cineclub.entity.Pelicula;
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

import java.time.LocalDate;

@Tag(name = "Pelicula", description = "Gestión del catálogo de películas del cineclub.")
@RequestMapping("/api/peliculas")
public interface PeliculaApi {

    /**
     * Crea una nueva película.
     */
    @Operation(
            summary = "Registrar una nueva película",
            description = "Añade una nueva película al catálogo del cineclub.",
            requestBody = @RequestBody(
                    description = "Ejemplo de POST para registrar una pelicula",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"titulo\": \"string\",\"sinopsis\": \"string\",\"duracion\": 1,\"fechaEstreno\": \"2025-11-11\",\"edadMinima\": 1}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Created: Película creada exitosamente.",
                            responseCode = "200", // o 201 Created
                            content = @Content(schema = @Schema(implementation = Pelicula.class))
                    ),
                    @ApiResponse(
                            description = "Bad Request: Datos de entrada inválidos (ej. validación fallida).",
                            responseCode = "400"
                    ),@ApiResponse(
                    description = "Forbidden: Permisos insuficientes (Ej. rol no autorizado).",
                    responseCode = "403",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\",\"path\": \"/api/pelicula\"}"
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
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/pelicula\"}"
                                            )
                                    })
                    )
            }
    )
    @PostMapping
    Pelicula save(@RequestBody @Valid Pelicula pelicula);

    /**
     * Busca películas aplicando diversos filtros y paginación.
     */
    @Operation(
            summary = "Buscar y filtrar películas",
            description = "Obtiene una lista paginada de películas, con filtros opcionales como título, duración, edad mínima y fecha de estreno.",
            parameters = {
                    @Parameter(name = "titulo", description = "Filtra por título de la película (coincidencia parcial).", required = false),
                    @Parameter(name = "duracionMaxima", description = "Duración máxima en formato String (ej. '90 min').", required = false),
                    @Parameter(name = "duracionMimina", description = "Duración mínima en formato String.", required = false),
                    @Parameter(name = "fechaEstrenoDesde", description = "Fecha de estreno mínima (ej. YYYY-MM-DD).", required = false),
                    @Parameter(name = "edadMinima", description = "Edad mínima requerida para ver la película.", required = false),
                    @Parameter(
                            name = "pageable",
                            description = "Parámetros de paginación y ordenación.",
                            example = "page=0&size=10&sort=id,ASC"
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Success: Lista paginada de películas.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    )
            }
    )
    @GetMapping
    ResponseEntity<Page<Pelicula>> buscarPeliculas(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String duracionMaxima,
            @RequestParam(required = false) String duracionMimina,
            @RequestParam(required = false) LocalDate fechaEstrenoDesde,
            @RequestParam(required = false) Integer edadMinima,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    );

    /**
     * Obtiene la información de una película específica por su ID.
     */
    @Operation(
            summary = "Obtener película por ID",
            description = "Busca y retorna una película por su identificador único.",
            responses = {
                    @ApiResponse(
                            description = "Success: Película encontrada.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Pelicula.class))
                    ),
                    @ApiResponse(
                            description = "Not Found: La película con el ID especificado no existe.",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping(path = "/{id}")
    ResponseEntity<Pelicula> getById(@PathVariable("id") Long id);

    /**
     * Actualiza la información de una película existente por su ID.
     */
    @Operation(
            summary = "Actualizar película por ID",
            description = "Actualiza la información de una película existente.",
            requestBody = @RequestBody(
                    description = "Ejemplo de PUT para modificar una pelicula",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"titulo\": \"string\",\"sinopsis\": \"string\",\"duracion\": 1}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Success: Película actualizada.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Pelicula.class))
                    ),
                    @ApiResponse(
                            description = "Not Found: La película con el ID especificado no existe.",
                            responseCode = "404"
                    ),@ApiResponse(
                    description = "Forbidden: Permisos insuficientes (Ej. rol no autorizado).",
                    responseCode = "403",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\",\"path\": \"/api/pelicula\"}"
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
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/pelicula\"}"
                                            )
                                    })
                    )
            }
    )
    @PutMapping(path = "/{id}")
    ResponseEntity<Pelicula> updateById(@RequestBody Pelicula request, @PathVariable("id") long id);

    /**
     * Elimina una película por su ID.
     */
    @Operation(
            summary = "Eliminar película por ID",
            description = "Elimina permanentemente la película con el ID especificado.",
            responses = {
                    @ApiResponse(
                            description = "No Content: Película eliminada exitosamente.",
                            responseCode = "200" // o 204 No Content
                    ),
                    @ApiResponse(
                            description = "Not Found: La película a eliminar no existe.",
                            responseCode = "404"
                    ),@ApiResponse(
                    description = "Forbidden: Permisos insuficientes (Ej. rol no autorizado).",
                    responseCode = "403",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\",\"path\": \"/api/pelicula\"}"
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
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/pelicula\"}"
                                            )
                                    })
                    )
            }
    )
    @DeleteMapping(path = "/{id}")
    void deletePeliculaById(@PathVariable("id") Long id);
}
