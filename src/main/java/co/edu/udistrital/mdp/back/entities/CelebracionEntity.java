package co.edu.udistrital.mdp.back.entities;

import java.util.Date;
import java.util.List;
import jakarta.persistence.OneToOne;
import lombok.Data;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class CelebracionEntity extends BaseEntity {
    
    private String nombre;
    private String descripcion;
    private String lugar;
    private String color;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private int cantidadInvitados;

    @PodamExclude
    @ManyToOne
    private CreadorEntity creador;

    @PodamExclude
    @ManyToMany
    private List<InvitadoEntity> invitados;

    @PodamExclude
    @OneToOne(mappedBy = "celebracion")
    private ListaRegalosEntity listaRegalos;

    @PodamExclude
    @OneToMany(mappedBy = "celebracion")
    private List<MensajeInvitacionEntity> mensajesInvitacion;




}
