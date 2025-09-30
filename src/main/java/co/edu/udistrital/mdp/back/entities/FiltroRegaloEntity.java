package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entidad que representa un filtro para regalos.
 * Contiene un criterio de filtro y su valor asociado.
 */
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FiltroRegaloEntity {

    /**
     * Identificador único del filtro generado automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Criterio del filtro (por ejemplo, categoría, prioridad, etc.).
     */
    private String criterio;

    /**
     * Valor asociado al criterio para filtrar los regalos.
     */
    private String valor;
}
