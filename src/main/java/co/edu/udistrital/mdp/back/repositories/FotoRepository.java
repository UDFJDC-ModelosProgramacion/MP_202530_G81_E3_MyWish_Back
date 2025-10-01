package co.edu.udistrital.mdp.back.repositories;

import co.edu.udistrital.mdp.back.entities.FotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FotoRepository extends JpaRepository<FotoEntity, Long> {

    // Encontrar fotos por entidad relacionada
    List<FotoEntity> findByEntidadRelacionada(String entidadRelacionada);
    
    // Encontrar fotos por ID de entidad relacionada
    List<FotoEntity> findByIdEntidadRelacionada(Long idEntidadRelacionada);
    
    // Encontrar fotos principales
    List<FotoEntity> findByEsPrincipalTrue();
    
    // Encontrar por tipo de archivo
    List<FotoEntity> findByTipoArchivo(String tipoArchivo);
    
    // Encontrar fotos por tamaño menor o igual a
    List<FotoEntity> findByTamanioBytesLessThanEqual(Long tamanioMaximo);
    
    // Consulta personalizada para encontrar fotos por entidad específica
    @Query("SELECT f FROM FotoEntity f WHERE f.entidadRelacionada = :entidad AND f.idEntidadRelacionada = :id")
    List<FotoEntity> findByEntidadAndId(
        @Param("entidad") String entidad, 
        @Param("id") Long id
    );
    
    // Encontrar fotos por URL
    Optional<FotoEntity> findByUrl(String url);
}
