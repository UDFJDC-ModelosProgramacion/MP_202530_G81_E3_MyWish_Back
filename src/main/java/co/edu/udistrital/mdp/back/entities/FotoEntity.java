package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class FotoEntity extends BaseEntity {

    private String url;
    private String descripcion;
    private String tipoArchivo;
    private Long tamanioBytes;
    private Boolean esPrincipal = false;

    // --- Relaciones ---
    
    @PodamExclude
    @ManyToOne
    private RegaloEntity regalo;

    @PodamExclude
    @ManyToOne
    private ListaRegalosEntity listaRegalos;

    @PodamExclude
    @ManyToOne
    private TiendaEntity tienda;

    @PodamExclude
    @ManyToOne
    private ComentarioEntity comentario;
}