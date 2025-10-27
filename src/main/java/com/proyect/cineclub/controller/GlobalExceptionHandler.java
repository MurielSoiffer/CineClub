package com.proyect.cineclub.controller;

import com.proyect.cineclub.exception.HorarioSuperpuestoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HorarioSuperpuestoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleHorarioSuperpuestoException(HorarioSuperpuestoException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", ex.getMessage());
        body.put("timestamp", java.time.LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}