package co.edu.udistrital.mdp.back.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.back.entities.RegaloEntity;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.entities.EstadoCompraEntity;
import co.edu.udistrital.mdp.back.entities.PrioridadRegaloEntity;

/**
 * Repositorio para la entidad Regalo.
 * Permite realizar operaciones CRUD y consultas personalizadas
 * sobre los regalos en la base de datos.
 */
@Repository
public interface RegaloRepository extends JpaRepository<RegaloEntity, Long> {

    // Buscar regalos por categoría
    List<RegaloEntity> findByCategoria(String categoria);

    // Buscar regalos por precio estimado menor a
    List<RegaloEntity> findByPrecioEstimadoLessThan(Double precio);

    // Buscar regalos por precio estimado mayor a
    List<RegaloEntity> findByPrecioEstimadoGreaterThan(Double precio);

    // Buscar regalos por tienda
    List<RegaloEntity> findByTienda(TiendaEntity tienda);

    // Buscar regalos por estado de compra
    List<RegaloEntity> findByEstadoCompra(EstadoCompraEntity estado);

    // Buscar regalos por prioridad
    List<RegaloEntity> findByPrioridad(PrioridadRegaloEntity prioridad);

    // Buscar regalos por descripción que contenga texto
    List<RegaloEntity> findByDescripcionContainingIgnoreCase(String descripcion);
}
