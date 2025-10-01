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
    private String direccion;
    private String telefono;
    private String sitioWeb;

    // Constructor vacío
    public TiendaEntity() {}

    // Constructor con parámetros
    public TiendaEntity(String nombre, String direccion, String telefono, String sitioWeb) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.sitioWeb = sitioWeb;
    }
}
