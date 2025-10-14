package com.proyect.cineclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;

@Entity
@Table(name = "tickets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"butaca_fk", "funcion_fk", "estado"})
})
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ciclo de vida: HOLD, CONFIRMADO, CANCELADO, EXPIRADO
    @NotNull
    @Pattern(regexp = "HOLD|CONFIRMADO|CANCELADO|EXPIRADO",
            message = "El estado debe ser uno de: HOLD, CONFIRMADO, CANCELADO, o EXPIRADO.")
    private String estado;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "butaca_fk")
    private Butaca butaca;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "funcion_fk")
    private Funcion screening;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_fk")
    private Usuario usuario;

    private Instant holdExpirationTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Butaca getButaca() {
        return butaca;
    }

    public void setButaca(Butaca butaca) {
        this.butaca = butaca;
    }

    public Funcion getScreening() {
        return screening;
    }

    public void setScreening(Funcion screening) {
        this.screening = screening;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Instant getHoldExpirationTime() {
        return holdExpirationTime;
    }

    public void setHoldExpirationTime(Instant holdExpirationTime) {
        this.holdExpirationTime = holdExpirationTime;
    }
}