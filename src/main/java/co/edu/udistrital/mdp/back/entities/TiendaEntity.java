package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tiendas")
@Getter
@Setter
public class TiendaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String link;
    private String descripcion;

    // Constructor vacío
    public TiendaEntity() {}

    // Constructor con parámetros
    public TiendaEntity(String nombre, String link, String descripcion) {
        this.nombre = nombre;
        this.link = link;
        this.descripcion = descripcion;
    }
}
