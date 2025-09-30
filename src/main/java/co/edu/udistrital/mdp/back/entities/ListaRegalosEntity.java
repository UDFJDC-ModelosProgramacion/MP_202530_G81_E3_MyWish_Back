package co.edu.udistrital.mdp.back.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa una lista de regalos en la persistencia.
 * Contiene información de la lista y sus relaciones con otras entidades.
 */
@Data
@Entity
public class ListaRegalosEntity extends BaseEntity {

    private String nombre;
    private String descripcion;
    private String color;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    // --- Relaciones ---

    @PodamExclude
    @ManyToOne
    private UsuarioEntity creador;

    @PodamExclude
    @ManyToMany
    private List<UsuarioEntity> invitados = new ArrayList<>();

    @PodamExclude
    @OneToOne
    private CelebracionEntity celebracion; // opcional

    @PodamExclude
    @OneToMany(mappedBy = "listaRegalos", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<RegaloEntity> regalos = new ArrayList<>();

    @PodamExclude
    @OneToOne
    private FotoEntity foto;

    @PodamExclude
    @OneToMany(mappedBy = "listaRegalos")
    private List<FiltroRegalo> filtrosRegalos = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private Moneda moneda;

    @PodamExclude
    @ManyToOne
    private Ocasion ocasion;

}
