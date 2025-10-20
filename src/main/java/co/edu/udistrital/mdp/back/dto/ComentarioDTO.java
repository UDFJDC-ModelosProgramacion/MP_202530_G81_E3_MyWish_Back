package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

/**
 * DTO para la entidad ComentarioEntity.
 * Contiene los atributos principales del comentario.
 */
@Data
public class ComentarioDTO {

    private Long id;
    private String texto;
    private int calificacion;
}
