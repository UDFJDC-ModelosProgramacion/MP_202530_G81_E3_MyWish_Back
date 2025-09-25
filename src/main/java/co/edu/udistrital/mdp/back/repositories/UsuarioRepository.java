package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.back.entities.UsuarioEntity;

/**
 * Repositorio para gestionar la persistencia de UsuarioEntity
 */
@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    // Ejemplo de método de consulta personalizado
    UsuarioEntity findByCorreo(String correo);
}
