package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.PrioridadRegaloEntity;
import co.edu.udistrital.mdp.back.repositories.PrioridadRegaloRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PrioridadRegaloService {

    @Autowired
    private PrioridadRegaloRepository prioridadRegaloRepository;

    // =====================================================
    // CREATE
    // =====================================================
    @Transactional
    public PrioridadRegaloEntity createPrioridadRegalo(PrioridadRegaloEntity prioridadRegaloEntity) {

        log.info("Inicia proceso de creación de la prioridad de regalo");

        // Regla 1: El nivel de prioridad debe estar entre 1 y 10 (donde 1 es máxima prioridad)
        if (prioridadRegaloEntity.getNivel() == null || 
            prioridadRegaloEntity.getNivel() < 1 || 
            prioridadRegaloEntity.getNivel() > 10) {
            throw new IllegalArgumentException("El nivel de prioridad debe estar entre 1 y 10.");
        }

        // Regla 2: No puede existir más de una prioridad con el mismo nombre
        if (prioridadRegaloRepository.existsByNombre(prioridadRegaloEntity.getNombre())) {
            throw new IllegalArgumentException("Ya existe una prioridad con el nombre: " + prioridadRegaloEntity.getNombre());
        }

        // Regla 3: No puede existir más de una prioridad con el mismo nivel
        List<PrioridadRegaloEntity> prioridadesMismoNivel = prioridadRegaloRepository.findByNivel(prioridadRegaloEntity.getNivel());
        if (!prioridadesMismoNivel.isEmpty()) {
            throw new IllegalArgumentException("Ya existe una prioridad con el nivel: " + prioridadRegaloEntity.getNivel());
        }

        // Regla 4: La prioridad "Esencial" debe crearse automáticamente como prioridad por defecto
        if ("Esencial".equalsIgnoreCase(prioridadRegaloEntity.getNombre())) {
            prioridadRegaloEntity.setEsPorDefecto(true);
        }

        log.info("Termina proceso de creación de la prioridad de regalo");
        return prioridadRegaloRepository.save(prioridadRegaloEntity);
    }

    // =====================================================
    // UPDATE
    // =====================================================
    @Transactional
    public PrioridadRegaloEntity updatePrioridadRegalo(Long prioridadId, PrioridadRegaloEntity prioridadRegaloEntity) {

        log.info("Inicia proceso de actualización de la prioridad de regalo con id: {}", prioridadId);

        Optional<PrioridadRegaloEntity> prioridadOpt = prioridadRegaloRepository.findById(prioridadId);
        if (prioridadOpt.isEmpty()) {
            throw new EntityNotFoundException("La prioridad de regalo con id " + prioridadId + " no existe.");
        }

        PrioridadRegaloEntity existente = prioridadOpt.get();

        // Regla 5: No se puede modificar el nivel de una prioridad si está siendo utilizada por regalos
        if (prioridadRegaloEntity.getNivel() != null && 
            !prioridadRegaloEntity.getNivel().equals(existente.getNivel())) {
            
            if (!existente.getRegalos().isEmpty()) {
                throw new IllegalStateException("No se puede modificar el nivel de una prioridad que está siendo utilizada por regalos.");
            }
            
            // Verificar que el nuevo nivel no exista
            List<PrioridadRegaloEntity> prioridadesMismoNivel = prioridadRegaloRepository.findByNivel(prioridadRegaloEntity.getNivel());
            if (!prioridadesMismoNivel.isEmpty()) {
                throw new IllegalArgumentException("Ya existe una prioridad con el nivel: " + prioridadRegaloEntity.getNivel());
            }
            existente.setNivel(prioridadRegaloEntity.getNivel());
        }

        // Actualización de datos válidos
        if (prioridadRegaloEntity.getDescripcion() != null) {
            existente.setDescripcion(prioridadRegaloEntity.getDescripcion());
        }
        if (prioridadRegaloEntity.getColor() != null) {
            existente.setColor(prioridadRegaloEntity.getColor());
        }

        log.info("Termina proceso de actualización de la prioridad de regalo con id: {}", prioridadId);
        return prioridadRegaloRepository.save(existente);
    }

    // =====================================================
    // DELETE
    // =====================================================
    @Transactional
    public void deletePrioridadRegalo(Long prioridadId) {

        log.info("Inicia proceso de eliminación de la prioridad de regalo con id: {}", prioridadId);

        Optional<PrioridadRegaloEntity> prioridadOpt = prioridadRegaloRepository.findById(prioridadId);
        if (prioridadOpt.isEmpty()) {
            throw new EntityNotFoundException("La prioridad de regalo con id " + prioridadId + " no existe.");
        }

        PrioridadRegaloEntity prioridad = prioridadOpt.get();

        // Regla 6: No se puede eliminar una prioridad si está asignada a algún regalo
        if (!prioridad.getRegalos().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar la prioridad porque está siendo utilizada por regalos.");
        }

        // Regla 7: No se puede eliminar la prioridad marcada como por defecto
        if (prioridad.getEsPorDefecto()) {
            throw new IllegalStateException("No se puede eliminar la prioridad marcada como por defecto.");
        }

        prioridadRegaloRepository.delete(prioridad);

        log.info("Termina proceso de eliminación de la prioridad de regalo con id: {}", prioridadId);
    }

    // =====================================================
    // GET
    // =====================================================
    @Transactional(readOnly = true)
    public List<PrioridadRegaloEntity> getAllPrioridadesRegalo() {
        log.info("Inicia proceso de consulta de todas las prioridades de regalo");
        return prioridadRegaloRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PrioridadRegaloEntity getPrioridadRegaloById(Long prioridadId) {
        log.info("Inicia proceso de consulta de la prioridad de regalo con id: {}", prioridadId);
        return prioridadRegaloRepository.findById(prioridadId)
                .orElseThrow(() -> new EntityNotFoundException("La prioridad de regalo con id " + prioridadId + " no existe."));
    }

    @Transactional(readOnly = true)
    public List<PrioridadRegaloEntity> getPrioridadesPorNivel(Integer nivel) {
        log.info("Inicia proceso de consulta de prioridades por nivel: {}", nivel);
        return prioridadRegaloRepository.findByNivel(nivel);
    }
}   