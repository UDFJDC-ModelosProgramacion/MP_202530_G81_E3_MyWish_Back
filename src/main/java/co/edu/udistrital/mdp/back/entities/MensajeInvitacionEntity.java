
package co.edu.udistrital.mdp.back.entities;

import lombok.Data;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import uk.co.jemos.podam.common.PodamExclude;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Data
@Entity
public class MensajeInvitacionEntity extends BaseEntity {

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;

    private String mensaje;

    @PodamExclude
    @ManyToOne(optional = true)
    private CelebracionEntity celebracion;

    @PodamExclude
    @ManyToOne(optional = true)
    private ListaRegalosEntity listaRegalos;

    @PodamExclude
    @ManyToOne
    private UsuarioEntity remitente; // 🧑‍💼 Quien envía la invitación (se obtiene del creador de la celebración)

    @PodamExclude
    @ManyToOne
    private UsuarioEntity destinatario; // 🧍 Usuario al que se le envía el mensaje
}
