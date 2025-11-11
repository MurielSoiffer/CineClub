package com.proyect.cineclub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoException extends RuntimeException{
    public RecursoNoEncontradoException(String recurso, Long id) {
        super( recurso + " no encontrada con ID: " + id);
    }
}
