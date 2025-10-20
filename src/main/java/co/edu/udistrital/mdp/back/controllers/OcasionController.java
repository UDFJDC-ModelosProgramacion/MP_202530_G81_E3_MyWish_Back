package co.edu.udistrital.mdp.back.controllers;


import co.edu.udistrital.mdp.back.dto.OcasionDTO;
import co.edu.udistrital.mdp.back.dto.OcasionDetailDTO;
import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import co.edu.udistrital.mdp.back.services.OcasionService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad Ocasion.
 * Gestiona las operaciones CRUD sobre las ocasiones.
 */
@RestController
@RequestMapping("/ocasiones")
public class OcasionController {

    @Autowired
    private OcasionService ocasionService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obtiene todas las ocasiones.
     * @return Lista de OcasionDetailDTO.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<OcasionDetailDTO> findAll() {
        List<OcasionEntity> ocasiones = ocasionService.getOcasiones();
        return modelMapper.map(ocasiones, new TypeToken<List<OcasionDetailDTO>>(){}.getType());
    }

    /**
     * Obtiene una ocasión específica por su ID.
     * @param id ID de la ocasión.
     * @return OcasionDetailDTO de la ocasión encontrada.
     * @throws IllegalArgumentException si no se encuentra la ocasión.
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public OcasionDetailDTO findOne(@PathVariable Long id) {
        OcasionEntity ocasionEntity = ocasionService.getOcasion(id);
        return modelMapper.map(ocasionEntity, OcasionDetailDTO.class);
    }

    /**
     * Crea una nueva ocasión.
     * @param ocasionDTO Datos de la nueva ocasión.
     * @return OcasionDTO creada.
     * @throws IllegalArgumentException si el nombre es inválido o ya existe.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public OcasionDTO create(@RequestBody OcasionDTO ocasionDTO) {
        OcasionEntity ocasionEntity = ocasionService.createOcasion(
                modelMapper.map(ocasionDTO, OcasionEntity.class));
        return modelMapper.map(ocasionEntity, OcasionDTO.class);
    }

    /**
     * Actualiza el nombre de una ocasión existente.
     * @param id ID de la ocasión a actualizar.
     * @param ocasionDTO DTO con el nuevo nombre.
     * @return OcasionDTO actualizada.
     * @throws IllegalArgumentException si el nombre es inválido o duplicado.
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public OcasionDTO update(@PathVariable Long id, @RequestBody OcasionDTO ocasionDTO) {
        OcasionEntity ocasionEntity = ocasionService.updateOcasion(id, ocasionDTO.getNombre());
        return modelMapper.map(ocasionEntity, OcasionDTO.class);
    }

    /**
     * Elimina una ocasión por su ID.
     * @param id ID de la ocasión a eliminar.
     * @throws IllegalArgumentException si la ocasión no existe.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ocasionService.deleteOcasion(id);
    }
}
