package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

/**
 * DTO para la entidad OcasionEntity.
 * Representa la información básica que se envía o recibe desde el API.
 */
@Data
public class OcasionDTO {

    private Long id;
    private String nombre;
}
