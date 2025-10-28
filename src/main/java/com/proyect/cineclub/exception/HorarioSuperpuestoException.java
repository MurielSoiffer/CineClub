package com.proyect.cineclub.exception;

public class HorarioSuperpuestoException extends RuntimeException {

    public HorarioSuperpuestoException(Long id) {
        super("La Sala " + id + " ya tiene una función programada que se superpone con el horario.");
    }
}
