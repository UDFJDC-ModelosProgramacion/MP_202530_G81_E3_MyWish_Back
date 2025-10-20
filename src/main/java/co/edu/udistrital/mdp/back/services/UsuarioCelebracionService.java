package co.edu.udistrital.mdp.back.services;



import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.CelebracionRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UsuarioCelebracionService {

    private static final String CELEBRACION_NO_EXISTE = "Celebración no encontrada con id ";
    private static final String USUARIO_NO_EXISTE = "Usuario no encontrado con id ";

    @Autowired
    private CelebracionRepository celebracionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /** Agregar un invitado a una celebración */
    @Transactional
    public CelebracionEntity addInvitado(Long celebracionId, Long usuarioId) {

        log.info("Inicia proceso de agregar invitado a la celebración");

        CelebracionEntity celebracion = celebracionRepository.findById(celebracionId)
                .orElseThrow(() -> new EntityNotFoundException(CELEBRACION_NO_EXISTE + celebracionId));

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(USUARIO_NO_EXISTE + usuarioId));

        // Regla 1: el invitado no puede ser el organizador
        if (usuario.getId().equals(celebracion.getOrganizador().getId())) {
            throw new IllegalArgumentException(
                    "El invitado no puede ser el mismo usuario que el organizador de la celebración.");
        }

        // Regla 2: no se pueden duplicar invitados
        boolean yaInvitado = celebracion.getInvitados().stream()
                .anyMatch(u -> u.getId().equals(usuarioId));
        if (yaInvitado) {
            throw new IllegalArgumentException("El usuario ya está invitado a esta celebración.");
        }

        celebracion.getInvitados().add(usuario);

        log.info("Termina proceso de agregar invitado a la celebración");

        return celebracionRepository.save(celebracion);
    }

    /** Remover un invitado de una celebración */
    @Transactional
    public CelebracionEntity removeInvitado(Long celebracionId, Long usuarioId) {

        log.info("Inicia proceso de remover invitado de la celebración");

        CelebracionEntity celebracion = celebracionRepository.findById(celebracionId)
                .orElseThrow(() -> new EntityNotFoundException(CELEBRACION_NO_EXISTE + celebracionId));

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new EntityNotFoundException(USUARIO_NO_EXISTE + usuarioId);
        }

        celebracion.getInvitados().removeIf(u -> u.getId().equals(usuarioId));

        log.info("Termina proceso de remover invitado de la celebración");

        return celebracionRepository.save(celebracion);
    }

    /** Consultar si un usuario está invitado a una celebración */
    public boolean isUsuarioInvitado(Long celebracionId, Long usuarioId) {

        CelebracionEntity celebracion = celebracionRepository.findById(celebracionId)
                .orElseThrow(() -> new EntityNotFoundException(CELEBRACION_NO_EXISTE + celebracionId));

        return celebracion.getInvitados().stream()
                .anyMatch(u -> u.getId().equals(usuarioId));
    }
}
