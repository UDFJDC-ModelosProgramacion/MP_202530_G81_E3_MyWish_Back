package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {
        "listasCreadas", "listasInvitado",
        "celebracionesOrganizadas", "celebracionesInvitado",
        "comentarios", "mensajesEnviados", "mensajesRecibidos"
})
public class UsuarioEntity extends BaseEntity {

    private String correo;
    private String nombre;

    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    // --- Relaciones ---

    /** Usuario crea varias listas de regalos */
    @PodamExclude
    @OneToMany(mappedBy = "creador")
    private List<ListaRegalosEntity> listasCreadas;

    /** Usuario es invitado a varias listas de regalos */
    @PodamExclude
    @ManyToMany(mappedBy = "invitados")
    private List<ListaRegalosEntity> listasInvitado;

    /** Usuario organiza varias celebraciones */
    @PodamExclude
    @OneToMany(mappedBy = "organizador")
    private List<CelebracionEntity> celebracionesOrganizadas;

    /** Usuario es invitado a varias celebraciones */
    @PodamExclude
    @ManyToMany(mappedBy = "invitados")
    private List<CelebracionEntity> celebracionesInvitado;

    /** Usuario escribe varios comentarios */
    @PodamExclude
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComentarioEntity> comentarios = new ArrayList<>();

    /** Mensajes enviados por el usuario (como remitente) */
    @PodamExclude
    @OneToMany(mappedBy = "remitente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MensajeInvitacionEntity> mensajesEnviados = new ArrayList<>();

    /** Mensajes recibidos por el usuario (como destinatario) */
    @PodamExclude
    @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MensajeInvitacionEntity> mensajesRecibidos = new ArrayList<>();
}
