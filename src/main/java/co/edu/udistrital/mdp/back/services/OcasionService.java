package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import co.edu.udistrital.mdp.back.repositories.OcasionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class OcasionService {

    @Autowired
    private OcasionRepository ocasionRepository;

    // Crear ocasión con validaciones
    @Transactional
    public OcasionEntity createOcasion(OcasionEntity ocasion) {
        log.info("Creando ocasión con nombre '{}'", ocasion.getNombre());

        if (ocasion.getNombre() == null || ocasion.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la ocasión no puede ser nulo o vacío.");
        }

        List<OcasionEntity> existentes = ocasionRepository.findAll()
                .stream()
                .filter(o -> o.getNombre().equalsIgnoreCase(ocasion.getNombre()))
                .toList();
        if (!existentes.isEmpty()) {
            throw new IllegalArgumentException("Ya existe una ocasión con ese nombre.");
        }

        return ocasionRepository.save(ocasion);
    }

    public List<OcasionEntity> getOcasiones() {
        return ocasionRepository.findAll();
    }

    public OcasionEntity getOcasion(Long id) {
        return ocasionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ocasión no encontrada."));
    }

    // Actualizar ocasión con validaciones
    @Transactional
    public OcasionEntity updateOcasion(Long id, String nuevoNombre) {
        OcasionEntity ocasion = getOcasion(id);

        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser vacío o nulo.");
        }

        boolean existe = ocasionRepository.findAll().stream()
                .anyMatch(o -> o.getNombre().equalsIgnoreCase(nuevoNombre) && !o.getId().equals(id));
        if (existe) {
            throw new IllegalArgumentException("Ya existe otra ocasión con ese nombre.");
        }

        ocasion.setNombre(nuevoNombre);
        return ocasionRepository.save(ocasion);
    }

    @Transactional
    public void deleteOcasion(Long id) {
        OcasionEntity ocasion = getOcasion(id);
        ocasionRepository.delete(ocasion);
    }
}   