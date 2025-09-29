package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa a un Invitado (usuario que es invitado a celebraciones).
 */
@Data
@Entity
@Table(name = "invitado")
@EqualsAndHashCode(callSuper = true)
public class InvitadoEntity extends UsuarioEntity {

    // Asociación muchos a muchos con CelebracionEntity
    @ManyToMany
    @JoinTable(
            name = "invitado_celebracion",
            joinColumns = @JoinColumn(name = "invitado_id"),
            inverseJoinColumns = @JoinColumn(name = "celebracion_id")
    )
    private Set<CelebracionEntity> celebraciones = new HashSet<>();
}
