package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.MonedaEntity;
import co.edu.udistrital.mdp.back.repositories.MonedaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class MonedaService {

    @Autowired
    private MonedaRepository monedaRepository;

    @Transactional
    public MonedaEntity createMoneda(MonedaEntity moneda) {
        log.info("Creando moneda con código '{}'", moneda.getCodigo());

        if (moneda.getCodigo() == null || moneda.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El código de la moneda no puede ser nulo o vacío.");
        }

        if (moneda.getSimbolo() == null || moneda.getSimbolo().isBlank()) {
            throw new IllegalArgumentException("El símbolo de la moneda no puede ser nulo o vacío.");
        }

        boolean existe = monedaRepository.findAll().stream()
                .anyMatch(m -> m.getCodigo().equalsIgnoreCase(moneda.getCodigo()));
        if (existe) {
            throw new IllegalArgumentException("Ya existe una moneda con ese código.");
        }

        return monedaRepository.save(moneda);
    }

    public List<MonedaEntity> getMonedas() {
        return monedaRepository.findAll();
    }

    public MonedaEntity getMoneda(Long id) {
        return monedaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Moneda no encontrada."));
    }

    @Transactional
    public MonedaEntity updateMoneda(Long id, String nuevoCodigo, String nuevoSimbolo) {
        MonedaEntity moneda = getMoneda(id);

        if (nuevoCodigo == null || nuevoCodigo.isBlank()) {
            throw new IllegalArgumentException("El código no puede ser vacío o nulo.");
        }
        if (nuevoSimbolo == null || nuevoSimbolo.isBlank()) {
            throw new IllegalArgumentException("El símbolo no puede ser vacío o nulo.");
        }

        boolean existe = monedaRepository.findAll().stream()
                .anyMatch(m -> m.getCodigo().equalsIgnoreCase(nuevoCodigo) && !m.getId().equals(id));
        if (existe) {
            throw new IllegalArgumentException("Ya existe otra moneda con ese código.");
        }

        moneda.setCodigo(nuevoCodigo);
        moneda.setSimbolo(nuevoSimbolo);
        return monedaRepository.save(moneda);
    }

    @Transactional
    public void deleteMoneda(Long id) {
        MonedaEntity moneda = getMoneda(id);
        monedaRepository.delete(moneda);
    }
}
