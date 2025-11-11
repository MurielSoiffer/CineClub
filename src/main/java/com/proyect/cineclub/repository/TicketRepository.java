package com.proyect.cineclub.repository;

import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.entity.Funcion;
import com.proyect.cineclub.entity.Ticket;
import com.proyect.cineclub.entity.Usuario;
import com.proyect.cineclub.security.EstadoTicket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    Optional<Ticket> findFirstByFuncionAndButacaAndEstadoIn(
            Funcion funcion,
            Butaca butaca,
            List<EstadoTicket> estadosNoLiberados
    );
    Optional<Ticket> findByFuncionAndButaca(Funcion funcion, Butaca butaca);
    List<Ticket> findByEstadoAndHoldExpirationTimeBefore(EstadoTicket estado, Instant holdExpirationTime);
    List<Ticket> findByUsuario(Usuario usuario);

    @Modifying
    @Query("DELETE FROM Ticket t WHERE t.estado IN :estados")
    int deleteByEstadoInBulk(@Param("estados") List<EstadoTicket> estados);
}
