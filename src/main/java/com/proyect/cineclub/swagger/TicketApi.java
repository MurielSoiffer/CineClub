package com.proyect.cineclub.swagger;

import com.proyect.cineclub.dto.TicketDto;
import com.proyect.cineclub.entity.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ticket", description = "Gestión de las reservas y compras de tickets.")
@RequestMapping("/api/tickets")
public interface TicketApi {

    /**
     * Obtiene todos los tickets asociados al usuario autenticado.
     */
    @Operation(
            summary = "Obtener mis tickets",
            description = "Retorna una lista de todos los tickets (reservados o confirmados) pertenecientes al usuario actualmente autenticado.",
            responses = {
                    @ApiResponse(
                            description = "Success: Lista de tickets del usuario.",
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketDto.class)))
                    ),
                    @ApiResponse(
                            description = "Not Found: El usuario no existe o no tiene tickets.",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\", \"path\": \"/api/ticket\"}"
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
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/ticket\"}"
                                            )
                                    })
                    )
            }
    )
    @GetMapping(path = "/me")
    ResponseEntity<List<TicketDto>> getMyTickets(Authentication authentication);

    /**
     * Cancela un ticket (reserva o compra) por su ID.
     */
    @Operation(
            summary = "Cancelar ticket por ID",
            description = "Cancela un ticket existente. Solo el usuario dueño del ticket puede realizar la cancelación.",
            responses = {
                    @ApiResponse(
                            description = "Success: Ticket cancelado. Retorna el ticket actualizado.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TicketDto.class))
                    ),
                    @ApiResponse(
                            description = "Forbidden: El ticket no pertenece al usuario autenticado o no puede ser cancelado.",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\", \"path\": \"/api/ticket\"}"
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
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/ticket\"}"
                                            )
                                    })
                    ),
                    @ApiResponse(
                            description = "Not Found: El ticket con el ID especificado no existe.",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping(path = "/{id}")
    ResponseEntity<TicketDto> cancelById(@Parameter(description = "ID del ticket a cancelar.") @PathVariable("id") long id, Authentication authentication);

    /**
     * Confirma una reserva temporal (HOLD), convirtiéndola en una compra final.
     */
    @Operation(
            summary = "Confirmar reserva (Hold)",
            description = "Confirma una reserva temporal (estado HOLD) por su ID, convirtiéndola en un ticket confirmado (compra). Solo el dueño puede confirmarlo.",
            responses = {
                    @ApiResponse(
                            description = "Success: Ticket confirmado (comprado). Retorna el ticket actualizado.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TicketDto.class))
                    ),
                    @ApiResponse(
                            description = "Forbidden: El ticket no pertenece al usuario autenticado o ya está confirmado/cancelado.",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 403, \"error\": \"Forbidden\", \"path\": \"/api/ticket\"}"
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
                                                    value = "{\"timestamp\": \"2025-11-10T15:00:00Z\", \"status\": 401, \"error\": \"Unauthorized\", \"path\": \"/api/ticket\"}"
                                            )
                                    })
                    ),
                    @ApiResponse(
                            description = "Not Found: El ticket con el ID especificado no existe.",
                            responseCode = "404"
                    )
            }
    )
    @PostMapping(path = "{id}/confirm")
    ResponseEntity<TicketDto> confirmById(@Parameter(description = "ID del ticket Hold a confirmar.") @PathVariable("id") long id, Authentication authentication);
}
