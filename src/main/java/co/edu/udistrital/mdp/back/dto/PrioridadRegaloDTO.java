package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class PrioridadRegaloDTO {
    private Long id;
    private String nombre;
    private Integer nivel;
    private String descripcion;
    private String color;
    private Boolean esPorDefecto;
}