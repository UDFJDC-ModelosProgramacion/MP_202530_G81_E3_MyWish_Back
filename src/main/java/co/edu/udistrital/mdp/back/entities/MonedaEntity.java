package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonedaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    private String simbolo;

    // Relación con RegaloEntity (una moneda puede estar en muchos regalos)
    @OneToMany(mappedBy = "moneda")
    private List<RegaloEntity> regalos;
}
