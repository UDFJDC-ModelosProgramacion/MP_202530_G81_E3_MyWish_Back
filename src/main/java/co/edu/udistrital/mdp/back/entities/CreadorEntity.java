package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un Creador (usuario que crea listas y celebraciones).
 */
@Data
@Entity
@Table(name = "creador")
@EqualsAndHashCode(callSuper = true) 
public class CreadorEntity extends UsuarioEntity {

    
    private String biografia;
    private String paginaWeb;

    // Relación uno a muchos con ListaRegalosEntity
    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ListaRegalosEntity> listasRegalos = new ArrayList<>();

    // Relación uno a muchos con CelebracionEntity
    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CelebracionEntity> celebraciones = new ArrayList<>();
}

