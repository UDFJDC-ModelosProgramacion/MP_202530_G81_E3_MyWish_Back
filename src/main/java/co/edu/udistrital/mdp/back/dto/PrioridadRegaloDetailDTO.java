package co.edu.udistrital.mdp.back.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class PrioridadRegaloDetailDTO extends PrioridadRegaloDTO {
    
    private List<RegaloDTO> regalos = new ArrayList<>();
}