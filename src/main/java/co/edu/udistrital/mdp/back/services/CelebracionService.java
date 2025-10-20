package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.entities.MensajeInvitacionEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.CelebracionRepository;
import co.edu.udistrital.mdp.back.repositories.MensajeInvitacionRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException; 
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
@Slf4j
@Service
public class CelebracionService {

    private static final String MSG_CELEBRACION_ID = "La celebración con id ";
    private static final String MSG_NO_EXISTE = " no existe.";

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

        if (celebracionEntity.getNombre() == null || celebracionEntity.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("No se puede crear una celebración sin nombre.");
        }

        if (celebracionEntity.getFecha() == null || celebracionEntity.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de la celebración no puede ser anterior a la fecha actual.");
        }

        UsuarioEntity organizador = usuarioRepository.findById(organizadorId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario organizador no encontrado con id " + organizadorId));

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

        CelebracionEntity existente = celebracionRepository.findById(celebracionId)
                .orElseThrow(() -> new EntityNotFoundException(MSG_CELEBRACION_ID + celebracionId + MSG_NO_EXISTE));

        if (celebracionEntity.getOrganizador() != null &&
                !celebracionEntity.getOrganizador().equals(existente.getOrganizador())) {
            throw new IllegalStateException("No se puede cambiar el organizador de una celebración existente.");
        }

        if (celebracionEntity.getListaRegalos() != null) {
            existente.setListaRegalos(celebracionEntity.getListaRegalos());
        }

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

        CelebracionEntity celebracion = celebracionRepository.findById(celebracionId)
                .orElseThrow(() -> new EntityNotFoundException(MSG_CELEBRACION_ID + celebracionId + MSG_NO_EXISTE));

        List<MensajeInvitacionEntity> mensajes = mensajeInvitacionRepository.findByCelebracion_Id(celebracionId);
        mensajes.forEach(mensajeInvitacionRepository::delete);

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
                .orElseThrow(() -> new EntityNotFoundException(MSG_CELEBRACION_ID + celebracionId + MSG_NO_EXISTE));
    }
}
