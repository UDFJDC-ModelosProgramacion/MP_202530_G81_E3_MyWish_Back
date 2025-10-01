package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class CategoriaRegaloEntity extends BaseEntity {

    private String nombre;
    private String descripcion;
    private String color;
    private Boolean esPorDefecto = false;

    // --- Relaciones ---
    
    @PodamExclude
    @OneToMany(mappedBy = "categoria")
    private List<RegaloEntity> regalos = new ArrayList<>();
}