package co.edu.udistrital.mdp.back.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * DetailDTO para ComentarioEntity.
 * Incluye únicamente las listas asociadas (fotos).
 */
@Data
public class ComentarioDetailDTO extends ComentarioDTO {

    private List<FotoDTO> fotos = new ArrayList<>();
}
