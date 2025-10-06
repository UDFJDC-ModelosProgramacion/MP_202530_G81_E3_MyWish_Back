package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.back.entities.UbicacionEntity;
import co.edu.udistrital.mdp.back.repositories.UbicacionRepository;

@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    public List<UbicacionEntity> findAll() {
        return ubicacionRepository.findAll();
    }

    public Optional<UbicacionEntity> findById(Long id) {
        return ubicacionRepository.findById(id);
    }

    public UbicacionEntity save(UbicacionEntity ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    public void deleteById(Long id) {
        ubicacionRepository.deleteById(id);
    }
}
