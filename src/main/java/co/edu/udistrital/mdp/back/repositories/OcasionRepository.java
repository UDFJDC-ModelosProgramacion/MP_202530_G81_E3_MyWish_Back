package co.edu.udistrital.mdp.back.repositories;

import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OcasionRepository extends JpaRepository<OcasionEntity, Long> {
    // Aquí puedes agregar consultas personalizadas si se necesitan
}
  