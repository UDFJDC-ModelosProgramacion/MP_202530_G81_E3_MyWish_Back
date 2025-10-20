package co.edu.udistrital.mdp.back.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.CatalogoTiendasEntity;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.CatalogoTiendasRepository;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CatalogoTiendasService {

    @Autowired
    private CatalogoTiendasRepository catalogoTiendasRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    // Crear catálogo con validaciones
    @Transactional
    public CatalogoTiendasEntity crearCatalogo(CatalogoTiendasEntity catalogo) {
        log.info("Crear catálogo con nombre '{}'", catalogo.getNombre());

        if (catalogo.getNombre() == null || catalogo.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del catálogo no puede ser nulo o vacío.");
        }

        if (catalogo.getDescripcion() == null || catalogo.getDescripcion().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede ser nula o vacía.");
        }

        List<CatalogoTiendasEntity> existentes = catalogoTiendasRepository.findByNombre(catalogo.getNombre());
        if (!existentes.isEmpty()) {
            throw new IllegalArgumentException("Ya existe un catálogo con ese nombre.");
        }

        // Inicializar lista de tiendas si es null
        if (catalogo.getTiendas() == null) {
            catalogo.setTiendas(new ArrayList<>());
        }

        return catalogoTiendasRepository.save(catalogo);
    }

    // Actualizar catálogo con validaciones
    @Transactional
    public CatalogoTiendasEntity actualizarCatalogo(Long catalogoId, String nuevoNombre, String nuevaDescripcion, List<TiendaEntity> nuevasTiendas) {
        log.info("Actualizar catálogo id {}", catalogoId);

        CatalogoTiendasEntity catalogo = catalogoTiendasRepository.findById(catalogoId)
                .orElseThrow(() -> new IllegalArgumentException("Catálogo no encontrado"));

        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }

        if (nuevaDescripcion == null || nuevaDescripcion.isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede ser nula o vacía.");
        }

        List<CatalogoTiendasEntity> otrosCatalogos = catalogoTiendasRepository.findByNombre(nuevoNombre);
        boolean existeOtro = otrosCatalogos.stream().anyMatch(c -> !c.getId().equals(catalogoId));
        if (existeOtro) {
            throw new IllegalArgumentException("El nombre ya está en uso por otro catálogo.");
        }

        // Inicializar lista de tiendas si es null
        if (nuevasTiendas == null) {
            nuevasTiendas = new ArrayList<>();
        }

        // Regla: No se puede dejar vacía la lista de tiendas si ya tenía elementos
        if (!catalogo.getTiendas().isEmpty() && nuevasTiendas.isEmpty()) {
            throw new IllegalArgumentException("No se puede dejar vacía la lista de tiendas si ya tenía elementos.");
        }

        catalogo.setNombre(nuevoNombre);
        catalogo.setDescripcion(nuevaDescripcion);
        catalogo.setTiendas(nuevasTiendas);

        return catalogoTiendasRepository.save(catalogo);
    }

    // Eliminar catálogo con reglas
    @Transactional
    public void eliminarCatalogo(Long catalogoId) {
        log.info("Eliminar catálogo id {}", catalogoId);

        CatalogoTiendasEntity catalogo = catalogoTiendasRepository.findById(catalogoId)
                .orElseThrow(() -> new IllegalArgumentException("Catálogo no encontrado"));

        if (!catalogo.getTiendas().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un catálogo que tiene tiendas asociadas.");
        }

        catalogoTiendasRepository.delete(catalogo);
    }

    // Eliminar tienda de un catálogo
    @Transactional
    public void eliminarTiendaDeCatalogo(Long catalogoId, Long tiendaId) {
        log.info("Eliminar tienda id {} del catálogo id {}", tiendaId, catalogoId);

        CatalogoTiendasEntity catalogo = catalogoTiendasRepository.findById(catalogoId)
                .orElseThrow(() -> new IllegalArgumentException("Catálogo no encontrado"));

        TiendaEntity tienda = tiendaRepository.findById(tiendaId)
                .orElseThrow(() -> new IllegalArgumentException("Tienda no encontrada"));

        if (!catalogo.getTiendas().contains(tienda)) {
            throw new IllegalArgumentException("La tienda no pertenece a este catálogo.");
        }

        catalogo.getTiendas().remove(tienda);
        catalogoTiendasRepository.save(catalogo);
    }
}
