package co.edu.udistrital.mdp.back.dtos;

import lombok.Getter;
import lombok.Setter;
import co.edu.udistrital.mdp.back.entities.ComentarioEntity;

/**
 * DTO básico de Comentario
 */
@Getter
@Setter
public class ComentarioDTO {

    private String texto;
    private int calificacion;

    public ComentarioDTO() {}

    public ComentarioDTO(ComentarioEntity entity) {
        if (entity != null) {
            this.texto = entity.getTexto();
            this.calificacion = entity.getCalificacion();
        }
    }

    public ComentarioEntity toEntity() {
        ComentarioEntity entity = new ComentarioEntity();
        entity.setTexto(this.texto);
        entity.setCalificacion(this.calificacion);
        return entity;
    }
}
