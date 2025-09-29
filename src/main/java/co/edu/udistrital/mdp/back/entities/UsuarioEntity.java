package co.edu.udistrital.mdp.back.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Clase que representa un usuario en la persistencia
 */
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UsuarioEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String nombre;

    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    // Relación uno a muchos con Comentario
    @OneToMany(mappedBy = "usuario")
    private List<ComentarioEntity> comentarios = new ArrayList<>();
}
