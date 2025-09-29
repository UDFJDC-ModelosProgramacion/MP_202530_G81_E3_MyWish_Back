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
public class OcasionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // Relación con RegaloEntity (una ocasión puede tener muchos regalos)
    @OneToMany(mappedBy = "ocasion")
    private List<RegaloEntity> regalos;
}


