package com.proyect.cineclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "butacas")
public class Butaca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La fila es obligatoria.")
    @Size(min = 1, max = 2, message = "La fila debe ser una letra o un código corto (ej. A, B, Z1).")
    private String fila;

    @NotNull(message = "El número de butaca es obligatorio.")
    @Min(value = 1, message = "El número de butaca debe ser al menos 1.")
    private Integer numero;

    @NotBlank(message = "La etiqueta es obligatoria (ej. 'Estándar', 'VIP', 'Preferencial').")
    @Size(max = 50, message = "La etiqueta no puede exceder los 50 caracteres.")
    private String etiqueta;

    @NotNull(message = "El ID de la sala es obligatorio para asignar la butaca.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id")
    private Sala sala;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

}
