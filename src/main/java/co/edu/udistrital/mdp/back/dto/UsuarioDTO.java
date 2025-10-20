package co.edu.udistrital.mdp.back.dto;

import java.sql.Date;

import lombok.Data;

import lombok.Getter;

import lombok.Setter;

@Data
public class UsuarioDTO {
    private String correo;
    private String nombre;
    private Date fechaNacimiento;
}
