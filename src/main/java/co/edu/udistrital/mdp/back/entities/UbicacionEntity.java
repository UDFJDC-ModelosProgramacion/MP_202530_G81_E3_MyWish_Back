package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ubicaciones")
@Getter
@Setter
public class UbicacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direccion;
    private String ciudad;
    private String pais;

    public UbicacionEntity() {}

    public UbicacionEntity(String direccion, String ciudad, String pais) {
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
    }
}
