package com.proyect.cineclub.controller;

import com.proyect.cineclub.exception.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Horario superpuesto en funciones
    @ExceptionHandler(HorarioSuperpuestoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleHorarioSuperpuestoException(HorarioSuperpuestoException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
    //Recurso no encontrado (esto lo uso en muchos services)
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(RecursoNoEncontradoException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    //El ticket que se quiere crear esta muy cerca del horario de la funcion
    @ExceptionHandler(HoldTooCloseToStartException.class)
    public ResponseEntity<Object> handleHoldTooCloseToStartException(HoldTooCloseToStartException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    //Cuanto falla la validacion del @RequestBody en el controller (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("message", "Los datos enviados no son válidos.");
        body.put("details", errors);
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    //Cuando falla la validación del objeto al intentar guardarlo (.save()) en el Service
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {

        // 1. Recolectar todos los errores de validación
        Map<String, String> errors = new HashMap<>();

        // Iteramos sobre el conjunto de violaciones de restricciones
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();

            if (fieldName.contains(".")) {
                fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            }

            errors.put(fieldName, errorMessage);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("message", "Una o más restricciones de la entidad fueron violadas al intentar guardar.");
        body.put("details", errors);
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    //Cuanto intentan tocar un ticket que no es suyo
    @ExceptionHandler(TicketNoPermitidoException.class)
    public ResponseEntity<Object> handleTicketNoPermitidoException(TicketNoPermitidoException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
    //Cuanto la butaca no esta disponible para el ticket
    @ExceptionHandler(ButacaOcuadaException.class)
    public ResponseEntity<Object> handleButacaOcuadaException(ButacaOcuadaException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
    //Cuanto la butaca no esta disponible para el ticket
    @ExceptionHandler(TicketExpiradoException.class)
    public ResponseEntity<Object> handleTicketExpiradoException(TicketExpiradoException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put("error", "Unprocessable Entity");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}