package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.entities.RegaloEntity;
import co.edu.udistrital.mdp.back.services.RegaloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/regalos")
public class RegaloController {

    @Autowired
    private RegaloService regaloService;

    @GetMapping
    public List<RegaloEntity> findAll() {
        return regaloService.getAll();
    }

    @GetMapping("/{id}")
    public RegaloEntity findOne(@PathVariable Long id) {
        return regaloService.getById(id);
    }

    @PostMapping
    public RegaloEntity create(@RequestBody RegaloEntity regalo) {
        return regaloService.create(regalo);
    }

    @PutMapping("/{id}")
    public RegaloEntity update(@PathVariable Long id, @RequestBody RegaloEntity regalo) {
        return regaloService.update(id, regalo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        regaloService.delete(id);
    }

    @GetMapping("/categoria/{categoria}")
    public List<RegaloEntity> findByCategoria(@PathVariable String categoria) {
        return regaloService.findByCategoria(categoria);
    }

    @GetMapping("/precio/menor/{precio}")
    public List<RegaloEntity> findByPrecioMenorA(@PathVariable Double precio) {
        return regaloService.findByPrecioMenorA(precio);
    }

    @GetMapping("/precio/mayor/{precio}")
    public List<RegaloEntity> findByPrecioMayorA(@PathVariable Double precio) {
        return regaloService.findByPrecioMayorA(precio);
    }

    @GetMapping("/buscar")
    public List<RegaloEntity> findByDescripcion(@RequestParam String texto) {
        return regaloService.findByDescripcion(texto);
    }

    @GetMapping("/tienda/{tiendaId}")
    public List<RegaloEntity> findByTienda(@PathVariable Long tiendaId) {
        return regaloService.findByTienda(tiendaId);
    }

    @GetMapping("/estado/{estadoId}")
    public List<RegaloEntity> findByEstado(@PathVariable Long estadoId) {
        return regaloService.findByEstadoCompra(estadoId);
    }

    @GetMapping("/prioridad/{prioridadId}")
    public List<RegaloEntity> findByPrioridad(@PathVariable Long prioridadId) {
        return regaloService.findByPrioridad(prioridadId);
    }
}
