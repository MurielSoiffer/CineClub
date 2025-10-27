package com.proyect.cineclub.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FuncionFiltroDto {
    private String pelicula;
    private String Sala;
    private LocalDateTime fechaYhoraMinima;
    private Long precioMinimo;
    private Long precioMaximo;

    public String getPelicula() {
        return pelicula;
    }

    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }

    public String getSala() {
        return Sala;
    }

    public void setSala(String sala) {
        Sala = sala;
    }

    public LocalDateTime getFechaYhoraMinima() {
        return fechaYhoraMinima;
    }

    public void setFechaYhoraMinima(LocalDateTime fechaYhoraMinima) {
        this.fechaYhoraMinima = fechaYhoraMinima;
    }

    public Long getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(Long precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public Long getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(Long precioMaximo) {
        this.precioMaximo = precioMaximo;
    }
}
