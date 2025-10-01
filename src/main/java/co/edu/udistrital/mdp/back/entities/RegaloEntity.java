package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class RegaloEntity extends BaseEntity {

    private String descripcion;
    private String linkCompra;
    private Double precioEstimado;
    private String categoria;

    @PodamExclude
    @OneToOne
    private EstadoCompraEntity estadoCompra;

    @PodamExclude
    @ManyToOne
    private TiendaEntity tienda;

    @PodamExclude
    @OneToOne
    private FotoEntity foto;

    @PodamExclude
    @OneToOne
    private PrioridadRegaloEntity prioridad;
}
