package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.entities.MensajeInvitacionEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.CelebracionRepository;
import co.edu.udistrital.mdp.back.repositories.MensajeInvitacionRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException; // ✅ Excepción estándar JPA
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CelebracionService {

    @Autowired
    private CelebracionRepository celebracionRepository;

    @Autowired
    private MensajeInvitacionRepository mensajeInvitacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // =====================================================
    // CREATE
    // =====================================================
    @Transactional
    public CelebracionEntity createCelebracion(CelebracionEntity celebracionEntity, Long organizadorId) {

        log.info("Inicia proceso de creación de la celebración");

        // Regla 1: No se puede crear una celebración sin nombre
        if (celebracionEntity.getNombre() == null || celebracionEntity.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("No se puede crear una celebración sin nombre.");
        }

        // Regla 2: La fecha de la celebración no puede ser anterior a la fecha actual
        if (celebracionEntity.getFecha() == null ||
                celebracionEntity.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de la celebración no puede ser anterior a la fecha actual.");
        }

        // Asignar organizador
        UsuarioEntity organizador = usuarioRepository.findById(organizadorId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Usuario organizador no encontrado con id " + organizadorId));

        celebracionEntity.setOrganizador(organizador);

        log.info("Termina proceso de creación de la celebración");
        return celebracionRepository.save(celebracionEntity);
    }

    // =====================================================
    // UPDATE
    // =====================================================
    @Transactional
    public CelebracionEntity updateCelebracion(Long celebracionId, CelebracionEntity celebracionEntity) {

        log.info("Inicia proceso de actualización de la celebración con id: {}", celebracionId);

        Optional<CelebracionEntity> celebracionOpt = celebracionRepository.findById(celebracionId);
        if (celebracionOpt.isEmpty()) {
            throw new EntityNotFoundException("La celebración con id " + celebracionId + " no existe.");
        }

        CelebracionEntity existente = celebracionOpt.get();

        // Regla 3: No se puede cambiar el organizador una vez creada la celebración
        if (celebracionEntity.getOrganizador() != null &&
                !celebracionEntity.getOrganizador().equals(existente.getOrganizador())) {
            throw new IllegalStateException("No se puede cambiar el organizador de una celebración existente.");
        }

        // ✅ Regla 5: Se puede actualizar la lista de regalos asociada (OneToOne)
        if (celebracionEntity.getListaRegalos() != null) {
            existente.setListaRegalos(celebracionEntity.getListaRegalos());
        }

        // Actualización de datos válidos
        existente.setNombre(celebracionEntity.getNombre());
        existente.setDescripcion(celebracionEntity.getDescripcion());
        existente.setLugar(celebracionEntity.getLugar());
        existente.setColor(celebracionEntity.getColor());
        existente.setFecha(celebracionEntity.getFecha());
        existente.setCantidadInvitados(celebracionEntity.getCantidadInvitados());

        log.info("Termina proceso de actualización de la celebración con id: {}", celebracionId);
        return celebracionRepository.save(existente);
    }

    // =====================================================
    // DELETE
    // =====================================================
    @Transactional
    public void deleteCelebracion(Long celebracionId) {

        log.info("Inicia proceso de eliminación de la celebración con id: {}", celebracionId);

        Optional<CelebracionEntity> celebracionOpt = celebracionRepository.findById(celebracionId);
        if (celebracionOpt.isEmpty()) {
            throw new EntityNotFoundException("La celebración con id " + celebracionId + " no existe.");
        }

        CelebracionEntity celebracion = celebracionOpt.get();

        // Regla 4: Al eliminar la celebración, inactivar los mensajes de invitación
        List<MensajeInvitacionEntity> mensajes = mensajeInvitacionRepository.findByCelebracionId(celebracionId);
        for (MensajeInvitacionEntity mensaje : mensajes) {
            mensajeInvitacionRepository.delete(mensaje);

        }

        celebracionRepository.delete(celebracion);

        log.info("Termina proceso de eliminación de la celebración con id: {}", celebracionId);
    }

    // =====================================================
    // GET
    // =====================================================
    @Transactional(readOnly = true)
    public List<CelebracionEntity> getAllCelebraciones() {
        log.info("Inicia proceso de consulta de todas las celebraciones");
        return celebracionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CelebracionEntity getCelebracionById(Long celebracionId) {
        log.info("Inicia proceso de consulta de la celebración con id: {}", celebracionId);
        return celebracionRepository.findById(celebracionId)
                .orElseThrow(
                        () -> new EntityNotFoundException("La celebración con id " + celebracionId + " no existe."));
    }
}