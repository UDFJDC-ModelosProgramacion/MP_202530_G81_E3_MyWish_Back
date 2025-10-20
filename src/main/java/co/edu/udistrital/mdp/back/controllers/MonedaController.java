package co.edu.udistrital.mdp.back.controllers;


import co.edu.udistrital.mdp.back.dto.MonedaDTO;
import co.edu.udistrital.mdp.back.dto.MonedaDetailDTO;
import co.edu.udistrital.mdp.back.entities.MonedaEntity;
import co.edu.udistrital.mdp.back.services.MonedaService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad Moneda.
 * Permite realizar las operaciones CRUD sobre las monedas del sistema.
 */
@RestController
@RequestMapping("/monedas")
public class MonedaController {

    @Autowired
    private MonedaService monedaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obtiene todas las monedas registradas.
     *
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<MonedaDetailDTO> findAll() {
        List<MonedaEntity> monedas = monedaService.getMonedas();
        return modelMapper.map(monedas, new TypeToken<List<MonedaDetailDTO>>() {
        }.getType());
    }

    /**
     * Obtiene una moneda específica por su ID.
     *
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MonedaDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        MonedaEntity monedaEntity = monedaService.getMoneda(id);
        return modelMapper.map(monedaEntity, MonedaDetailDTO.class);
    }

    /**
     * Crea una nueva moneda en el sistema.
     *     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public MonedaDTO create(@RequestBody MonedaDTO monedaDTO) {
        MonedaEntity monedaEntity = modelMapper.map(monedaDTO, MonedaEntity.class);
        MonedaEntity creada = monedaService.createMoneda(monedaEntity);
        return modelMapper.map(creada, MonedaDTO.class);
    }

    /**
     * Actualiza los datos de una moneda existente.
     *
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MonedaDTO update(@PathVariable Long id, @RequestBody MonedaDTO monedaDTO) {
        MonedaEntity actualizada = monedaService.updateMoneda(
                id,
                monedaDTO.getCodigo(),
                monedaDTO.getSimbolo()
        );
        return modelMapper.map(actualizada, MonedaDTO.class);
    }

    /**
     * Elimina una moneda del sistema.
     *
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        monedaService.deleteMoneda(id);
    }
}
