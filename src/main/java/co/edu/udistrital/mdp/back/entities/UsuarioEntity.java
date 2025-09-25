package co.edu.udistrital.mdp.back.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;

/**
 * Clase que representa un usuario en la persistencia
 */
@Data
@Entity
public class UsuarioEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String nombre;

    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
}
