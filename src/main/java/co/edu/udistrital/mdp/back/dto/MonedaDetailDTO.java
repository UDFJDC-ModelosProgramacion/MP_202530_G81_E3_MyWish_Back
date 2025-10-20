package co.edu.udistrital.mdp.back.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * DetailDTO de MonedaEntity.
 * Incluye las asociaciones con otras entidades (listas de regalos).
 */
@Data
public class MonedaDetailDTO extends MonedaDTO {

    private List<ListaRegalosDTO> listasRegalos = new ArrayList<>();
}

