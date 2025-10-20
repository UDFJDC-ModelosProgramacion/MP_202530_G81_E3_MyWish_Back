package co.edu.udistrital.mdp.back.dtos;

import lombok.Getter;
import lombok.Setter;
import co.edu.udistrital.mdp.back.entities.MonedaEntity;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MonedaDetail extends MonedaDTO {

    private List<ListaRegalosDTO> listasRegalos;

    public MonedaDetail() {
        super();
    }

    public MonedaDetail(MonedaEntity entity) {
        super(entity);
        if (entity != null && entity.getListasRegalos() != null) {
            this.listasRegalos = entity.getListasRegalos().stream()
                    .map(ListaRegalosDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public MonedaEntity toEntity() {
        MonedaEntity entity = super.toEntity();
        if (listasRegalos != null)
            entity.setListasRegalos(listasRegalos.stream()
                .map(ListaRegalosDTO::toEntity)
                .collect(Collectors.toList()));
        return entity;
    }
}

