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

    static final String MENSAJE_CATALOGO_NO_ENCONTRADO = "Catálogo no encontrado";
    static final String MENSAJE_TIENDA_NO_ENCONTRADA = "Tienda no encontrada";
    static final String MENSAJE_NOMBRE_VACIO = "El nombre no puede ser nulo o vacío.";
    static final String MENSAJE_DESCRIPCION_VACIA = "La descripción no puede ser nula o vacía.";
    static final String MENSAJE_NOMBRE_CATALOGO_DUPLICADO = "Ya existe un catálogo con ese nombre.";
    static final String MENSAJE_NOMBRE_EN_USO_OTRO = "El nombre ya está en uso por otro catálogo.";
    static final String MENSAJE_NO_DEJAR_TIENDAS_VACIAS = "No se puede dejar vacía la lista de tiendas si ya tenía elementos.";
    static final String MENSAJE_NO_ELIMINAR_TIENDAS_ASOCIADAS = "No se puede eliminar un catálogo que tiene tiendas asociadas.";
    static final String MENSAJE_TIENDA_NO_PERTENECE_CATALOGO = "La tienda no pertenece a este catálogo.";

    @Autowired
    private CatalogoTiendasRepository catalogoTiendasRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    // Crear catálogo con validaciones
    @Transactional
    public CatalogoTiendasEntity crearCatalogo(CatalogoTiendasEntity catalogo) {
        log.info("Crear catálogo con nombre '{}'", catalogo.getNombre());

        if (catalogo.getNombre() == null || catalogo.getNombre().isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_NOMBRE_VACIO);
        }

        if (catalogo.getDescripcion() == null || catalogo.getDescripcion().isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_DESCRIPCION_VACIA);
        }

        List<CatalogoTiendasEntity> existentes = catalogoTiendasRepository.findByNombre(catalogo.getNombre());
        if (!existentes.isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_NOMBRE_CATALOGO_DUPLICADO);
        }

        if (catalogo.getTiendas() == null) {
            catalogo.setTiendas(new ArrayList<>());
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
                .orElseThrow(() -> new IllegalArgumentException(MENSAJE_CATALOGO_NO_ENCONTRADO));

        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_NOMBRE_VACIO);
        }

        if (nuevaDescripcion == null || nuevaDescripcion.isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_DESCRIPCION_VACIA);
        }

        List<CatalogoTiendasEntity> otrosCatalogos = catalogoTiendasRepository.findByNombre(nuevoNombre);
        boolean existeOtro = otrosCatalogos.stream().anyMatch(c -> !c.getId().equals(catalogoId));
        if (existeOtro) {
            throw new IllegalArgumentException(MENSAJE_NOMBRE_EN_USO_OTRO);
        }

        if (nuevasTiendas == null) {
            nuevasTiendas = new ArrayList<>();
        }

        if (!catalogo.getTiendas().isEmpty() && nuevasTiendas.isEmpty()) {
            throw new IllegalArgumentException(MENSAJE_NO_DEJAR_TIENDAS_VACIAS);
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
                .orElseThrow(() -> new IllegalArgumentException(MENSAJE_CATALOGO_NO_ENCONTRADO));

        if (!catalogo.getTiendas().isEmpty()) {
            throw new IllegalStateException(MENSAJE_NO_ELIMINAR_TIENDAS_ASOCIADAS);
        }

        catalogoTiendasRepository.delete(catalogo);
    }

    // Eliminar tienda de un catálogo
    @Transactional
    public void eliminarTiendaDeCatalogo(Long catalogoId, Long tiendaId) {
        log.info("Eliminar tienda id {} del catálogo id {}", tiendaId, catalogoId);

        CatalogoTiendasEntity catalogo = catalogoTiendasRepository.findById(catalogoId)
                .orElseThrow(() -> new IllegalArgumentException(MENSAJE_CATALOGO_NO_ENCONTRADO));

        TiendaEntity tienda = tiendaRepository.findById(tiendaId)
                .orElseThrow(() -> new IllegalArgumentException(MENSAJE_TIENDA_NO_ENCONTRADA));

        if (!catalogo.getTiendas().contains(tienda)) {
            throw new IllegalArgumentException(MENSAJE_TIENDA_NO_PERTENECE_CATALOGO);
        }

        catalogo.getTiendas().remove(tienda);
        catalogoTiendasRepository.save(catalogo);
    }
}
