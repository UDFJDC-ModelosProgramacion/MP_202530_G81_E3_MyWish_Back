package co.edu.udistrital.mdp.back.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class TiendaEntity extends BaseEntity {

    private String nombre;

    private String link;

    private String descripcion;

    @PodamExclude
    @OneToOne(cascade = CascadeType.ALL)
    private UbicacionEntity ubicacion;

    @PodamExclude
    @OneToMany(mappedBy = "tienda", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<RegaloEntity> regalos = new ArrayList<>();
}
