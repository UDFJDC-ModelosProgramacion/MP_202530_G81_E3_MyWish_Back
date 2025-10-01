package co.edu.udistrital.mdp.back.entities;

import java.util.ArrayList;
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
    @OneToMany(mappedBy = "tienda")
    private List<UbicacionEntity> ubicaciones;
    
    @PodamExclude
    @ManyToOne
    private CatalogoTiendasEntity catalogo;

    @PodamExclude
    @OneToMany(mappedBy = "tienda")
    private List<ComentarioEntity> comentarios = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "tienda")
    private List<RegaloEntity> regalos = new ArrayList<>();

    PodamExclude
    @OneToMany(mappedBy = "tienda")
    private List<FotoEntity> fotos;

}

