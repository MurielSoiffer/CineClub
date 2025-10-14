package com.proyect.cineclub.dto;

import com.proyect.cineclub.entity.Funcion;

import java.time.LocalDateTime;

public class FuncionDto {
    private Long id;
    private String pelicula;
    private String sala;
    private LocalDateTime inicio;
    private LocalDateTime finalizacion;
    private Boolean activa;
    private Long precio;

    public FuncionDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPelicula() {
        return pelicula;
    }

    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFinalizacion() {
        return finalizacion;
    }

    public void setFinalizacion(LocalDateTime finalizacion) {
        this.finalizacion = finalizacion;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public static FuncionDto fromFuncion(Funcion funcion){
        FuncionDto dto = new FuncionDto();
        dto.setId(funcion.getId());
        dto.setPelicula(funcion.getPelicula().getTitulo());
        dto.setSala(funcion.getSala().getNombre());
        dto.setInicio(funcion.getInicio());
        dto.setFinalizacion(funcion.getFinalizacion());
        dto.setActiva(funcion.getActiva());
        dto.setPrecio(funcion.getPrecio());
        return dto;
    }
}
