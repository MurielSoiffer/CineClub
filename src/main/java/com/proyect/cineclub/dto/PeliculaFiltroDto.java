package com.proyect.cineclub.dto;

import java.time.LocalDate;

public class PeliculaFiltroDto {
    private String titulo;
    private String duracionMaxima;
    private String duracionMinima;
    private LocalDate fechaEstrenoDesde;
    private Integer edadMinima;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDuracionMaxima() {
        return duracionMaxima;
    }

    public void setDuracionMaxima(String duracionMaxima) {
        this.duracionMaxima = duracionMaxima;
    }

    public String getDuracionMinima() {
        return duracionMinima;
    }

    public void setDuracionMinima(String duracionMinima) {
        this.duracionMinima = duracionMinima;
    }

    public LocalDate getFechaEstrenoDesde() {
        return fechaEstrenoDesde;
    }

    public void setFechaEstrenoDesde(LocalDate fechaEstrenoDesde) {
        this.fechaEstrenoDesde = fechaEstrenoDesde;
    }

    public Integer getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(Integer edadMinima) {
        this.edadMinima = edadMinima;
    }
}
