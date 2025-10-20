package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class TiendaDTO {

    private Long id;

    private String nombre;

    private String link;

    private String descripcion;

    // Relaciones
    private CatalogoTiendasDTO catalogoTiendas;
}
