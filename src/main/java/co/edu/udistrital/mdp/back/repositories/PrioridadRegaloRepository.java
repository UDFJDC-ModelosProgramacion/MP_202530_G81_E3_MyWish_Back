package co.edu.udistrital.mdp.back.repositories;

import co.edu.udistrital.mdp.back.entities.PrioridadRegaloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrioridadRegaloRepository extends JpaRepository<PrioridadRegaloEntity, Long> {

    // Encontrar por nombre exacto
    Optional<PrioridadRegaloEntity> findByNombre(String nombre);
    
    // Encontrar por nivel 
    List<PrioridadRegaloEntity> findByNivel(Integer nivel);
    
    // Encontrar prioridades por defecto
    List<PrioridadRegaloEntity> findByEsPorDefectoTrue();
    
    // Verificar si existe por nombre
    boolean existsByNombre(String nombre);
    
    // Encontrar por nombre ignorando mayúsculas/minúsculas
    Optional<PrioridadRegaloEntity> findByNombreIgnoreCase(String nombre);
    
    // Buscar prioridades que contengan un texto en el nombre
    List<PrioridadRegaloEntity> findByNombreContainingIgnoreCase(String nombre);
    
    // Encontrar por nivel mayor o igual
    List<PrioridadRegaloEntity> findByNivelGreaterThanEqual(Integer nivelMinimo);
    
    // Ordenar por nivel ascendente
    List<PrioridadRegaloEntity> findAllByOrderByNivelAsc();
}
