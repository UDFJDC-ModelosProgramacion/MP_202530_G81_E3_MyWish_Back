package co.edu.udistrital.mdp.back.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String correo;
    private String nombre;
    private Date fechaNacimiento;
}
