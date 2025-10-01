package co.edu.udistrital.mdp.back.entities;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class TiendaEntity extends BaseEntity {

    private String nombre;
    private String descripcion;
    private String link;

    @PodamExclude
    @ManyToOne
    private UbicacionEntity ubicacion;

    @PodamExclude
    @OneToMany(mappedBy = "tienda")
    private List<RegaloEntity> regalos;

}
