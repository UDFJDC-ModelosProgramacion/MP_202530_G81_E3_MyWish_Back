package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import co.edu.udistrital.mdp.back.services.OcasionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ocasiones")
public class OcasionController {

    @Autowired
    private OcasionService ocasionService;

    @GetMapping
    public List<OcasionEntity> findAll() {
        return ocasionService.getAllOcasiones();
    }

    @GetMapping("/{id}")
    public OcasionEntity findOne(@PathVariable Long id) {
        return ocasionService.getOcasionById(id);
    }

    @PostMapping
    public OcasionEntity create(@RequestBody OcasionEntity ocasion) {
        return ocasionService.createOcasion(ocasion);
    }

    @PutMapping("/{id}")
    public OcasionEntity update(@PathVariable Long id, @RequestBody OcasionEntity ocasion) {
        return ocasionService.updateOcasion(id, ocasion.getNombre());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ocasionService.deleteOcasion(id);
    }
}
