package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class UbicacionEntity extends BaseEntity {

    private String direccion;

    private String ciudad;

    private String pais;

    @PodamExclude
    @OneToOne(mappedBy = "ubicacion")
    private TiendaEntity tienda;
}
