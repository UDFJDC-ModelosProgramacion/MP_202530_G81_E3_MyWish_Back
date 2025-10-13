package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que representa una Ubicación dentro del sistema.
 */
@Data
@Entity
public class UbicacionEntity extends BaseEntity {

    private String direccion;
    private String ciudad;
    private String pais;

    @PodamExclude
    @ManyToOne // muchas ubicaciones pueden pertenecer a una misma tienda
    private TiendaEntity tienda; 

}
