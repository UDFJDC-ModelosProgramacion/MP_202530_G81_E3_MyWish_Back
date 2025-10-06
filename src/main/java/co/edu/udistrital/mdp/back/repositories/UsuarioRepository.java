package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.back.entities.UsuarioEntity;

/**
 * Repositorio para manejar la persistencia de UsuarioEntity.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo correo único del usuario
     * @return UsuarioEntity si existe, null en caso contrario
     */
    UsuarioEntity findByCorreo(String correo);
}
