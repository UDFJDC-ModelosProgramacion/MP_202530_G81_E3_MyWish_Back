package co.edu.udistrital.mdp.back.services;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.CelebracionRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
 
@Slf4j
@Service
public class CelebracionUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CelebracionRepository celebracionRepository;

    public List<CelebracionEntity> obtenerCelebracionesDeUsuario(Long usuarioId) {
        log.info("Obteniendo celebraciones del usuario {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id " + usuarioId));

        return usuario.getCelebracionesInvitado();
    }

    public boolean estaInvitadoEnCelebracion(Long usuarioId, Long celebracionId) {
        log.info("Verificando invitación del usuario {} en celebración {}", usuarioId, celebracionId);

        CelebracionEntity celebracion = celebracionRepository.findById(celebracionId)
                .orElseThrow(() -> new EntityNotFoundException("Celebración no encontrada con id " + celebracionId));

        return celebracion.getInvitados().stream()
                .anyMatch(u -> u.getId().equals(usuarioId));
    }

    @Transactional
    public void removerInvitacion(Long usuarioId, Long celebracionId) {
        log.info("Removiendo invitación de usuario {} en celebración {}", usuarioId, celebracionId);

        CelebracionEntity celebracion = celebracionRepository.findById(celebracionId)
                .orElseThrow(() -> new EntityNotFoundException("Celebración no encontrada con id " + celebracionId));

        boolean eliminado = celebracion.getInvitados().removeIf(u -> u.getId().equals(usuarioId));

        if (!eliminado) {
            throw new IllegalArgumentException("Usuario no invitado en esta celebración.");
        }

        celebracionRepository.save(celebracion);
    }
}