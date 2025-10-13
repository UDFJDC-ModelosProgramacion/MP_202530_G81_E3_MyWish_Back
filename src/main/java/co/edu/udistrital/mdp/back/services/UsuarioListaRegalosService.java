package co.edu.udistrital.mdp.back.services;



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
public class UsuarioListaRegalosService {

    @Autowired
    private ListaRegalosRepository listaRegalosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Agregar un invitado a una lista de regalos
     */
    @Transactional
    public ListaRegalosEntity addInvitado(Long listaId, Long usuarioId) throws EntityNotFoundException, IllegalArgumentException {
        log.info("Inicia proceso de agregar invitado a la lista de regalos");

        ListaRegalosEntity lista = listaRegalosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException("Lista de regalos no encontrada con id " + listaId));

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id " + usuarioId));

        // Regla 1: el invitado no puede ser el mismo que el creador
        if (usuario.getId().equals(lista.getCreador().getId())) {
            throw new IllegalArgumentException("El invitado no puede ser el mismo usuario que el creador de la lista.");
        }

        // Regla 2: no se pueden duplicar invitados
        for (UsuarioEntity u : lista.getInvitados()) {
            if (u.getId().equals(usuarioId)) {
                throw new IllegalArgumentException("El usuario ya está invitado a esta lista.");
            }
        }

        lista.getInvitados().add(usuario);
        log.info("Termina proceso de agregar invitado a la lista de regalos");

        return listaRegalosRepository.save(lista);
    }

    /**
     * Remover un invitado de una lista de regalos
     */
    @Transactional
    public ListaRegalosEntity removeInvitado(Long listaId, Long usuarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de remover invitado de la lista de regalos");

        ListaRegalosEntity lista = listaRegalosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException("Lista de regalos no encontrada con id " + listaId));

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id " + usuarioId));

        // Buscar y remover invitado
        UsuarioEntity invitadoAEliminar = null;
        for (UsuarioEntity u : lista.getInvitados()) {
            if (u.getId().equals(usuarioId)) {
                invitadoAEliminar = u;
                break;
            }
        }
        if (invitadoAEliminar != null) {
            lista.getInvitados().remove(invitadoAEliminar);
        }

        log.info("Termina proceso de remover invitado de la lista de regalos");
        return listaRegalosRepository.save(lista);
    }

    /**
     * Consultar si un usuario está invitado a una lista
     */
    public boolean isUsuarioInvitado(Long listaId, Long usuarioId) throws EntityNotFoundException {
        ListaRegalosEntity lista = listaRegalosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException("Lista de regalos no encontrada con id " + listaId));

        for (UsuarioEntity u : lista.getInvitados()) {
            if (u.getId().equals(usuarioId)) {
                return true;
            }
        }
        return false;
    }
}
    