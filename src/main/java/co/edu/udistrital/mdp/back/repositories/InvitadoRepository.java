package co.edu.udistrital.mdp.back.repositories;

import co.edu.udistrital.mdp.back.entities.InvitadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitadoRepository extends JpaRepository<InvitadoEntity, Long> {

    // Buscar un invitado por correo
    Optional<InvitadoEntity> findByCorreo(String correo);

    // Si prefieres buscar por código (id)
    Optional<InvitadoEntity> findById(Long id);
}
