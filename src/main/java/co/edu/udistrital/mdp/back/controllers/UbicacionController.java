package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.entities.UbicacionEntity;
import co.edu.udistrital.mdp.back.services.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ubicaciones")
public class UbicacionController {

    @Autowired
    private UbicacionService ubicacionService;

    @GetMapping
    public List<UbicacionEntity> findAll() {
        return ubicacionService.getAll();
    }

    @GetMapping("/{id}")
    public UbicacionEntity findOne(@PathVariable Long id) {
        return ubicacionService.getById(id);
    }

    @PostMapping
    public UbicacionEntity create(@RequestBody UbicacionEntity ubicacion) {
        return ubicacionService.create(ubicacion);
    }

    @PutMapping("/{id}")
    public UbicacionEntity update(@PathVariable Long id, @RequestBody UbicacionEntity ubicacion) {
        return ubicacionService.update(id, ubicacion);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ubicacionService.delete(id);
    }

    @GetMapping("/buscar/ciudad/{ciudad}")
    public List<UbicacionEntity> findByCiudad(@PathVariable String ciudad) {
        return ubicacionService.findByCiudad(ciudad);
    }

    @GetMapping("/buscar/pais/{pais}")
    public List<UbicacionEntity> findByPais(@PathVariable String pais) {
        return ubicacionService.findByPais(pais);
    }

    @GetMapping("/buscar/direccion/{direccion}")
    public List<UbicacionEntity> findByDireccion(@PathVariable String direccion) {
        return ubicacionService.findByDireccion(direccion);
    }

    @GetMapping("/buscar/tienda/{tiendaId}")
    public List<UbicacionEntity> findByTienda(@PathVariable Long tiendaId) {
        return ubicacionService.findByTiendaId(tiendaId);
    }
}
