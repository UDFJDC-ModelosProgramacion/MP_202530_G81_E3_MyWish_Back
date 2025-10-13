package co.edu.udistrital.mdp.back.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.back.entities.UbicacionEntity;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.UbicacionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepo;

    /**
     * Obtiene todas las ubicaciones.
     */
    public List<UbicacionEntity> getAll() {
        log.info("Obteniendo todas las ubicaciones");
        return ubicacionRepo.findAll();
    }

    /**
     * Obtiene una ubicación por su ID.
     */
    public UbicacionEntity getById(Long id) {
        log.info("Buscando ubicación con id {}", id);
        return ubicacionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la ubicación con id: " + id));
    }

    /**
     * Crea una nueva ubicación.
     */
    public UbicacionEntity create(UbicacionEntity nueva) {
        log.info("Creando nueva ubicación en ciudad: {}, país: {}", nueva.getCiudad(), nueva.getPais());
        return ubicacionRepo.save(nueva);
    }

    /**
     * Actualiza una ubicación existente.
     */
    public UbicacionEntity update(Long id, UbicacionEntity nueva) {
        log.info("Actualizando ubicación con id {}", id);
        UbicacionEntity existente = getById(id);

        existente.setDireccion(nueva.getDireccion());
        existente.setCiudad(nueva.getCiudad());
        existente.setPais(nueva.getPais());
        existente.setTienda(nueva.getTienda());

        return ubicacionRepo.save(existente);
    }

    /**
     * Elimina una ubicación por ID.
     */
    public void delete(Long id) {
        log.info("Eliminando ubicación con id {}", id);
        UbicacionEntity ubicacion = getById(id);
        ubicacionRepo.delete(ubicacion);
    }

    /**
     * Busca ubicaciones por ciudad.
     */
    public List<UbicacionEntity> findByCiudad(String ciudad) {
        log.info("Buscando ubicaciones en la ciudad: {}", ciudad);
        return ubicacionRepo.findByCiudad(ciudad);
    }

    /**
     * Busca ubicaciones por país.
     */
    public List<UbicacionEntity> findByPais(String pais) {
        log.info("Buscando ubicaciones en el país: {}", pais);
        return ubicacionRepo.findByPais(pais);
    }

    /**
     * Busca ubicaciones por dirección.
     */
    public List<UbicacionEntity> findByDireccion(String direccion) {
        log.info("Buscando ubicaciones con dirección: {}", direccion);
        return ubicacionRepo.findByDireccion(direccion);
    }

    /**
     * Busca ubicaciones por tienda.
     */
    public List<UbicacionEntity> findByTienda(TiendaEntity tienda) {
        log.info("Buscando ubicaciones de la tienda: {}", tienda.getNombre());
        return ubicacionRepo.findByTienda(tienda);
    }

    /**
     * Busca ubicaciones por ID de tienda.
     */
    public List<UbicacionEntity> findByTiendaId(Long tiendaId) {
        log.info("Buscando ubicaciones de la tienda con id: {}", tiendaId);
        return ubicacionRepo.findByTiendaId(tiendaId);
    }
}
    