package com.proyect.cineclub.dto;

import com.proyect.cineclub.entity.Ticket;
import com.proyect.cineclub.service.TicketService;

public class TicketDto {
    private Long id;
    private String estado;
    private String butaca;
    private FuncionDto funcion;

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

    public String getButaca() {
        return butaca;
    }

    public void setButaca(String butaca) {
        this.butaca = butaca;
    }

    public FuncionDto getFuncion() {
        return funcion;
    }

    public void setFuncion(FuncionDto funcion) {
        this.funcion = funcion;
    }

    public static TicketDto fromTicket(Ticket ticket){
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setEstado(ticket.getEstado().name());
        dto.setButaca(ticket.getButaca().getEtiqueta());
        dto.setFuncion(FuncionDto.fromFuncion(ticket.getFuncion()));
        return dto;
    }
}
