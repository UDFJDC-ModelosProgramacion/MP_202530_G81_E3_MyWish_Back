package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.dto.PrioridadRegaloDTO;
import co.edu.udistrital.mdp.back.dto.PrioridadRegaloDetailDTO;
import co.edu.udistrital.mdp.back.entities.PrioridadRegaloEntity;
import co.edu.udistrital.mdp.back.services.PrioridadRegaloService;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prioridades-regalo")
public class PrioridadRegaloController {

    @Autowired
    private PrioridadRegaloService prioridadRegaloService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PrioridadRegaloDetailDTO> findAll() {
        List<PrioridadRegaloEntity> prioridades = prioridadRegaloService.getAllPrioridadesRegalo();
        return modelMapper.map(prioridades, new TypeToken<List<PrioridadRegaloDetailDTO>>() {}.getType());
    }


    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PrioridadRegaloDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        PrioridadRegaloEntity prioridadEntity = prioridadRegaloService.getPrioridadRegaloById(id);
        return modelMapper.map(prioridadEntity, PrioridadRegaloDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public PrioridadRegaloDTO create(@RequestBody PrioridadRegaloDTO prioridadRegaloDTO) 
            throws IllegalOperationException, EntityNotFoundException {
        PrioridadRegaloEntity prioridadEntity = prioridadRegaloService.createPrioridadRegalo(
            modelMapper.map(prioridadRegaloDTO, PrioridadRegaloEntity.class)
        );
        return modelMapper.map(prioridadEntity, PrioridadRegaloDTO.class);
    }


    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PrioridadRegaloDTO update(@PathVariable Long id, @RequestBody PrioridadRegaloDTO prioridadRegaloDTO)
            throws EntityNotFoundException, IllegalOperationException {
        PrioridadRegaloEntity prioridadEntity = prioridadRegaloService.updatePrioridadRegalo(
            id, 
            modelMapper.map(prioridadRegaloDTO, PrioridadRegaloEntity.class)
        );
        return modelMapper.map(prioridadEntity, PrioridadRegaloDTO.class);
    }


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) 
            throws EntityNotFoundException, IllegalOperationException {
        prioridadRegaloService.deletePrioridadRegalo(id);
    }


    @GetMapping(value = "/nivel/{nivel}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<PrioridadRegaloDTO> findByNivel(@PathVariable Integer nivel) {
        List<PrioridadRegaloEntity> prioridades = prioridadRegaloService.getPrioridadesPorNivel(nivel);
        return modelMapper.map(prioridades, new TypeToken<List<PrioridadRegaloDTO>>() {}.getType());
    }
}