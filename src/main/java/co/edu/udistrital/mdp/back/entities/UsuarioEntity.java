package co.edu.udistrital.mdp.back.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa un usuario en la persistencia
 */
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UsuarioEntity extends BaseEntity {

    private String correo;
    private String nombre;

    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @PodamExclude
    @OneToMany(mappedBy = "usuario")
    private List<ComentarioEntity> comentarios = new ArrayList<>();
}
