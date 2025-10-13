package co.edu.udistrital.mdp.back.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.back.entities.UbicacionEntity;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;

/**
 * Repositorio para la entidad Ubicacion.
 * Permite realizar operaciones CRUD y consultas personalizadas
 * sobre las ubicaciones en la base de datos.
 */
@Repository
public interface UbicacionRepository extends JpaRepository<UbicacionEntity, Long> {

    // Buscar ubicaciones por ciudad
    List<UbicacionEntity> findByCiudad(String ciudad);

    // Buscar ubicaciones por país 
    List<UbicacionEntity> findByPais(String pais);

    // Buscar ubicaciones por dirección exacta
    List<UbicacionEntity> findByDireccion(String direccion);

    // Buscar ubicaciones de una tienda
    List<UbicacionEntity> findByTienda(TiendaEntity tienda);

    // Buscar ubicaciones de una tienda por ID
    List<UbicacionEntity> findByTiendaId(Long tiendaId);
}
