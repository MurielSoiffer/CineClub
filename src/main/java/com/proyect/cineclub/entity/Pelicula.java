package com.proyect.cineclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "peliculas")
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio.")
    @Size(min = 2, max = 200, message = "El título debe tener entre 2 y 200 caracteres.")
    private String titulo;

    @NotBlank(message = "La sinopsis es obligatoria.")
    @Size(min = 10, max = 500, message = "La sinopsis debe tener entre 10 y 500 caracteres.")
    private String sinopsis;

    @NotNull(message = "La duración es obligatoria.")
    @Min(value = 1, message = "La duración debe ser al menos 1 minuto.")
    private Long duracion; // Generalmente en minutos

    @NotNull(message = "La fecha de estreno es obligatoria.")
    @PastOrPresent(message = "La fecha de estreno no puede ser en el futuro.")
    private LocalDate fechaEstreno;

    @Min(value = 0, message = "La edad mínima no puede ser negativa.")
    private int edadMinima;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Long getDuracion() {
        return duracion;
    }

    public void setDuracion(Long duracion) {
        this.duracion = duracion;
    }

    public LocalDate getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMin) {
        this.edadMinima = edadMin;
    }
}
