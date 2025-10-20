package co.edu.udistrital.mdp.back.dto;


import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ListaRegalosDetailDTO extends ListaRegalosDTO {

    private List<UsuarioDTO> invitados = new ArrayList<>();

    private List<RegaloDTO> regalos = new ArrayList<>();

    private List<FiltroRegaloDTO> filtrosRegalos = new ArrayList<>();

    private List<MensajeInvitacionDTO> mensajesInvitacion = new ArrayList<>();
}