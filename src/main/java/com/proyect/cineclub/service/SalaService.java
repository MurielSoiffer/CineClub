package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Butaca;
import com.proyect.cineclub.entity.Sala;
import com.proyect.cineclub.repository.ButacaRepository;
import com.proyect.cineclub.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SalaService {
    @Autowired
    SalaRepository salaRepository;

    @Autowired
    ButacaService butacaService;

    private static final int BUTACAS_POR_FILA = 10;

    @Transactional
    public Sala save(Sala sala){
        Sala nuevaSala = salaRepository.save(sala);
        List<Butaca> butacasGeneradas = generarButacas(nuevaSala);
        nuevaSala.setButacas(butacasGeneradas);
        return nuevaSala;
    }

    @Transactional
    public Sala updateById(Sala request, Long id) {
        Optional<Sala> salaExistente = salaRepository.findById(id);
        if(salaExistente.isEmpty()) {
            // .orElseThrow(() -> new RuntimeException("Pelicula no encontrada"));
        }
        int capacidadAnterior = salaExistente.get().getCapacidad();
        int nuevaCapacidad = request.getCapacidad();

        salaExistente.get().setNombre(request.getNombre());
        salaExistente.get().setCapacidad(request.getCapacidad());

        if (nuevaCapacidad != capacidadAnterior) {
            if (nuevaCapacidad < capacidadAnterior) {
                eliminarExcesoDeButacas(salaExistente.get(), nuevaCapacidad);
            } else {
                agregarNuevasButacas(salaExistente.get(), nuevaCapacidad, capacidadAnterior);
            }
        }

        return salaRepository.save(salaExistente.get());
    }

    public Page<Sala> getAll(Pageable pageable){return salaRepository.findAll(pageable);}

    public Optional<Sala> getById(Long id){return salaRepository.findById(id);}

    public void deleteById(Long id){salaRepository.deleteById(id);}

    private List<Butaca> generarButacas(Sala sala) {
        int capacidad = sala.getCapacidad();
        List<Butaca> butacas = new ArrayList<>();

        // Calcular filas y el resto de asientos
        int numFilas = (int) Math.ceil((double) capacidad / BUTACAS_POR_FILA);
        int butacasRestantes = capacidad;

        for (int i = 0; i < numFilas; i++) {
            String filaLetra = String.valueOf((char) ('A' + i));
            int asientosEnFila = Math.min(BUTACAS_POR_FILA, butacasRestantes);

            for (int j = 1; j <= asientosEnFila; j++) {
                Butaca butaca = new Butaca();
                butaca.setSala(sala);
                butaca.setFila(filaLetra);
                butaca.setNumero(j);
                butaca.setEtiqueta(filaLetra + "-" + j);

                butacas.add(butaca);
                butacasRestantes--;
            }
        }
        butacaService.saveAll(butacas);

        return butacas;
    }

    private void eliminarExcesoDeButacas(Sala sala, int nuevaCapacidad) {
        List<Butaca> butacasActuales = sala.getButacas();
        int butacasParaMantener = nuevaCapacidad;

        if (butacasActuales.size() > butacasParaMantener) {
            List<Butaca> butacasAEliminar = new ArrayList<>(
                    butacasActuales.subList(butacasParaMantener, butacasActuales.size())
            );
            for (Butaca butaca : butacasAEliminar) {
                sala.getButacas().remove(butaca);
            }
        }
    }
    private void agregarNuevasButacas(Sala sala, int nuevaCapacidad, int capacidadAnterior) {
        int butacasAñadir = nuevaCapacidad - capacidadAnterior;
        List<Butaca> nuevasButacas = new ArrayList<>();

        int totalButacasActual = sala.getButacas().size();

        for (int i = 0; i < butacasAñadir; i++) {
            int indiceButaca = totalButacasActual + i + 1;
            int filaIndex = (indiceButaca - 1) / BUTACAS_POR_FILA;
            int numeroEnFila = (indiceButaca - 1) % BUTACAS_POR_FILA + 1;

            String filaLetra = String.valueOf((char) ('A' + filaIndex));

            Butaca butaca = new Butaca();
            butaca.setSala(sala);
            butaca.setFila(filaLetra);
            butaca.setNumero(numeroEnFila);
            butaca.setEtiqueta(filaLetra + "-" + numeroEnFila);

            nuevasButacas.add(butaca);
        }
        sala.getButacas().addAll(nuevasButacas);
    }
}
