package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class FotoDTO {

    private Long id;

    private String url;

    private String descripcion;

    private String tipoArchivo;

    private Long tamanioBytes;

    private Boolean esPrincipal;

    private UsuarioDTO usuario;

    private ListaRegalosDTO listaRegalos;

    private TiendaDTO tienda;

    private ComentarioDTO comentario;

}