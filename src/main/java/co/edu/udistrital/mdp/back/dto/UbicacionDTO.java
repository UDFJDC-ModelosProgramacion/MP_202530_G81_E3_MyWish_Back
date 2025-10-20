package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class UbicacionDTO {

    private Long id;

    private String direccion;

    private String ciudad;

    private String pais;

    private TiendaDTO tienda;
}
