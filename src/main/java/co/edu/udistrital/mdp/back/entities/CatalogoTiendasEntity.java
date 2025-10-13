package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.ArrayList;

/**
 * Entidad que representa un catálogo de tiendas.
 * Un catálogo contiene un nombre, descripción y una lista de tiendas asociadas.
 */
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CatalogoTiendasEntity extends BaseEntity {

    /**
     * Nombre descriptivo del catálogo de tiendas. 
     */
    private String nombre;

    /**
     * Breve descripción que detalla el catálogo.
     */
    private String descripcion;

    /**
     * Lista de tiendas que pertenecen a este catálogo.
     * Relación de uno a muchos con TiendaEntity.
     * CascadeType.ALL propaga las operaciones a las tiendas asociadas.
     * orphanRemoval=true elimina tiendas huérfanas si se eliminan de la lista.
     */
    @OneToMany(mappedBy = "catalogoTiendas", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TiendaEntity> tiendas = new ArrayList<>();
}
