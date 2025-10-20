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

    private static final String UBICACION_NO_ENCONTRADA = "No se encontró la ubicación con id: ";
    private static final String TIENDA_NO_EXISTE = "La tienda asociada no existe.";
    private static final String DIRECCION_VACIA = "La dirección de la ubicación no puede estar vacía.";
    private static final String CIUDAD_VACIA = "La ciudad de la ubicación no puede estar vacía.";
    private static final String PAIS_VACIO = "El país de la ubicación no puede estar vacío.";
    private static final String TIENDA_INVALIDA = "La ubicación debe estar asociada a una tienda válida.";
    private static final String NO_CAMBIAR_TIENDA = "No se puede cambiar la tienda asociada a la ubicación.";

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
                .orElseThrow(() -> new IllegalArgumentException(UBICACION_NO_ENCONTRADA + id));
    }

    public UbicacionEntity create(UbicacionEntity nueva) {
        log.info("Creando nueva ubicación en ciudad: {}, país: {}", nueva.getCiudad(), nueva.getPais());

        if (nueva.getDireccion() == null || nueva.getDireccion().isBlank()) {
            throw new IllegalArgumentException(DIRECCION_VACIA);
        }
        if (nueva.getCiudad() == null || nueva.getCiudad().isBlank()) {
            throw new IllegalArgumentException(CIUDAD_VACIA);
        }
        if (nueva.getPais() == null || nueva.getPais().isBlank()) {
            throw new IllegalArgumentException(PAIS_VACIO);
        }
        if (nueva.getTienda() == null || nueva.getTienda().getId() == null) {
            throw new IllegalArgumentException(TIENDA_INVALIDA);
        }

        tiendaRepo.findById(nueva.getTienda().getId())
                .orElseThrow(() -> new IllegalArgumentException(TIENDA_NO_EXISTE));

        return ubicacionRepo.save(nueva);
    }

    public UbicacionEntity update(Long id, UbicacionEntity nueva) {
        log.info("Actualizando ubicación con id {}", id);

        UbicacionEntity existente = ubicacionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(UBICACION_NO_ENCONTRADA + id));

        if (nueva.getTienda() != null && existente.getTienda() != null
                && !nueva.getTienda().getId().equals(existente.getTienda().getId())) {
            throw new IllegalArgumentException(NO_CAMBIAR_TIENDA);
        }

        if (nueva.getDireccion() == null || nueva.getDireccion().isBlank()) {
            throw new IllegalArgumentException(DIRECCION_VACIA);
        }
        if (nueva.getCiudad() == null || nueva.getCiudad().isBlank()) {
            throw new IllegalArgumentException(CIUDAD_VACIA);
        }
        if (nueva.getPais() == null || nueva.getPais().isBlank()) {
            throw new IllegalArgumentException(PAIS_VACIO);
        }

        existente.setDireccion(nueva.getDireccion());
        existente.setCiudad(nueva.getCiudad());
        existente.setPais(nueva.getPais());

        return ubicacionRepo.save(existente);
    }

    public void delete(Long id) {
        log.info("Eliminando ubicación con id {}", id);
        UbicacionEntity ubicacion = ubicacionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(UBICACION_NO_ENCONTRADA + id));
        ubicacionRepo.delete(ubicacion);
    }

    public List<UbicacionEntity> findByCiudad(String ciudad) {
        log.info("Buscando ubicaciones en la ciudad: {}", ciudad);
        return ubicacionRepo.findByCiudad(ciudad);
    }

    public List<UbicacionEntity> findByPais(String pais) {
        log.info("Buscando ubicaciones en el país: {}", pais);
        return ubicacionRepo.findByPais(pais);
    }

    public List<UbicacionEntity> findByDireccion(String direccion) {
        log.info("Buscando ubicaciones con dirección: {}", direccion);
        return ubicacionRepo.findByDireccion(direccion);
    }

    public List<UbicacionEntity> findByTienda(TiendaEntity tienda) {
        log.info("Buscando ubicaciones de la tienda: {}", tienda.getNombre());
        return ubicacionRepo.findByTienda(tienda);
    }

    public List<UbicacionEntity> findByTiendaId(Long tiendaId) {
        log.info("Buscando ubicaciones de la tienda con id: {}", tiendaId);
        return ubicacionRepo.findByTiendaId(tiendaId);
    }
}
