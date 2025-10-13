package co.edu.udistrital.mdp.back.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.entities.UbicacionEntity;

/**
 * Repositorio para la entidad Tienda.
 * Permite realizar operaciones CRUD y consultas personalizadas
 * sobre las tiendas en la base de datos.
 */
@Repository
public interface TiendaRepository extends JpaRepository<TiendaEntity, Long> {

    // Buscar tiendas por nombre
    List<TiendaEntity> findByNombre(String nombre); 

    // Buscar tiendas por nombre que contenga una palabra clave (ignora mayúsculas/minúsculas)
    List<TiendaEntity> findByNombreContainingIgnoreCase(String keyword);

    // Buscar tiendas por ciudad de su ubicación
    List<TiendaEntity> findByUbicaciones_Ciudad(String ciudad);

    // Buscar tiendas por país de su ubicación
    List<TiendaEntity> findByUbicaciones_Pais(String pais);

    // Buscar tiendas por link
    List<TiendaEntity> findByLink(String link);

    // Buscar tiendas por descripción
    List<TiendaEntity> findByDescripcionContainingIgnoreCase(String descripcion);

    // Buscar tiendas que tengan una ubicación específica
    List<TiendaEntity> findByUbicaciones(UbicacionEntity ubicacion);
}
