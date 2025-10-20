package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class RegaloDTO {

    private Long id;

    private String descripcion;

    private String linkCompra;

    private Double precioEstimado;

    private String categoria;

    private EstadoCompraDTO estadoCompra;

    private TiendaDTO tienda;

    private FotoDTO foto;

    private PrioridadRegaloDTO prioridad;
}
