package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioEntity extends BaseEntity {


    private String texto;

    private int calificacion; 

    // Relación con UsuarioEntity (muchos comentarios pueden ser hechos por un usuario)
    @ManyToOne
    private UsuarioEntity usuario;

    // Relación opcional con TiendaEntity (0..1)
    @ManyToOne
    private TiendaEntity tienda;

    // Relación con FotoEntity (un comentario puede tener muchas fotos)
    @OneToMany(mappedBy = "comentario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoEntity> fotos;
}

