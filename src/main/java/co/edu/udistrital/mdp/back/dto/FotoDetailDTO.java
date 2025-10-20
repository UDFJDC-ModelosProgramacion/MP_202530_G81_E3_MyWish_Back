package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class FotoDetailDTO extends FotoDTO {

    private RegaloDTO regalo;
    private ListaRegalosDTO listaRegalos;
    private TiendaDTO tienda;
    private ComentarioDTO comentario;
}