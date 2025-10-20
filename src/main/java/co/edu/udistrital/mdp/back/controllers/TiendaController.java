package co.edu.udistrital.mdp.back.controllers;


import co.edu.udistrital.mdp.back.dto.TiendaDTO;
import co.edu.udistrital.mdp.back.dto.TiendaDetailDTO;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.services.TiendaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tiendas")
public class TiendaController {

    @Autowired
    private TiendaService tiendaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obtiene todas las tiendas registradas.
     *
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<TiendaDetailDTO> findAll() {
        log.info("Solicitud GET /tiendas");
        List<TiendaEntity> tiendas = tiendaService.getAll();
        Type listType = new TypeToken<List<TiendaDetailDTO>>() {}.getType();
        return modelMapper.map(tiendas, listType);
    }

    /**
     * Obtiene una tienda específica por su ID.
     *
     * @param id Identificador de la tienda
     
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TiendaDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        log.info("Solicitud GET /tiendas/{}", id);
        TiendaEntity tiendaEntity = tiendaService.getById(id);
        return modelMapper.map(tiendaEntity, TiendaDetailDTO.class);
    }

    /**
     * Crea una nueva tienda.
     *

     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public TiendaDTO create(@RequestBody TiendaDTO tiendaDTO) throws IllegalArgumentException {
        log.info("Solicitud POST /tiendas");
        TiendaEntity tiendaEntity = tiendaService.create(modelMapper.map(tiendaDTO, TiendaEntity.class));
        return modelMapper.map(tiendaEntity, TiendaDTO.class);
    }

    /**
     * Actualiza los datos de una tienda existente.
     *

     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TiendaDTO update(@PathVariable Long id, @RequestBody TiendaDTO tiendaDTO)
            throws EntityNotFoundException, IllegalArgumentException {
        log.info("Solicitud PUT /tiendas/{}", id);
        TiendaEntity tiendaEntity = tiendaService.update(id, modelMapper.map(tiendaDTO, TiendaEntity.class));
        return modelMapper.map(tiendaEntity, TiendaDTO.class);
    }

    /**
     * Elimina una tienda por su ID.
     *

     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalArgumentException {
        log.info("Solicitud DELETE /tiendas/{}", id);
        tiendaService.delete(id);
    }

    /**
     * Busca tiendas por nombre parcial o total.
     *

     */
    @GetMapping("/buscar/nombre")
    @ResponseStatus(code = HttpStatus.OK)
    public List<TiendaDTO> findByNombre(@RequestParam String nombre) {
        log.info("Solicitud GET /tiendas/buscar/nombre?nombre={}", nombre);
        List<TiendaEntity> tiendas = tiendaService.findByNombre(nombre);
        Type listType = new TypeToken<List<TiendaDTO>>() {}.getType();
        return modelMapper.map(tiendas, listType);
    }

    /**
     * Busca tiendas por país.
     *

     */
    @GetMapping("/buscar/pais")
    @ResponseStatus(code = HttpStatus.OK)
    public List<TiendaDTO> findByPais(@RequestParam String pais) {
        log.info("Solicitud GET /tiendas/buscar/pais?pais={}", pais);
        List<TiendaEntity> tiendas = tiendaService.findByPais(pais);
        Type listType = new TypeToken<List<TiendaDTO>>() {}.getType();
        return modelMapper.map(tiendas, listType);
    }

    /**
     * Busca tiendas por ciudad.
     *

     */
    @GetMapping("/buscar/ciudad")
    @ResponseStatus(code = HttpStatus.OK)
    public List<TiendaDTO> findByCiudad(@RequestParam String ciudad) {
        log.info("Solicitud GET /tiendas/buscar/ciudad?ciudad={}", ciudad);
        List<TiendaEntity> tiendas = tiendaService.findByCiudad(ciudad);
        Type listType = new TypeToken<List<TiendaDTO>>() {}.getType();
        return modelMapper.map(tiendas, listType);
    }
}
