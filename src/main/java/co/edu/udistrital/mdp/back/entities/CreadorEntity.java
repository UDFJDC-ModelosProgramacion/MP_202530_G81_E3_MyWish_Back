package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

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

    @PodamExclude
    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ListaRegalosEntity> listasRegalos = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CelebracionEntity> celebraciones = new ArrayList<>();
}
