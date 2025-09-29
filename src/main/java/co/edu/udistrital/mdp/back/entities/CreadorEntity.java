package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entidad que representa a un Creador (usuario que crea listas y celebraciones).
 */
@Data
@Entity
@Table(name = "creador")
@EqualsAndHashCode(callSuper = true)
public class CreadorEntity extends UsuarioEntity {

    // Aquí van atributos específicos de un creador
    private String biografia;
    private String paginaWeb;
}
