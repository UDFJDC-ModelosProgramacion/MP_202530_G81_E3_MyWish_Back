package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.*;
import co.edu.udistrital.mdp.back.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class RegaloService {

    @Autowired private RegaloRepository regaloRepo;
    @Autowired private TiendaRepository tiendaRepo;
    @Autowired private EstadoCompraRepository estadoRepo;
    @Autowired private PrioridadRegaloRepository prioridadRepo;

    @Transactional(readOnly = true)
    public List<RegaloEntity> getAll() {
        log.info("Listando todos los regalos");
        return regaloRepo.findAll();
    }

    @Transactional(readOnly = true)
    public RegaloEntity getById(Long id) {
        log.info("Buscando regalo con id {}", id);
        return regaloRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el regalo con id " + id));
    }

    @Transactional
    public RegaloEntity create(RegaloEntity regalo) {
        log.info("Creando nuevo regalo: {}", regalo.getDescripcion());
        return regaloRepo.save(regalo);
    }

    @Transactional
    public RegaloEntity update(Long id, RegaloEntity nuevo) {
        log.info("Actualizando regalo con id {}", id);
        RegaloEntity existente = getById(id);

        existente.setDescripcion(nuevo.getDescripcion());
        existente.setLinkCompra(nuevo.getLinkCompra());
        existente.setPrecioEstimado(nuevo.getPrecioEstimado());
        existente.setCategoria(nuevo.getCategoria());
        existente.setTienda(nuevo.getTienda());
        existente.setEstadoCompra(nuevo.getEstadoCompra());
        existente.setFoto(nuevo.getFoto());
        existente.setListaRegalos(nuevo.getListaRegalos());
        existente.setPrioridad(nuevo.getPrioridad());

        return regaloRepo.save(existente);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Eliminando regalo con id {}", id);
        RegaloEntity existente = getById(id);
        regaloRepo.delete(existente);
    }

    @Transactional(readOnly = true)
    public List<RegaloEntity> findByCategoria(String categoria) {
        log.info("Buscando regalos por categoría: {}", categoria);
        return regaloRepo.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    public List<RegaloEntity> findByPrecioMenorA(Double precio) {
        log.info("Buscando regalos con precio menor a {}", precio);
        return regaloRepo.findByPrecioEstimadoLessThan(precio);
    }

    @Transactional(readOnly = true)
    public List<RegaloEntity> findByPrecioMayorA(Double precio) {
        log.info("Buscando regalos con precio mayor a {}", precio);
        return regaloRepo.findByPrecioEstimadoGreaterThan(precio);
    }

    @Transactional(readOnly = true)
    public List<RegaloEntity> findByDescripcion(String texto) {
        log.info("Buscando regalos con descripción que contiene: {}", texto);
        return regaloRepo.findByDescripcionContainingIgnoreCase(texto);
    }

    @Transactional(readOnly = true)
    public List<RegaloEntity> findByTienda(Long tiendaId) {
        log.info("Buscando regalos por tienda con id {}", tiendaId);
        TiendaEntity tienda = tiendaRepo.findById(tiendaId)
                .orElseThrow(() -> new EntityNotFoundException("No existe tienda con id " + tiendaId));
        return regaloRepo.findByTienda(tienda);
    }

    @Transactional(readOnly = true)
    public List<RegaloEntity> findByEstadoCompra(Long estadoId) {
        log.info("Buscando regalos con estado de compra id {}", estadoId);
        EstadoCompraEntity estado = estadoRepo.findById(estadoId)
                .orElseThrow(() -> new EntityNotFoundException("No existe estado de compra con id " + estadoId));
        return regaloRepo.findByEstadoCompra(estado);
    }

    @Transactional(readOnly = true)
    public List<RegaloEntity> findByPrioridad(Long prioridadId) {
        log.info("Buscando regalos con prioridad id {}", prioridadId);
        PrioridadRegaloEntity prioridad = prioridadRepo.findById(prioridadId)
                .orElseThrow(() -> new EntityNotFoundException("No existe prioridad con id " + prioridadId));
        return regaloRepo.findByPrioridad(prioridad);
    }
}
