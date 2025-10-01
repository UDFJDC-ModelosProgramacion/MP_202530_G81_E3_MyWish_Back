package co.edu.udistrital.mdp.back.repositories;

import co.edu.udistrital.mdp.back.entities.UbicacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<UbicacionEntity, Long> {
}
