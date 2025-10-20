package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.dto.EstadoCompraDTO;
import co.edu.udistrital.mdp.back.dto.EstadoCompraDetailDTO;
import co.edu.udistrital.mdp.back.entities.EstadoCompraEntity;
import co.edu.udistrital.mdp.back.services.EstadoCompraService;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados-compra")
public class EstadoCompraController {

    @Autowired
    private EstadoCompraService estadoCompraService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * GET /api/estados-compra
     * Obtiene todos los estados de compra
     * @return Lista de EstadoCompraDetailDTO
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<EstadoCompraDetailDTO> findAll() {
        List<EstadoCompraEntity> estados = estadoCompraService.getAllEstadosCompra();
        return modelMapper.map(estados, new TypeToken<List<EstadoCompraDetailDTO>>() {}.getType());
    }

    /**
     * GET /api/estados-compra/{id}
     * Obtiene un estado de compra por ID
     * @param id - ID del estado de compra
     * @return EstadoCompraDetailDTO
     * @throws EntityNotFoundException si el estado no existe
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EstadoCompraDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        EstadoCompraEntity estadoEntity = estadoCompraService.getEstadoCompraById(id);
        return modelMapper.map(estadoEntity, EstadoCompraDetailDTO.class);
    }

    /**
     * POST /api/estados-compra
     * Crea un nuevo estado de compra
     * @param estadoCompraDTO - Datos del estado a crear
     * @return EstadoCompraDTO creado
     * @throws IllegalOperationException si hay error de validación
     * @throws EntityNotFoundException si alguna entidad relacionada no existe
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EstadoCompraDTO create(@RequestBody EstadoCompraDTO estadoCompraDTO) 
            throws IllegalOperationException, EntityNotFoundException {
        EstadoCompraEntity estadoEntity = estadoCompraService.createEstadoCompra(
            modelMapper.map(estadoCompraDTO, EstadoCompraEntity.class)
        );
        return modelMapper.map(estadoEntity, EstadoCompraDTO.class);
    }

    /**
     * PUT /api/estados-compra/{id}
     * Actualiza un estado de compra existente
     * @param id - ID del estado a actualizar
     * @param estadoCompraDTO - Nuevos datos del estado
     * @return EstadoCompraDTO actualizado
     * @throws EntityNotFoundException si el estado no existe
     * @throws IllegalOperationException si hay error de validación
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EstadoCompraDTO update(@PathVariable Long id, @RequestBody EstadoCompraDTO estadoCompraDTO)
            throws EntityNotFoundException, IllegalOperationException {
        EstadoCompraEntity estadoEntity = estadoCompraService.updateEstadoCompra(
            id, 
            modelMapper.map(estadoCompraDTO, EstadoCompraEntity.class)
        );
        return modelMapper.map(estadoEntity, EstadoCompraDTO.class);
    }

    /**
     * DELETE /api/estados-compra/{id}
     * Elimina un estado de compra
     * @param id - ID del estado a eliminar
     * @throws EntityNotFoundException si el estado no existe
     * @throws IllegalOperationException si el estado está en uso
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) 
            throws EntityNotFoundException, IllegalOperationException {
        estadoCompraService.deleteEstadoCompra(id);
    }

    /**
     * GET /api/estados-compra/por-defecto
     * Obtiene el estado de compra por defecto
     * @return EstadoCompraDTO por defecto
     */
    @GetMapping(value = "/por-defecto")
    @ResponseStatus(code = HttpStatus.OK)
    public EstadoCompraDTO getEstadoPorDefecto() {
        EstadoCompraEntity estadoEntity = estadoCompraService.getEstadoCompraPorDefecto();
        return modelMapper.map(estadoEntity, EstadoCompraDTO.class);
    }
}