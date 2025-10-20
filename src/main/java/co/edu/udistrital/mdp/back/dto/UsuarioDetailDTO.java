package co.edu.udistrital.mdp.back.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UsuarioDetailDTO extends UsuarioDTO {
    private List<ListaRegalosDTO> listasCreadas = new ArrayList<>();
    private List<ListaRegalosDTO> listasInvitadoEn = new ArrayList<>();
    private List<CelebracionDTO> celebracionesOrganizadas = new ArrayList<>();
    private List<CelebracionDTO> celebsInvitadoEn = new ArrayList<>();
    private List<MensajeInvitacionDTO> invitacionesRecibidas = new ArrayList<>();
    private List<MensajeInvitacionDTO> invitacionesEnviadas = new ArrayList<>();
    private List<ComentarioDTO> comentarios = new ArrayList<>();
}
