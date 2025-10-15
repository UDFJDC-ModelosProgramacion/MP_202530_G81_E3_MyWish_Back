package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class PrioridadRegaloEntity extends BaseEntity {

    private String nombre;
    private Integer nivel;
    private String descripcion;
    private String color;
    private Boolean esPorDefecto = false;

    // --- Relaciones ---
    
    @PodamExclude
    @OneToMany(mappedBy = "prioridad")
    private List<RegaloEntity> regalos = new ArrayList<>();
}