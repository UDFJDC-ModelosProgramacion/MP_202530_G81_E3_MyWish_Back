package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.EstadoCompraEntity;
import co.edu.udistrital.mdp.back.repositories.EstadoCompraRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EstadoCompraService {
 
    @Autowired
    private EstadoCompraRepository estadoCompraRepository;

    // =====================================================
    // CREATE
    // =====================================================
    @Transactional
    public EstadoCompraEntity createEstadoCompra(EstadoCompraEntity estadoCompraEntity) {

        log.info("Inicia proceso de creación del estado de compra");

        // Regla 1: No se puede crear un estado de compra sin nombre
        if (estadoCompraEntity.getNombre() == null || estadoCompraEntity.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("No se puede crear un estado de compra sin nombre.");
        }

        // Regla 2: No puede existir más de un estado de compra con el mismo nombre
        if (estadoCompraRepository.existsByNombre(estadoCompraEntity.getNombre())) {
            throw new IllegalArgumentException("Ya existe un estado de compra con el nombre: " + estadoCompraEntity.getNombre());
        }

        // Regla 3: El estado "Pendiente" debe crearse automáticamente como estado por defecto
        if ("Pendiente".equalsIgnoreCase(estadoCompraEntity.getNombre())) {
            estadoCompraEntity.setEsPorDefecto(true);
        }

        log.info("Termina proceso de creación del estado de compra");
        return estadoCompraRepository.save(estadoCompraEntity);
    }

    // =====================================================
    // UPDATE
    // =====================================================
    @Transactional
    public EstadoCompraEntity updateEstadoCompra(Long estadoCompraId, EstadoCompraEntity estadoCompraEntity) {

        log.info("Inicia proceso de actualización del estado de compra con id: {}", estadoCompraId);

        Optional<EstadoCompraEntity> estadoCompraOpt = estadoCompraRepository.findById(estadoCompraId);
        if (estadoCompraOpt.isEmpty()) {
            throw new EntityNotFoundException("El estado de compra con id " + estadoCompraId + " no existe.");
        }

        EstadoCompraEntity existente = estadoCompraOpt.get();

        // Regla 4: No se puede modificar el nombre de un estado de compra si está siendo utilizado por regalos
        if (estadoCompraEntity.getNombre() != null && 
            !estadoCompraEntity.getNombre().equals(existente.getNombre())) {
            
            if (!existente.getRegalos().isEmpty()) {
                throw new IllegalStateException("No se puede modificar el nombre de un estado de compra que está siendo utilizado por regalos.");
            }
            
            // Verificar que el nuevo nombre no exista
            if (estadoCompraRepository.existsByNombre(estadoCompraEntity.getNombre())) {
                throw new IllegalArgumentException("Ya existe un estado de compra con el nombre: " + estadoCompraEntity.getNombre());
            }
            existente.setNombre(estadoCompraEntity.getNombre());
        }

        // Actualización de datos válidos
        if (estadoCompraEntity.getDescripcion() != null) {
            existente.setDescripcion(estadoCompraEntity.getDescripcion());
        }
        if (estadoCompraEntity.getColor() != null) {
            existente.setColor(estadoCompraEntity.getColor());
        }

        log.info("Termina proceso de actualización del estado de compra con id: {}", estadoCompraId);
        return estadoCompraRepository.save(existente);
    }

    // =====================================================
    // DELETE
    // =====================================================
    @Transactional
    public void deleteEstadoCompra(Long estadoCompraId) {

        log.info("Inicia proceso de eliminación del estado de compra con id: {}", estadoCompraId);

        Optional<EstadoCompraEntity> estadoCompraOpt = estadoCompraRepository.findById(estadoCompraId);
        if (estadoCompraOpt.isEmpty()) {
            throw new EntityNotFoundException("El estado de compra con id " + estadoCompraId + " no existe.");
        }

        EstadoCompraEntity estadoCompra = estadoCompraOpt.get();

        // Regla 5: No se puede eliminar un estado de compra si está asignado a algún regalo
        if (!estadoCompra.getRegalos().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar el estado de compra porque está siendo utilizado por regalos.");
        }

        // Regla 6: No se puede eliminar el estado de compra marcado como por defecto
        if (estadoCompra.getEsPorDefecto()) {
            throw new IllegalStateException("No se puede eliminar el estado de compra marcado como por defecto.");
        }

        estadoCompraRepository.delete(estadoCompra);

        log.info("Termina proceso de eliminación del estado de compra con id: {}", estadoCompraId);
    }

    // =====================================================
    // GET
    // =====================================================
    @Transactional(readOnly = true)
    public List<EstadoCompraEntity> getAllEstadosCompra() {
        log.info("Inicia proceso de consulta de todos los estados de compra");
        return estadoCompraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EstadoCompraEntity getEstadoCompraById(Long estadoCompraId) {
        log.info("Inicia proceso de consulta del estado de compra con id: {}", estadoCompraId);
        return estadoCompraRepository.findById(estadoCompraId)
                .orElseThrow(() -> new EntityNotFoundException("El estado de compra con id " + estadoCompraId + " no existe."));
    }

    @Transactional(readOnly = true)
    public EstadoCompraEntity getEstadoCompraPorDefecto() {
        log.info("Inicia proceso de consulta del estado de compra por defecto");
        List<EstadoCompraEntity> estadosPorDefecto = estadoCompraRepository.findByEsPorDefectoTrue();
        return estadosPorDefecto.isEmpty() ? null : estadosPorDefecto.get(0);
    }
}