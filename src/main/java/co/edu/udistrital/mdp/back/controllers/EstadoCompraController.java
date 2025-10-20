package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.dto.EstadoCompraDTO;
import co.edu.udistrital.mdp.back.dto.EstadoCompraDetailDTO;
import co.edu.udistrital.mdp.back.entities.EstadoCompraEntity;
import co.edu.udistrital.mdp.back.services.EstadoCompraService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estados-compra")
public class EstadoCompraController {

    @Autowired
    private EstadoCompraService estadoCompraService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EstadoCompraDetailDTO> findAll() {
        List<EstadoCompraEntity> estados = estadoCompraService.getAllEstadosCompra();
        return modelMapper.map(estados, new TypeToken<List<EstadoCompraDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EstadoCompraDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        EstadoCompraEntity estadoEntity = estadoCompraService.getEstadoCompraById(id);
        return modelMapper.map(estadoEntity, EstadoCompraDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoCompraDTO create(@RequestBody EstadoCompraDTO estadoCompraDTO) throws EntityNotFoundException {
        EstadoCompraEntity estadoEntity = estadoCompraService.createEstadoCompra(
                modelMapper.map(estadoCompraDTO, EstadoCompraEntity.class)
        );
        return modelMapper.map(estadoEntity, EstadoCompraDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EstadoCompraDTO update(@PathVariable Long id, @RequestBody EstadoCompraDTO estadoCompraDTO) throws EntityNotFoundException {
        EstadoCompraEntity estadoEntity = estadoCompraService.updateEstadoCompra(
                id,
                modelMapper.map(estadoCompraDTO, EstadoCompraEntity.class)
        );
        return modelMapper.map(estadoEntity, EstadoCompraDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        estadoCompraService.deleteEstadoCompra(id);
    }

    @GetMapping("/por-defecto")
    @ResponseStatus(HttpStatus.OK)
    public EstadoCompraDTO getEstadoPorDefecto() {
        EstadoCompraEntity estadoEntity = estadoCompraService.getEstadoCompraPorDefecto();
        return modelMapper.map(estadoEntity, EstadoCompraDTO.class);
    }
}
