package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.services.TiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tiendas")
public class TiendaController {

    @Autowired
    private TiendaService tiendaService;

    // Obtener todas las tiendas
    @GetMapping
    public List<TiendaEntity> findAll() {
        return tiendaService.getAll();
    }

    // Obtener una tienda por ID
    @GetMapping("/{id}")
    public TiendaEntity findOne(@PathVariable Long id) {
        return tiendaService.getById(id);
    }

    // Crear una nueva tienda
    @PostMapping
    public TiendaEntity create(@RequestBody TiendaEntity tienda) {
        return tiendaService.create(tienda);
    }

    // Actualizar una tienda existente
    @PutMapping("/{id}")
    public TiendaEntity update(@PathVariable Long id, @RequestBody TiendaEntity tienda) {
        return tiendaService.update(id, tienda);
    }

    // Eliminar una tienda por ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tiendaService.delete(id);
    }

    // Buscar tiendas por nombre
    @GetMapping("/buscar/nombre/{nombre}")
    public List<TiendaEntity> findByNombre(@PathVariable String nombre) {
        return tiendaService.findByNombre(nombre);
    }

    // Buscar tiendas por país
    @GetMapping("/buscar/pais/{pais}")
    public List<TiendaEntity> findByPais(@PathVariable String pais) {
        return tiendaService.findByPais(pais);
    }

    // Buscar tiendas por ciudad
    @GetMapping("/buscar/ciudad/{ciudad}")
    public List<TiendaEntity> findByCiudad(@PathVariable String ciudad) {
        return tiendaService.findByCiudad(ciudad);
    }
}
