package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "moneda")
public class MonedaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 10)
    private String simbolo;

    // ===== Constructores =====
    public Moneda() {}

    public Moneda(String codigo, String simbolo) {
        this.codigo = codigo;
        this.simbolo = simbolo;
    }

    // ===== Getters y Setters =====
    public Long getId() { return id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }
}
