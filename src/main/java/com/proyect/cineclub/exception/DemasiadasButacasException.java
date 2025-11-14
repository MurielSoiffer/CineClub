package com.proyect.cineclub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DemasiadasButacasException extends RuntimeException{
    public DemasiadasButacasException(int max){
        super("Maximo de butacas permitido " + max);
    }
}
