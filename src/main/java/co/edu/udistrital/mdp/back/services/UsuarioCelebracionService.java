package co.edu.udistrital.mdp.back.services;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.CelebracionRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioCelebracionService {

    @Autowired
    private CelebracionRepository celebracionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Agregar un invitado a una celebración
     */
    @Transactional
    public CelebracionEntity addInvitado(Long celebracionId, Long usuarioId) throws EntityNotFoundException, IllegalArgumentException {
        log.info("Inicia proceso de agregar invitado a la celebración");

        CelebracionEntity celebracion = celebracionRepository.findById(celebracionId)
                .orElseThrow(() -> new EntityNotFoundException("Celebración no encontrada con id " + celebracionId));

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id " + usuarioId));

        // Regla 1: el invitado no puede ser el mismo que el organizador
        if (usuario.getId().equals(celebracion.getOrganizador().getId())) {
            throw new IllegalArgumentException("El invitado no puede ser el mismo usuario que el organizador de la celebración.");
        }

        // Regla 2: no se pueden duplicar invitados
        for (UsuarioEntity u : celebracion.getInvitados()) {
            if (u.getId().equals(usuarioId)) {
                throw new IllegalArgumentException("El usuario ya está invitado a esta celebración.");
            }
        }

        celebracion.getInvitados().add(usuario);
        log.info("Termina proceso de agregar invitado a la celebración");

        return celebracionRepository.save(celebracion);
    }

    /**
     * Remover un invitado de una celebración
     */
    @Transactional
    public CelebracionEntity removeInvitado(Long celebracionId, Long usuarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de remover invitado de la celebración");

        CelebracionEntity celebracion = celebracionRepository.findById(celebracionId)
                .orElseThrow(() -> new EntityNotFoundException("Celebración no encontrada con id " + celebracionId));

        // Verificar que el usuario exista
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new EntityNotFoundException("Usuario no encontrado con id " + usuarioId);
        }

        // Buscar y remover invitado
        UsuarioEntity invitadoAEliminar = null;
        for (UsuarioEntity u : celebracion.getInvitados()) {
            if (u.getId().equals(usuarioId)) {
                invitadoAEliminar = u;
                break;
            }
        }
        if (invitadoAEliminar != null) {
            celebracion.getInvitados().remove(invitadoAEliminar);
        }

        log.info("Termina proceso de remover invitado de la celebración");
        return celebracionRepository.save(celebracion);
    }

    /**
     * Consultar si un usuario está invitado a una celebración
     */
    public boolean isUsuarioInvitado(Long celebracionId, Long usuarioId) throws EntityNotFoundException {
        CelebracionEntity celebracion = celebracionRepository.findById(celebracionId)
                .orElseThrow(() -> new EntityNotFoundException("Celebración no encontrada con id " + celebracionId));

        for (UsuarioEntity u : celebracion.getInvitados()) {
            if (u.getId().equals(usuarioId)) {
                return true;
            }
        }
        return false;
    }
}
    