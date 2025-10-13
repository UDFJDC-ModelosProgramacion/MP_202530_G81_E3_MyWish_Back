package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.back.entities.FiltroRegaloEntity;

/**
 * Repositorio para acceder a los filtros de regalo en la base de datos.
 * Provee método para búsqueda basado en criterio.
 */
@Repository
public interface FiltroRegaloRepository extends JpaRepository<FiltroRegaloEntity, Long> {

    /**
     * Buscar un filtro por su criterio exacto.
     * @param criterio Criterio de filtro.
     * @return Filtro que coincide con el criterio.
     */
    FiltroRegaloEntity findByCriterio(String criterio); 
}

