package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class RegaloEntity extends BaseEntity {

    private String descripcion;

    private String linkCompra;

    private Double precioEstimado;

    @PodamExclude
    @ManyToOne
    private TiendaEntity tienda;
}
