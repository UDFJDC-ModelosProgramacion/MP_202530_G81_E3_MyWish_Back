package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entidad que representa un filtro para regalos.
 * Contiene un criterio de filtro y su valor asociado.
 */
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FiltroRegaloEntity extends BaseEntity {

    /**
     * Criterio del filtro (por ejemplo, categoría, prioridad, etc.).
     */
    private String criterio;

    /**
     * Valor asociado al criterio para filtrar los regalos.
     */
    private String valor;

    /**
     * Asociación de muchos filtros a una lista de regalos.
     */
    @ManyToOne
    private ListaRegalosEntity listaRegalos;
}
