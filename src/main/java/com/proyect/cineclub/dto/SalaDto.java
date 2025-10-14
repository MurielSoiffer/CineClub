package com.proyect.cineclub.dto;

import com.proyect.cineclub.entity.Sala;

public class SalaDto {
    private Long id;
    private String nombre;
    private int capacidad;


    public SalaDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public static SalaDto fromSala(Sala sala){
        SalaDto dto = new SalaDto();
        dto.setId(sala.getId());
        dto.setNombre(sala.getNombre());
        dto.setCapacidad(sala.getCapacidad());
        return dto;
    }
}
