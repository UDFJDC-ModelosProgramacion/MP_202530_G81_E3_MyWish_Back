package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class EstadoCompraDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String color;
    private Boolean esPorDefecto;
}