package co.edu.udistrital.mdp.back.dtos;

import lombok.Getter;
import lombok.Setter;
import co.edu.udistrital.mdp.back.entities.ComentarioEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DetailDTO de Comentario que incluye relaciones.
 */
@Getter
@Setter
public class ComentarioDetail extends ComentarioDTO {

    private UsuarioDTO usuario;
    private TiendaDTO tienda;
    private List<FotoDTO> fotos;

    public ComentarioDetail() {
        super();
    }

    public ComentarioDetail(ComentarioEntity entity) {
        super(entity);
        if (entity != null) {
            if (entity.getUsuario() != null)
                this.usuario = new UsuarioDTO(entity.getUsuario());
            if (entity.getTienda() != null)
                this.tienda = new TiendaDTO(entity.getTienda());
            if (entity.getFotos() != null)
                this.fotos = entity.getFotos().stream()
                    .map(FotoDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ComentarioEntity toEntity() {
        ComentarioEntity entity = super.toEntity();
        if (usuario != null)
            entity.setUsuario(usuario.toEntity());
        if (tienda != null)
            entity.setTienda(tienda.toEntity());
        if (fotos != null)
            entity.setFotos(fotos.stream().map(FotoDTO::toEntity).collect(Collectors.toList()));
        return entity;
    }
}
