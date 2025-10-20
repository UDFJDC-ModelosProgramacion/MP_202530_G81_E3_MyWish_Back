package co.edu.udistrital.mdp.back.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TiendaDetailDTO extends TiendaDTO {

    private List<UbicacionDTO> ubicaciones = new ArrayList<>();

    private List<RegaloDTO> regalos = new ArrayList<>();

    private List<ComentarioDTO> comentarios = new ArrayList<>();

    private List<FotoDTO> fotos = new ArrayList<>();
}
