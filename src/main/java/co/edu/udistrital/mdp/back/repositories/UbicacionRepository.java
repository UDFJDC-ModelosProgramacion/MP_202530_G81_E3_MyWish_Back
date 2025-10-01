package co.edu.udistrital.mdp.back.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.UbicacionEntity;

/**
 * Repositorio para la entidad Ubicacion.
 * Permite realizar operaciones CRUD y consultas personalizadas
 * sobre las ubicaciones en la base de datos.
 */
@Repository
public interface UbicacionRepository extends JpaRepository<UbicacionEntity, Long> {

    // Buscar por ciudad
    List<UbicacionEntity> findByCiudad(String ciudad);

    // Buscar por país
    List<UbicacionEntity> findByPais(String pais);

    // Buscar por ciudad y país
    List<UbicacionEntity> findByCiudadAndPais(String ciudad, String pais);
}
