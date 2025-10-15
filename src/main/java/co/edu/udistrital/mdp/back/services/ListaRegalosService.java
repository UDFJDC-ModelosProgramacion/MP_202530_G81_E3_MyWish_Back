package co.edu.udistrital.mdp.back.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ListaRegalosService {

    @Autowired
    private ListaRegalosRepository listaRegalosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Crear una nueva lista de regalos
     */
    @Transactional
    public ListaRegalosEntity createListaRegalos(ListaRegalosEntity lista)
            throws IllegalArgumentException, EntityNotFoundException {
        log.info("Inicia proceso de creación de lista de regalos");

        UsuarioEntity creador = usuarioRepository.findById(lista.getCreador().getId())
                .orElseThrow(() -> new EntityNotFoundException("El usuario creador no existe."));
        lista.setCreador(creador);

        // Validación: nombre obligatorio
        if (lista.getNombre() == null || lista.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la lista de regalos es obligatorio.");
        }

        // Validación: fecha no anterior a hoy
        if (lista.getFecha() == null || lista.getFecha().before(new Date())) {
            throw new IllegalArgumentException(
                    "La fecha de la lista de regalos no puede ser anterior a la fecha actual.");
        }

        // Validación: creador debe existir
        if (lista.getCreador() == null || lista.getCreador().getId() == null) {
            throw new IllegalArgumentException("El creador de la lista de regalos es obligatorio.");
        }

        Optional<UsuarioEntity> creadorOpt = usuarioRepository.findById(lista.getCreador().getId());
        if (creadorOpt.isEmpty()) {
            throw new EntityNotFoundException("El usuario creador no existe.");
        }

        lista.setCreador(creadorOpt.get());

        log.info("Termina proceso de creación de lista de regalos");
        return listaRegalosRepository.save(lista);
    }

    /**
     * Actualizar lista de regalos
     */
    @Transactional
    public ListaRegalosEntity updateListaRegalos(Long id, ListaRegalosEntity listaActualizada)
            throws EntityNotFoundException, IllegalArgumentException {
        log.info("Inicia proceso de actualización de lista de regalos");

        ListaRegalosEntity listaExistente = listaRegalosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lista de regalos no encontrada con id " + id));

        // Validación: no se puede cambiar el creador
        if (listaActualizada.getCreador() != null
                && !listaActualizada.getCreador().getId().equals(listaExistente.getCreador().getId())) {
            throw new IllegalArgumentException("No se puede cambiar el creador de la lista de regalos.");
        }

        // Validación: fecha no puede ser anterior a hoy
        if (listaActualizada.getFecha() != null && listaActualizada.getFecha().before(new Date())) {
            throw new IllegalArgumentException(
                    "La fecha de la lista de regalos no puede ser anterior a la fecha actual.");
        }

        // Actualizar campos permitidos
        if (listaActualizada.getNombre() != null)
            listaExistente.setNombre(listaActualizada.getNombre());
        if (listaActualizada.getDescripcion() != null)
            listaExistente.setDescripcion(listaActualizada.getDescripcion());
        if (listaActualizada.getColor() != null)
            listaExistente.setColor(listaActualizada.getColor());
        if (listaActualizada.getFecha() != null)
            listaExistente.setFecha(listaActualizada.getFecha());
        if (listaActualizada.getCelebracion() != null) {
            // Validación: una lista solo puede estar asociada a una celebración
            if (listaExistente.getCelebracion() != null
                    && !listaExistente.getCelebracion().getId().equals(listaActualizada.getCelebracion().getId())) {
                throw new IllegalArgumentException(
                        "La lista de regalos solo puede estar asociada a una única celebración.");
            }
            listaExistente.setCelebracion(listaActualizada.getCelebracion());
        }

        log.info("Termina proceso de actualización de lista de regalos");
        return listaRegalosRepository.save(listaExistente);
    }

    /**
     * Eliminar lista de regalos
     */
    @Transactional
    public void deleteListaRegalos(Long id) throws EntityNotFoundException {
        log.info("Inicia proceso de eliminación de lista de regalos");

        ListaRegalosEntity listaExistente = listaRegalosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lista de regalos no encontrada con id " + id));

        // Al eliminar la lista, los regalos asociados se eliminarán automáticamente
        listaRegalosRepository.delete(listaExistente);

        log.info("Termina proceso de eliminación de lista de regalos");
    }

    /**
     * Consultar todas las listas
     */
    public List<ListaRegalosEntity> getAllListas() {
        return listaRegalosRepository.findAll();
    }

    /**
     * Consultar lista por id
     */
    public ListaRegalosEntity getListaById(Long id) throws EntityNotFoundException {
        return listaRegalosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lista de regalos no encontrada con id " + id));
    }

    /**
     * Consultar listas por creador
     */
    public List<ListaRegalosEntity> getListasByCreador(Long creadorId) {
        return listaRegalosRepository.findByCreadorId(creadorId);
    }

}
