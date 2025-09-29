package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un Invitado (usuario que es invitado a celebraciones).
 */
@Data
@Entity
@Table(name = "invitado")
@EqualsAndHashCode(callSuper = true)
public class InvitadoEntity extends UsuarioEntity {

    @PodamExclude
    @ManyToMany(mappedBy = "invitados") // relación bidireccional
    private List<CelebracionEntity> celebraciones = new ArrayList<>();
}
