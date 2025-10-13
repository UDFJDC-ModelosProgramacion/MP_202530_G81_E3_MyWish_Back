package co.edu.udistrital.mdp.back.repositories;

import co.edu.udistrital.mdp.back.entities.ComentarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Long> {
    // Aquí puedes agregar consultas personalizadas si se necesitan 
}