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

    private String codigo;

    private String simbolo;

    // Relación con ListaRegalosEntity (una moneda puede estar en muchas listas)
    @OneToMany(mappedBy = "moneda")
    private List<ListaRegalosEntity> listasRegalos;
}

