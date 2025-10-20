package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

/**
 * DTO para la entidad MonedaEntity.
 * Representa la información básica que se expone en el API.
 */
@Data
public class MonedaDTO {

    private Long id;
    private String codigo;
    private String simbolo;
}
