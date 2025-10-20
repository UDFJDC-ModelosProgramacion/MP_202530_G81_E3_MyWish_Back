package co.edu.udistrital.mdp.back.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ListaRegalosDTO {

    private Long id;

    private String nombre;

    private String descripcion;

    private String color;

    private Date fecha;

    private UsuarioDTO creador;

    private CelebracionDTO celebracion;

    private FotoDTO foto;

    private MonedaDTO moneda;

    private OcasionDTO ocasion;
}