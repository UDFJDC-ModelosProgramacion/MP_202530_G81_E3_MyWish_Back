package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.back.entities.InvitadoEntity;

/**
 * Repositorio para gestionar la persistencia de InvitadoEntity
 */
@Repository
public interface InvitadoRepository extends JpaRepository<InvitadoEntity, Long> {

    // Ejemplo de método de consulta personalizado
    InvitadoEntity findByCodigoInvitacion(String codigoInvitacion);
}
