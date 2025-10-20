package co.edu.udistrital.mdp.back.dtos;

import lombok.Getter;
import lombok.Setter;
import co.edu.udistrital.mdp.back.entities.OcasionEntity;

@Getter
@Setter
public class OcasionDTO {

    private String nombre;

    public OcasionDTO() {}

    public OcasionDTO(OcasionEntity entity) {
        if (entity != null) {
            this.nombre = entity.getNombre();
        }
    }

    public OcasionEntity toEntity() {
        OcasionEntity entity = new OcasionEntity();
        entity.setNombre(this.nombre);
        return entity;
    }
}

