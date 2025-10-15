package co.edu.udistrital.mdp.back.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.FiltroRegaloEntity;
import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.repositories.FiltroRegaloRepository;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FiltroRegaloService {

    @Autowired
    private FiltroRegaloRepository filtroRegaloRepository;

    @Autowired
    private ListaRegalosRepository listaRegalosRepository;

    // Crear Filtro con validaciones de negocio
    @Transactional
    public FiltroRegaloEntity crearFiltro(Long listaRegalosId, String criterio, String valor) {
        log.info("Crear filtro en lista {} con criterio '{}' y valor '{}'", listaRegalosId, criterio, valor);

        // Regla: El criterio y el valor no pueden ser nulos ni vacíos
        if (criterio == null || criterio.isEmpty() || valor == null || valor.isEmpty()) {
            throw new IllegalArgumentException("El criterio y el valor no pueden ser nulos ni vacíos.");
        }

        ListaRegalosEntity lista = listaRegalosRepository.findById(listaRegalosId)
                .orElseThrow(() -> new IllegalArgumentException("La lista de regalos no existe."));

        // Regla: No se pueden duplicar filtros con el mismo criterio y valor en la misma lista de regalos
        boolean duplicado = lista.getFiltrosRegalos().stream()
                .anyMatch(f -> f.getCriterio().equals(criterio) && f.getValor().equals(valor));
        if (duplicado) {
            throw new IllegalArgumentException("Ya existe un filtro con el mismo criterio y valor en esta lista de regalos.");
        }

        FiltroRegaloEntity filtro = new FiltroRegaloEntity();
        filtro.setCriterio(criterio);
        filtro.setValor(valor);
        filtro.setListaRegalos(lista);

        return filtroRegaloRepository.save(filtro);
    }

    // Actualizar valor del filtro existente con reglas de negocio
    @Transactional
    public FiltroRegaloEntity actualizarFiltro(Long filtroId, String nuevoValor) {
        log.info("Actualizar filtro id {} con nuevo valor '{}'", filtroId, nuevoValor);

        FiltroRegaloEntity filtro = filtroRegaloRepository.findById(filtroId)
                .orElseThrow(() -> new IllegalArgumentException("Filtro no encontrado"));

        // Regla: No se puede dejar vacío el valor
        if (nuevoValor == null || nuevoValor.isEmpty()) {
            throw new IllegalArgumentException("El valor del filtro no puede estar vacío.");
        }

        ListaRegalosEntity lista = filtro.getListaRegalos();

        // Regla: Evitar duplicados (criterio y valor) en la misma lista al actualizar
        boolean duplicado = lista.getFiltrosRegalos().stream()
                .anyMatch(f -> !f.getId().equals(filtroId) &&
                               f.getCriterio().equals(filtro.getCriterio()) &&
                               f.getValor().equals(nuevoValor));
        if (duplicado) {
            throw new IllegalArgumentException("No se puede actualizar el filtro: duplicaría criterio y valor en la misma lista.");
        }

        filtro.setValor(nuevoValor);
        return filtroRegaloRepository.save(filtro);
    }

    // Eliminar filtro si pertenece a una lista existente
    @Transactional
    public void eliminarFiltro(Long filtroId) {
        log.info("Eliminar filtro id {}", filtroId);

        FiltroRegaloEntity filtro = filtroRegaloRepository.findById(filtroId)
                .orElseThrow(() -> new IllegalArgumentException("Filtro no encontrado"));

        ListaRegalosEntity lista = filtro.getListaRegalos();
        if (lista == null) {
            throw new IllegalArgumentException("El filtro no pertenece a una lista de regalos existente.");
        }

        filtroRegaloRepository.delete(filtro);
    }
    
}
