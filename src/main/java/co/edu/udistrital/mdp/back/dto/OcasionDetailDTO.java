package co.edu.udistrital.mdp.back.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * DetailDTO de OcasionEntity.
 * Incluye la información de las asociaciones con otras entidades.
 */
@Data
public class OcasionDetailDTO extends OcasionDTO {

    private List<ListaRegalosDTO> listasRegalos = new ArrayList<>();
}
