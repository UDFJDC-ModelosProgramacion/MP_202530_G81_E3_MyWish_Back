package co.edu.udistrital.mdp.back.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TiendaService {

    @Autowired
    private TiendaRepository tiendaRepo;

    /**
     * Obtiene todas las tiendas.
     */
    public List<TiendaEntity> getAll() {
        log.info("Obteniendo todas las tiendas");
        return tiendaRepo.findAll();
    }

    /**
     * Obtiene una tienda por su ID.
     */
    public TiendaEntity getById(Long id) {
        log.info("Buscando tienda con id {}", id);
        return tiendaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la tienda con id: " + id));
    }

    /**
     * Crea una nueva tienda.
     * 
     * Reglas de negocio:
     *  - El nombre no puede ser nulo ni vacío.
     *  - La descripción no puede ser nula ni vacía.
     *  - El link debe ser una URL válida (comenzar con http o https).
     *  - No debe existir otra tienda con el mismo nombre.
     */
    public TiendaEntity create(TiendaEntity nueva) {
        log.info("Creando nueva tienda: {}", nueva.getNombre());
        validarTienda(nueva);

        // Evitar duplicados
        if (!tiendaRepo.findByNombreContainingIgnoreCase(nueva.getNombre()).isEmpty()) {
            throw new IllegalArgumentException("Ya existe una tienda con ese nombre");
        }

        return tiendaRepo.save(nueva);
    }

    /**
     * Actualiza una tienda existente.
     * 
     * Reglas de negocio:
     *  - Debe existir la tienda.
     *  - El nombre y la descripción deben ser válidos.
     *  - El link debe tener formato válido.
     */
    public TiendaEntity update(Long id, TiendaEntity nueva) {
        log.info("Actualizando tienda con id {}", id);

        TiendaEntity existente = tiendaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la tienda con id: " + id));

        validarTienda(nueva);

        // Evitar nombre duplicado (salvo que sea el mismo registro)
        List<TiendaEntity> duplicadas = tiendaRepo.findByNombreContainingIgnoreCase(nueva.getNombre());
        if (!duplicadas.isEmpty() && !duplicadas.get(0).getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe otra tienda con el mismo nombre");
        }

        existente.setNombre(nueva.getNombre());
        existente.setDescripcion(nueva.getDescripcion());
        existente.setLink(nueva.getLink());
        existente.setCatalogoTiendas(nueva.getCatalogoTiendas());
        existente.setUbicaciones(nueva.getUbicaciones());
        existente.setComentarios(nueva.getComentarios());
        existente.setRegalos(nueva.getRegalos());
        existente.setFotos(nueva.getFotos());

        return tiendaRepo.save(existente);
    }

    /**
     * Elimina una tienda por ID.
     * 
     * Regla de negocio:
     *  - No se puede eliminar una tienda con regalos asociados.
     */
    public void delete(Long id) {
        log.info("Eliminando tienda con id {}", id);
        TiendaEntity tienda = tiendaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la tienda con id: " + id));

        if (tienda.getRegalos() != null && !tienda.getRegalos().isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar una tienda con regalos asociados");
        }

        tiendaRepo.delete(tienda);
    }

    /**
     * Busca tiendas por nombre.
     */
    public List<TiendaEntity> findByNombre(String nombre) {
        log.info("Buscando tiendas con nombre que contiene: {}", nombre);
        return tiendaRepo.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Busca tiendas por país.
     */
    public List<TiendaEntity> findByPais(String pais) {
        log.info("Buscando tiendas por país: {}", pais);
        return tiendaRepo.findByUbicaciones_Pais(pais);
    }

    /**
     * Busca tiendas por ciudad.
     */
    public List<TiendaEntity> findByCiudad(String ciudad) {
        log.info("Buscando tiendas por ciudad: {}", ciudad);
        return tiendaRepo.findByUbicaciones_Ciudad(ciudad);
    }

    /**
     * Valida las reglas de negocio de una tienda.
     */
    private void validarTienda(TiendaEntity tienda) {
        if (tienda == null) {
            throw new IllegalArgumentException("La tienda no puede ser nula");
        }

        if (tienda.getNombre() == null || tienda.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la tienda es obligatorio");
        }

        if (tienda.getDescripcion() == null || tienda.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la tienda es obligatoria");
        }

        if (tienda.getLink() != null && !tienda.getLink().isEmpty()) {
            String link = tienda.getLink().toLowerCase();
            if (!(link.startsWith("http://") || link.startsWith("https://"))) {
                throw new IllegalArgumentException("El link de la tienda debe ser una URL válida (debe iniciar con http o https)");
            }
        }
    }
}
