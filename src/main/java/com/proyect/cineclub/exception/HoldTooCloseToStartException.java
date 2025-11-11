package com.proyect.cineclub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HoldTooCloseToStartException extends RuntimeException{
    public HoldTooCloseToStartException(LocalDateTime holdNotAllowedBefore) {
        super("No se permiten holds tan cerca del inicio de la función. La hora límite es " + holdNotAllowedBefore + ".");
    }
}
