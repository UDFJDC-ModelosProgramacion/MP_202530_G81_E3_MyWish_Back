package co.edu.udistrital.mdp.back.controllers;


import co.edu.udistrital.mdp.back.dto.RegaloDTO;
import co.edu.udistrital.mdp.back.entities.RegaloEntity;
import co.edu.udistrital.mdp.back.services.RegaloService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regalos")
public class RegaloController {

    @Autowired
    private RegaloService regaloService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Devuelve todos los regalos registrados en la base de datos.
     *
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<RegaloDTO> findAll() {
        List<RegaloEntity> regalos = regaloService.getAll();
        return modelMapper.map(regalos, new TypeToken<List<RegaloDTO>>() {}.getType());
    }

    /**
     * Devuelve un regalo por su ID.
     *

     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public RegaloDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        RegaloEntity regaloEntity = regaloService.getById(id);
        return modelMapper.map(regaloEntity, RegaloDTO.class);
    }

    /**
     * Crea un nuevo regalo.
     *

     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public RegaloDTO create(@RequestBody RegaloDTO regaloDTO) {
        RegaloEntity regaloEntity = regaloService.create(modelMapper.map(regaloDTO, RegaloEntity.class));
        return modelMapper.map(regaloEntity, RegaloDTO.class);
    }

    /**
     * Actualiza la información de un regalo existente.
     *

     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public RegaloDTO update(@PathVariable Long id, @RequestBody RegaloDTO regaloDTO)
            throws EntityNotFoundException {
        RegaloEntity regaloEntity = regaloService.update(id, modelMapper.map(regaloDTO, RegaloEntity.class));
        return modelMapper.map(regaloEntity, RegaloDTO.class);
    }

    /**
     * Elimina un regalo por su ID.
     *

     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        regaloService.delete(id);
    }


    /**
     * Busca regalos por categoría.
     *

     */
    @GetMapping("/categoria/{categoria}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RegaloDTO> findByCategoria(@PathVariable String categoria) {
        List<RegaloEntity> regalos = regaloService.findByCategoria(categoria);
        return modelMapper.map(regalos, new TypeToken<List<RegaloDTO>>() {}.getType());
    }

    /**
     * Busca regalos con precio menor a un valor dado.
     *

     */
    @GetMapping("/precio/menor/{precio}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RegaloDTO> findByPrecioMenorA(@PathVariable Double precio) {
        List<RegaloEntity> regalos = regaloService.findByPrecioMenorA(precio);
        return modelMapper.map(regalos, new TypeToken<List<RegaloDTO>>() {}.getType());
    }

    /**
     * Busca regalos con precio mayor a un valor dado.
     *
+
     */
    @GetMapping("/precio/mayor/{precio}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RegaloDTO> findByPrecioMayorA(@PathVariable Double precio) {
        List<RegaloEntity> regalos = regaloService.findByPrecioMayorA(precio);
        return modelMapper.map(regalos, new TypeToken<List<RegaloDTO>>() {}.getType());
    }

    /**
     * Busca regalos por texto contenido en la descripción.
     *
     */
    @GetMapping("/descripcion/{texto}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RegaloDTO> findByDescripcion(@PathVariable String texto) {
        List<RegaloEntity> regalos = regaloService.findByDescripcion(texto);
        return modelMapper.map(regalos, new TypeToken<List<RegaloDTO>>() {}.getType());
    }

    /**
     * Busca regalos asociados a una tienda específica.
     */
    @GetMapping("/tienda/{tiendaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RegaloDTO> findByTienda(@PathVariable Long tiendaId) {
        List<RegaloEntity> regalos = regaloService.findByTienda(tiendaId);
        return modelMapper.map(regalos, new TypeToken<List<RegaloDTO>>() {}.getType());
    }

    /**
     * Busca regalos según el estado de compra.
     */
    @GetMapping("/estado/{estadoId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RegaloDTO> findByEstadoCompra(@PathVariable Long estadoId) {
        List<RegaloEntity> regalos = regaloService.findByEstadoCompra(estadoId);
        return modelMapper.map(regalos, new TypeToken<List<RegaloDTO>>() {}.getType());
    }

    /**
     * Busca regalos según la prioridad asignada.
     */
    @GetMapping("/prioridad/{prioridadId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RegaloDTO> findByPrioridad(@PathVariable Long prioridadId) {
        List<RegaloEntity> regalos = regaloService.findByPrioridad(prioridadId);
        return modelMapper.map(regalos, new TypeToken<List<RegaloDTO>>() {}.getType());
    }
}
