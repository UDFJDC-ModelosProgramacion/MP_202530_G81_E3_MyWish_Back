package co.edu.udistrital.mdp.back.repositories;

import co.edu.udistrital.mdp.back.entities.RegaloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegaloRepository extends JpaRepository<RegaloEntity, Long> {
    
}
