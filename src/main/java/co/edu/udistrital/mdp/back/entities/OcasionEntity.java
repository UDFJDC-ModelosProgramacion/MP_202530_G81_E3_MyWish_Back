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
public class OcasionEntity extends BaseEntity {

    private String nombre;

    // Relación con ListaRegalosEntity (una ocasión puede estar en muchas listas)
    @OneToMany(mappedBy = "ocasion")
    private List<ListaRegalosEntity> listasRegalos;
} 



