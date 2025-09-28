package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "comentario")
public class ComentarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String texto;

    @Column(nullable = false)
    private int calificacion;

    // Relación con Usuario (muchos comentarios pueden ser hechos por un usuario)
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación opcional con Tienda (0..1)
    @ManyToOne
    @JoinColumn(name = "tienda_id")
    private Tienda tienda;

    // Relación con Fotos (un comentario puede tener muchas fotos)
    @OneToMany(mappedBy = "comentario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos;

    // ===== Constructores =====
    public Comentario() {}

    public Comentario(String texto, int calificacion, Usuario usuario, Tienda tienda) {
        this.texto = texto;
        this.calificacion = calificacion;
        this.usuario = usuario;
        this.tienda = tienda;
    }

    // ===== Getters y Setters =====
    public Long getId() { return id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Tienda getTienda() { return tienda; }
    public void setTienda(Tienda tienda) { this.tienda = tienda; }

    public List<Foto> getFotos() { return fotos; }
    public void setFotos(List<Foto> fotos) { this.fotos = fotos; }
}
