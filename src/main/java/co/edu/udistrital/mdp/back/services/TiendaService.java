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
     */
    public TiendaEntity create(TiendaEntity nueva) {
        log.info("Creando nueva tienda: {}", nueva.getNombre());
        return tiendaRepo.save(nueva);
    }

    /**
     * Actualiza una tienda existente.
     */
    public TiendaEntity update(Long id, TiendaEntity nueva) {
        log.info("Actualizando tienda con id {}", id);
        TiendaEntity existente = getById(id);

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
     */
    public void delete(Long id) {
        log.info("Eliminando tienda con id {}", id);
        TiendaEntity tienda = getById(id);
        tiendaRepo.delete(tienda);
    }

    /**
     * Busca tiendas por nombre.
     */
    public List<TiendaEntity> findByNombre(String nombre) {
        return tiendaRepo.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Busca tiendas por país o ciudad.
     */
    public List<TiendaEntity> findByPais(String pais) {
        return tiendaRepo.findByUbicaciones_Pais(pais);
    }

    public List<TiendaEntity> findByCiudad(String ciudad) {
        return tiendaRepo.findByUbicaciones_Ciudad(ciudad);
    }
}
