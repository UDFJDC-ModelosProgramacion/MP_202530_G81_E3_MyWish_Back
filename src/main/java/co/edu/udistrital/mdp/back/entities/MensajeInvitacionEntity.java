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
    @ManyToOne
    private CelebracionEntity celebracion;

}
