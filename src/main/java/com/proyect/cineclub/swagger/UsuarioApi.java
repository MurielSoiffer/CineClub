package com.proyect.cineclub.swagger;

import com.proyect.cineclub.dto.UsuarioDto;
import com.proyect.cineclub.entity.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;

@Tag(name = "Usuario", description = "Operaciones relacionadas con la gestión de usuarios")
@RequestMapping("api/usuarios")
public interface UsuarioApi {

    @Operation(
            description = "Endpoint para obtener una lista paginada de usuarios.",
            summary = "Obtener todos los usuarios con paginación",
            responses = {
                    @ApiResponse(
                            description = "Success: Lista de usuarios obtenida.",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "[{\"id\": 1, \"username\": \"pepe\", \"apellido\": \"Rodriguez\", \"contraseña\": \"xxxxxxx\", \"rol\": \"USER\"}]"
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
                                                    name = "Error falta de autorización",
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/usuario\"}"
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
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\", \"path\": \"/api/usuario\"}"
                                            )
                                    })
                    )
            },
            parameters = @Parameter(
                    name = "pageable",
                    description = "Parámetros de paginación (página, tamaño y ordenación). Por defecto: page=0, size=5, sort=id",
                    example = "{\"page\": 0, \"size\": 5, \"sort\": \"id\"}"
            )
    )
    @GetMapping
    List<Usuario> getUsuarios(@PageableDefault(size = 5, sort = "id") Pageable pageable);

    /**
     * Crea un nuevo usuario en el sistema.
     */
    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea un nuevo usuario utilizando los datos proporcionados en el cuerpo de la solicitud.",
            responses = {
                    @ApiResponse(
                            description = "Created: Usuario creado exitosamente.",
                            responseCode = "200", // A menudo se usa 201 Created para POST
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Usuario.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Bad Request: Datos de entrada inválidos.",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{}"
                                            )
                                    })
                    )
            },
            requestBody = @RequestBody(
                    description = "Ejemplo de POST para registrar un nuevo usuario",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"username\": \"Juan\",\"apellido\": \"Rodriguez\", \"contraseña\": \"1234\"}"
                            )
                    )
            )

    )
    @PostMapping
    Usuario save(@RequestBody @Valid UsuarioDto usuarioDto);

    @Operation(
            summary = "Actualizar usuario por ID",
            description = "Actualiza la información de un usuario existente.",
            requestBody = @RequestBody(
                    description = "Ejemplo de PUT para modificar un usuario",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"username\": \"Juan\",\"apellido\": \"Rodriguez\", \"rol\": \"ADMIN\"}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Success: Usuario actualizado.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Usuario.class))
                    ),
                    @ApiResponse(
                            description = "Not Found: El usuario con el ID especificado no existe.",
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
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/usuario\"}"
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
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\", \"path\": \"/api/usuario\"}"
                                            )
                                    })
                    )
            }
    )
    @PutMapping(path = "/{id}")
    ResponseEntity<Usuario> updateById(@RequestBody Usuario request, @PathVariable("id") long id);

    @Operation(
            summary = "Obtener usuario por ID",
            description = "Busca y retorna un usuario por su ID.",
            responses = {
                    @ApiResponse(
                            description = "Success: Usuario encontrado.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Usuario.class))
                    ),
                    @ApiResponse(
                            description = "Not Found: El usuario no fue encontrado.",
                            responseCode = "404",
                            content = @Content(
                                    examples = @ExampleObject(
                                        value = "{}"
                            ))
                    )
            }
    )
    @GetMapping(path = "/{id}")
    ResponseEntity<Usuario> getById(@PathVariable("id") Long id);

    @Operation(
            summary = "Eliminar usuario por ID",
            description = "Elimina permanentemente el usuario con el ID especificado.",
            responses = {
                    @ApiResponse(
                            description = "No Content: Usuario eliminado exitosamente.",
                            responseCode = "200" // A menudo se usa 204 No Content para DELETE
                    ),
                    @ApiResponse(
                            description = "Not Found: El usuario a eliminar no existe.",
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
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/usuario\"}"
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
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\", \"path\": \"/api/usuario\"}"
                                            )
                                    })
                    )
            }
    )
    @DeleteMapping(path = "/{id}")
    void deleteUsuarioById(@PathVariable("id") Long id);

}
