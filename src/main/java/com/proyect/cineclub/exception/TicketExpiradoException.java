package com.proyect.cineclub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class TicketExpiradoException extends RuntimeException{
    public TicketExpiradoException(){
        super("Estado invalido para la operacion solicitada");
    }
}
