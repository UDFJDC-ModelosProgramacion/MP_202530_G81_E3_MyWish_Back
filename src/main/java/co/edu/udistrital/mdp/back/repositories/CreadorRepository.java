package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.back.entities.CreadorEntity;

@Repository
public interface CreadorRepository extends JpaRepository<CreadorEntity, Long> {

    // Buscar un creador por correo
    CreadorEntity findByCorreo(String correo);
}
