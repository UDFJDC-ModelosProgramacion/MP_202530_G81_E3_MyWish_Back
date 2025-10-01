package co.edu.udistrital.mdp.back.repositories;

import co.edu.udistrital.mdp.back.entities.CategoriaRegaloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRegaloRepository extends JpaRepository<CategoriaRegaloEntity, Long> {

    // Encontrar por nombre exacto
    Optional<CategoriaRegaloEntity> findByNombre(String nombre);
    
    // Encontrar categorías por defecto
    List<CategoriaRegaloEntity> findByEsPorDefectoTrue();
    
    // Verificar si existe por nombre
    boolean existsByNombre(String nombre);
    
    // Encontrar por nombre ignorando mayúsculas/minúsculas
    Optional<CategoriaRegaloEntity> findByNombreIgnoreCase(String nombre);
    
    // Buscar categorías que contengan un texto en el nombre
    List<CategoriaRegaloEntity> findByNombreContainingIgnoreCase(String nombre);
    
    // Encontrar por color
    List<CategoriaRegaloEntity> findByColor(String color);
}