package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.entities.MonedaEntity;
import co.edu.udistrital.mdp.back.services.MonedaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/monedas")
public class MonedaController {

    @Autowired
    private MonedaService monedaService;

    @GetMapping
    public List<MonedaEntity> findAll() {
        return monedaService.getAllMonedas();
    }

    @GetMapping("/{id}")
    public MonedaEntity findOne(@PathVariable Long id) {
        return monedaService.getMonedaById(id);
    }

    @PostMapping
    public MonedaEntity create(@RequestBody MonedaEntity moneda) {
        return monedaService.createMoneda(moneda);
    }

    @PutMapping("/{id}")
    public MonedaEntity update(@PathVariable Long id, @RequestBody MonedaEntity moneda) {
        return monedaService.updateMoneda(id, moneda.getCodigo(), moneda.getSimbolo());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        monedaService.deleteMoneda(id);
    }
}
