package co.edu.udistrital.mdp.back.services;
import co.edu.udistrital.mdp.back.entities.EstadoCompraEntity;
import co.edu.udistrital.mdp.back.entities.RegaloEntity;
import co.edu.udistrital.mdp.back.repositories.EstadoCompraRepository;
import co.edu.udistrital.mdp.back.repositories.RegaloRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class EstadoCompraService {

    private static final String MENSAJE_ESTADO_NO_EXISTE = "El estado de compra con id ";
    private static final String MENSAJE_ESTADO_NO_EXISTE_FIN = " no existe.";
    private static final String MENSAJE_ESTADO_NOMBRE_USADO = "Ya existe un estado de compra con el nombre: ";
    private static final String MENSAJE_ESTADO_USADO_POR_REGALOS = "No se puede modificar/eliminar el estado de compra porque está siendo utilizado por regalos.";
    private static final String MENSAJE_ESTADO_POR_DEFECTO = "No se puede eliminar el estado de compra marcado como por defecto.";
    private static final String MENSAJE_ESTADO_SIN_NOMBRE = "No se puede crear un estado de compra sin nombre.";

    @Autowired
    private EstadoCompraRepository estadoCompraRepository;

    @Autowired
    private RegaloRepository regaloRepository;

    // =====================================================
    // CREATE
    // =====================================================
    @Transactional
    public EstadoCompraEntity createEstadoCompra(EstadoCompraEntity estadoCompraEntity) {
        log.info("Inicia proceso de creación del estado de compra");

        if (estadoCompraEntity.getNombre() == null || estadoCompraEntity.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_ESTADO_SIN_NOMBRE);
        }

        if (estadoCompraRepository.existsByNombre(estadoCompraEntity.getNombre())) {
            throw new IllegalArgumentException(MENSAJE_ESTADO_NOMBRE_USADO + estadoCompraEntity.getNombre());
        }

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

        EstadoCompraEntity existente = estadoCompraRepository.findById(estadoCompraId)
                .orElseThrow(() -> new EntityNotFoundException(MENSAJE_ESTADO_NO_EXISTE + estadoCompraId + MENSAJE_ESTADO_NO_EXISTE_FIN));

        if (estadoCompraEntity.getNombre() != null && !estadoCompraEntity.getNombre().equals(existente.getNombre())) {
            List<RegaloEntity> regalosAsociados = regaloRepository.findByEstadoCompra(existente);
            if (!regalosAsociados.isEmpty()) {
                throw new IllegalStateException(MENSAJE_ESTADO_USADO_POR_REGALOS);
            }

            if (estadoCompraRepository.existsByNombre(estadoCompraEntity.getNombre())) {
                throw new IllegalArgumentException(MENSAJE_ESTADO_NOMBRE_USADO + estadoCompraEntity.getNombre());
            }

            existente.setNombre(estadoCompraEntity.getNombre());
        }

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

        EstadoCompraEntity estadoCompra = estadoCompraRepository.findById(estadoCompraId)
                .orElseThrow(() -> new EntityNotFoundException(MENSAJE_ESTADO_NO_EXISTE + estadoCompraId + MENSAJE_ESTADO_NO_EXISTE_FIN));

        List<RegaloEntity> regalosAsociados = regaloRepository.findByEstadoCompra(estadoCompra);
        if (!regalosAsociados.isEmpty()) {
            throw new IllegalStateException(MENSAJE_ESTADO_USADO_POR_REGALOS);
        }

        if (Boolean.TRUE.equals(estadoCompra.getEsPorDefecto())) {
            throw new IllegalStateException(MENSAJE_ESTADO_POR_DEFECTO);
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
                .orElseThrow(() -> new EntityNotFoundException(MENSAJE_ESTADO_NO_EXISTE + estadoCompraId + MENSAJE_ESTADO_NO_EXISTE_FIN));
    }

    @Transactional(readOnly = true)
    public EstadoCompraEntity getEstadoCompraPorDefecto() {
        log.info("Inicia proceso de consulta del estado de compra por defecto");
        List<EstadoCompraEntity> estadosPorDefecto = estadoCompraRepository.findByEsPorDefectoTrue();
        return estadosPorDefecto.isEmpty() ? null : estadosPorDefecto.get(0);
    }
}
