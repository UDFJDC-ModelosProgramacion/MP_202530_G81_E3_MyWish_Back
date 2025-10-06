package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.back.entities.RegaloEntity;
import co.edu.udistrital.mdp.back.repositories.RegaloRepository;

@Service

public class RegaloService {

    @Autowired
    private RegaloRepository regaloRepository;

    public List<RegaloEntity> findAll() {
        return regaloRepository.findAll();
    }

    public Optional<RegaloEntity> findById(Long id) {
        return regaloRepository.findById(id);
    }

    public RegaloEntity save(RegaloEntity regalo) {
        return regaloRepository.save(regalo);
    }

    public void deleteById(Long id) {
        regaloRepository.deleteById(id);
    }
}
