package co.edu.udistrital.mdp.back.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CelebracionDetailDTO extends CelebracionDTO {

    private List<UsuarioDTO> invitados = new ArrayList<>();

    private List<MensajeInvitacionDTO> mensajesInvitacion = new ArrayList<>();
}