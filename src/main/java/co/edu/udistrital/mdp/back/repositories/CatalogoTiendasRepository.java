package co.edu.udistrital.mdp.back.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.back.entities.CatalogoTiendasEntity;

public interface CatalogoTiendasRepository extends JpaRepository<CatalogoTiendasEntity, Long> {

    /**
     * Busca catálogos cuyo nombre sea exactamente igual al parámetro.
     * @param nombre Nombre del catálogo a buscar.
     * @return Lista de catálogos con ese nombre.
     */
    List<CatalogoTiendasEntity> findByNombre(String nombre);
}