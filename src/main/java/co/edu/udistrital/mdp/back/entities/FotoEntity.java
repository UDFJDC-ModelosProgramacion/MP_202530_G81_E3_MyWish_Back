package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
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

    // --- Relaciones (OneToOne en lugar de ManyToOne) ---
    
    @PodamExclude
    @OneToOne(mappedBy = "foto")
    private RegaloEntity regalo;

    @PodamExclude
    @OneToOne(mappedBy = "foto")
    private ListaRegalosEntity listaRegalos;

    @PodamExclude
    @OneToOne(mappedBy = "foto")
    private TiendaEntity tienda;

    @PodamExclude
    @OneToOne(mappedBy = "foto")
    private ComentarioEntity comentario;
}