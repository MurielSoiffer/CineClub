package com.proyect.cineclub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TicketNoPermitidoException extends RuntimeException{
    public TicketNoPermitidoException(Long id){
        super("No tiene autorizacion para modificar el ticket " + id);
    }
}
