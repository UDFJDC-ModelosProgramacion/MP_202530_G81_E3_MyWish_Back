package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que representa una ubicación en el sistema.
 * Contiene información como dirección, ciudad y país.
 */
@Data
@Entity
public class UbicacionEntity extends BaseEntity {

    private String direccion;
    private String ciudad;
    private String pais;
    private String codigoPostal;

    @PodamExclude
    @ManyToOne
    private TiendaEntity tienda; // Una ubicación puede estar asociada a una tienda
}
