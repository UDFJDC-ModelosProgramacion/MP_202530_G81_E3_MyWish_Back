package co.edu.udistrital.mdp.back.repositories;

import co.edu.udistrital.mdp.back.entities.EstadoCompraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadoCompraRepository extends JpaRepository<EstadoCompraEntity, Long> {

    // Encontrar por nombre exacto
    Optional<EstadoCompraEntity> findByNombre(String nombre);
    
    // Encontrar estados por defecto
    List<EstadoCompraEntity> findByEsPorDefectoTrue();
     
    // Verificar si existe por nombre (para validaciones)
    boolean existsByNombre(String nombre);
    
    // Encontrar por nombre ignorando mayúsculas/minúsculas
    Optional<EstadoCompraEntity> findByNombreIgnoreCase(String nombre);
    
    // Buscar estados que contengan un texto en el nombre
    List<EstadoCompraEntity> findByNombreContainingIgnoreCase(String nombre);
}