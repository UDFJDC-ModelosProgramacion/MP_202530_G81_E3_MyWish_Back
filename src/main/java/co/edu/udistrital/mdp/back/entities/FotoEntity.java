package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    // --- Relaciones 
    
    @PodamExclude
    @OneToOne(mappedBy = "foto")
    private RegaloEntity regalo;

    @PodamExclude
    @OneToOne(mappedBy = "foto")
    private ListaRegalosEntity listaRegalos;

    @ManyToOne
    private TiendaEntity tienda;

    @ManyToOne
    private ComentarioEntity comentario;
}