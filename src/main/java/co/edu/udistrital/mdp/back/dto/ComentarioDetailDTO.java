package co.edu.udistrital.mdp.back.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * DetailDTO para ComentarioEntity.
 * Incluye las asociaciones con usuario, tienda y fotos.
 */
@Data
public class ComentarioDetailDTO extends ComentarioDTO {

    private UsuarioDTO usuario;
    private TiendaDTO tienda;
    private List<FotoDTO> fotos = new ArrayList<>();
}
