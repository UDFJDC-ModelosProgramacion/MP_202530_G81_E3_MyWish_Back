package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

/**
 * DTO para la entidad ComentarioEntity.
 * Incluye las asociaciones simples (usuario y tienda).
 */
@Data
public class ComentarioDTO {

    private Long id;
    private String texto;
    private int calificacion;
    private UsuarioDTO usuario;
    private TiendaDTO tienda;
}
