
package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;

@Service
public class TiendaService {

    @Autowired
    private TiendaRepository tiendaRepository;

    public List<TiendaEntity> findAll() {
        return tiendaRepository.findAll();
    }

    public Optional<TiendaEntity> findById(Long id) {
        return tiendaRepository.findById(id);
    }

    public TiendaEntity save(TiendaEntity tienda) {
        return tiendaRepository.save(tienda);
    }

    public void deleteById(Long id) {
        tiendaRepository.deleteById(id);
    }
}
