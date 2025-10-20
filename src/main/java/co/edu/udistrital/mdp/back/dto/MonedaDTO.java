package co.edu.udistrital.mdp.back.dtos;

import lombok.Getter;
import lombok.Setter;
import co.edu.udistrital.mdp.back.entities.MonedaEntity;

@Getter
@Setter
public class MonedaDTO {

    private String codigo;
    private String simbolo;

    public MonedaDTO() {}

    public MonedaDTO(MonedaEntity entity) {
        if (entity != null) {
            this.codigo = entity.getCodigo();
            this.simbolo = entity.getSimbolo();
        }
    }

    public MonedaEntity toEntity() {
        MonedaEntity entity = new MonedaEntity();
        entity.setCodigo(this.codigo);
        entity.setSimbolo(this.simbolo);
        return entity;
    }
}
