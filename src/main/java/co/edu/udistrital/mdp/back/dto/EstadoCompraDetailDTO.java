package co.edu.udistrital.mdp.back.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class EstadoCompraDetailDTO extends EstadoCompraDTO {
    
    private List<RegaloDTO> regalos = new ArrayList<>();
}