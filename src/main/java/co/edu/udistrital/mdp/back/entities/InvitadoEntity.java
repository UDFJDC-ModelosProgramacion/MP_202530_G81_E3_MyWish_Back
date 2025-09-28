package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import lombok.Data;

/**
 * Clase que representa un invitado, que hereda de UsuarioEntity
 */
@Data
@Entity
public class InvitadoEntity extends UsuarioEntity {

    @Column(nullable = true)
    private String codigoInvitacion;

    @Column(nullable = true)
    private String motivoVisita;

}
