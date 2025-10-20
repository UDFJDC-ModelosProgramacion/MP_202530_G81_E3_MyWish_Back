package co.edu.udistrital.mdp.back.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CelebracionDTO {

    private Long id;

    private String nombre;

    private String descripcion;

    private String lugar;

    private String color;

    private LocalDate fecha;

    private int cantidadInvitados;

    private UsuarioDTO organizador;

    private ListaRegalosDTO listaRegalos;
}
