package co.edu.udistrital.mdp.back.dto;


import java.util.Date;

import lombok.Data;

@Data
public class MensajeInvitacionDTO {

    private Long id;

    private Date fechaEnvio;

    private String mensaje;

    private CelebracionDTO celebracion;

    private ListaRegalosDTO listaRegalos;

    private UsuarioDTO remitente;

    private UsuarioDTO destinatario;
}
