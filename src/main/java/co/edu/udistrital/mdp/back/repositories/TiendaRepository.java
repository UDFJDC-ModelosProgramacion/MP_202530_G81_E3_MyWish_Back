package co.edu.udistrital.mdp.back.repositories;

import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiendaRepository extends JpaRepository<TiendaEntity, Long> {
}
