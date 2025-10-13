package co.edu.udistrital.mdp.back.repositories;

import co.edu.udistrital.mdp.back.entities.MonedaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonedaRepository extends JpaRepository<MonedaEntity, Long> {
    // Aquí puedes agregar consultas personalizadas si se necesitan
}
 