package com.proyect.cineclub.entity;

import com.proyect.cineclub.security.EstadoTicket;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;

@Entity
@Table(name = "tickets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"butaca_fk", "funcion_fk"}, name = "uq_funcion_butaca")
})
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ciclo de vida: HOLD, CONFIRMADO, CANCELADO, EXPIRADO
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private EstadoTicket estado;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "butaca_fk")
    private Butaca butaca;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "funcion_fk")
    private Funcion funcion;

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

    public EstadoTicket getEstado() {
        return estado;
    }

    public void setEstado(EstadoTicket estado) {
        this.estado = estado;
    }

    public Butaca getButaca() {
        return butaca;
    }

    public void setButaca(Butaca butaca) {
        this.butaca = butaca;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
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