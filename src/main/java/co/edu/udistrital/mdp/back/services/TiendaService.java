package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;
import lombok.extern.slf4j.Slf4j;




@Slf4j
@Service
public class TiendaService {

    @Autowired
    private TiendaRepository tiendaRepo;

    public List<TiendaEntity> getAll() {
        log.info("Obteniendo todas las tiendas");
        return tiendaRepo.findAll();
    }

    public TiendaEntity getById(Long id) {
        log.info("Buscando tienda con id {}", id);
        return tiendaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la tienda con id: " + id));
    }

    public TiendaEntity create(TiendaEntity nueva) {
        log.info("Creando nueva tienda: {}", nueva.getNombre());
        validarTienda(nueva);

        // Evitar duplicados
        if (!tiendaRepo.findByNombreContainingIgnoreCase(nueva.getNombre()).isEmpty()) {
            throw new IllegalArgumentException("Ya existe una tienda con ese nombre");
        }

        return tiendaRepo.save(nueva);
    }

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

    public void delete(Long id) {
        log.info("Eliminando tienda con id {}", id);
        TiendaEntity tienda = tiendaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la tienda con id: " + id));

        if (tienda.getRegalos() != null && !tienda.getRegalos().isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar una tienda con regalos asociados");
        }

        tiendaRepo.delete(tienda);
    }

    public List<TiendaEntity> findByNombre(String nombre) {
        log.info("Buscando tiendas con nombre que contiene: {}", nombre);
        return tiendaRepo.findByNombreContainingIgnoreCase(nombre);
    }

    public List<TiendaEntity> findByPais(String pais) {
        log.info("Buscando tiendas por país: {}", pais);
        return tiendaRepo.findByUbicaciones_Pais(pais);
    }

    public List<TiendaEntity> findByCiudad(String ciudad) {
        log.info("Buscando tiendas por ciudad: {}", ciudad);
        return tiendaRepo.findByUbicaciones_Ciudad(ciudad);
    }

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
