package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ocasion")
public class OcasionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    // ===== Constructores =====
    public Ocasion() {}

    public Ocasion(String nombre) {
        this.nombre = nombre;
    }

    // ===== Getters y Setters =====
    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

