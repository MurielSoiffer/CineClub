package com.proyect.cineclub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ButacaOcuadaException extends RuntimeException{
    public ButacaOcuadaException(String etiqueta){
        super("Butaca " + etiqueta + " no disponible");
    }
}
