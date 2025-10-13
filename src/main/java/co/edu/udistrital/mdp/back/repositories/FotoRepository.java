package co.edu.udistrital.mdp.back.repositories;

import co.edu.udistrital.mdp.back.entities.FotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FotoRepository extends JpaRepository<FotoEntity, Long> {
    Optional<FotoEntity> findByUrl(String url);
    List<FotoEntity> findByEsPrincipalTrue();
    List<FotoEntity> findByTipoArchivo(String tipoArchivo);
    List<FotoEntity> findByRegaloId(Long id);
    List<FotoEntity> findByRegalo_Id(Long regaloId);
    List<FotoEntity> findByListaRegalosId(Long id);
}