package co.edu.udistrital.mdp.back.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.back.entities.UbicacionEntity;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.UbicacionRepository;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepo;

    @Autowired
    private TiendaRepository tiendaRepo;

    
    public List<UbicacionEntity> getAll() {
        log.info("Obteniendo todas las ubicaciones");
        return ubicacionRepo.findAll();
    }

    public UbicacionEntity getById(Long id) {
        log.info("Buscando ubicación con id {}", id);
        return ubicacionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la ubicación con id: " + id));
    }


    public UbicacionEntity create(UbicacionEntity nueva) {
        log.info("Creando nueva ubicación en ciudad: {}, país: {}", nueva.getCiudad(), nueva.getPais());

        if (nueva.getDireccion() == null || nueva.getDireccion().isBlank()) {
            throw new IllegalArgumentException("La dirección de la ubicación no puede estar vacía.");
        }

        if (nueva.getCiudad() == null || nueva.getCiudad().isBlank()) {
            throw new IllegalArgumentException("La ciudad de la ubicación no puede estar vacía.");
        }

        if (nueva.getPais() == null || nueva.getPais().isBlank()) {
            throw new IllegalArgumentException("El país de la ubicación no puede estar vacío.");
        }

        if (nueva.getTienda() == null || nueva.getTienda().getId() == null) {
            throw new IllegalArgumentException("La ubicación debe estar asociada a una tienda válida.");
        }

        // Validar que la tienda exista realmente en la base de datos
        tiendaRepo.findById(nueva.getTienda().getId())
                .orElseThrow(() -> new IllegalArgumentException("La tienda asociada no existe."));

        return ubicacionRepo.save(nueva);
    }


    public UbicacionEntity update(Long id, UbicacionEntity nueva) {
        log.info("Actualizando ubicación con id {}", id);

        UbicacionEntity existente = ubicacionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la ubicación con id: " + id));

        // Verificar que no se cambie la tienda asociada
        if (nueva.getTienda() != null && existente.getTienda() != null
                && !nueva.getTienda().getId().equals(existente.getTienda().getId())) {
            throw new IllegalArgumentException("No se puede cambiar la tienda asociada a la ubicación.");
        }

        // Validar campos obligatorios
        if (nueva.getDireccion() == null || nueva.getDireccion().isBlank()) {
            throw new IllegalArgumentException("La dirección de la ubicación no puede estar vacía.");
        }

        if (nueva.getCiudad() == null || nueva.getCiudad().isBlank()) {
            throw new IllegalArgumentException("La ciudad de la ubicación no puede estar vacía.");
        }

        if (nueva.getPais() == null || nueva.getPais().isBlank()) {
            throw new IllegalArgumentException("El país de la ubicación no puede estar vacío.");
        }

        existente.setDireccion(nueva.getDireccion());
        existente.setCiudad(nueva.getCiudad());
        existente.setPais(nueva.getPais());

        return ubicacionRepo.save(existente);
    }

    
    public void delete(Long id) {
        log.info("Eliminando ubicación con id {}", id);
        UbicacionEntity ubicacion = ubicacionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la ubicación con id: " + id));
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
