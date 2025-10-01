package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "regalos")
@Getter
@Setter
public class RegaloEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private String linkCompra;
    private Double precioEstimado;
    private String categoria;

    // Constructor vacío
    public RegaloEntity() {}

    // Constructor con parámetros
    public RegaloEntity(String descripcion, String linkCompra, Double precioEstimado, String categoria) {
        this.descripcion = descripcion;
        this.linkCompra = linkCompra;
        this.precioEstimado = precioEstimado;
        this.categoria = categoria;
    }
}

