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

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<EstadoCompraDetailDTO> findAll() {
        List<EstadoCompraEntity> estados = estadoCompraService.getAllEstadosCompra();
        return modelMapper.map(estados, new TypeToken<List<EstadoCompraDetailDTO>>() {}.getType());
    }


    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EstadoCompraDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        EstadoCompraEntity estadoEntity = estadoCompraService.getEstadoCompraById(id);
        return modelMapper.map(estadoEntity, EstadoCompraDetailDTO.class);
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EstadoCompraDTO create(@RequestBody EstadoCompraDTO estadoCompraDTO) 
            throws IllegalOperationException, EntityNotFoundException {
        EstadoCompraEntity estadoEntity = estadoCompraService.createEstadoCompra(
            modelMapper.map(estadoCompraDTO, EstadoCompraEntity.class)
        );
        return modelMapper.map(estadoEntity, EstadoCompraDTO.class);
    }


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


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) 
            throws EntityNotFoundException, IllegalOperationException {
        estadoCompraService.deleteEstadoCompra(id);
    }


    @GetMapping(value = "/por-defecto")
    @ResponseStatus(code = HttpStatus.OK)
    public EstadoCompraDTO getEstadoPorDefecto() {
        EstadoCompraEntity estadoEntity = estadoCompraService.getEstadoCompraPorDefecto();
        return modelMapper.map(estadoEntity, EstadoCompraDTO.class);
    }
}