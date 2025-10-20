package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.dto.PrioridadRegaloDTO;
import co.edu.udistrital.mdp.back.dto.PrioridadRegaloDetailDTO;
import co.edu.udistrital.mdp.back.entities.PrioridadRegaloEntity;
import co.edu.udistrital.mdp.back.services.PrioridadRegaloService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prioridades-regalo")
public class PrioridadRegaloController {

    @Autowired
    private PrioridadRegaloService prioridadRegaloService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PrioridadRegaloDetailDTO> findAll() {
        List<PrioridadRegaloEntity> prioridades = prioridadRegaloService.getAllPrioridadesRegalo();
        return modelMapper.map(prioridades, new TypeToken<List<PrioridadRegaloDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PrioridadRegaloDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        PrioridadRegaloEntity prioridadEntity = prioridadRegaloService.getPrioridadRegaloById(id);
        return modelMapper.map(prioridadEntity, PrioridadRegaloDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrioridadRegaloDTO create(@RequestBody PrioridadRegaloDTO prioridadRegaloDTO) throws EntityNotFoundException {
        PrioridadRegaloEntity prioridadEntity = prioridadRegaloService.createPrioridadRegalo(
                modelMapper.map(prioridadRegaloDTO, PrioridadRegaloEntity.class)
        );
        return modelMapper.map(prioridadEntity, PrioridadRegaloDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PrioridadRegaloDTO update(@PathVariable Long id, @RequestBody PrioridadRegaloDTO prioridadRegaloDTO) throws EntityNotFoundException {
        PrioridadRegaloEntity prioridadEntity = prioridadRegaloService.updatePrioridadRegalo(
                id,
                modelMapper.map(prioridadRegaloDTO, PrioridadRegaloEntity.class)
        );
        return modelMapper.map(prioridadEntity, PrioridadRegaloDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        prioridadRegaloService.deletePrioridadRegalo(id);
    }

    @GetMapping("/nivel/{nivel}")
    @ResponseStatus(HttpStatus.OK)
    public List<PrioridadRegaloDTO> findByNivel(@PathVariable Integer nivel) {
        List<PrioridadRegaloEntity> prioridades = prioridadRegaloService.getPrioridadesPorNivel(nivel);
        return modelMapper.map(prioridades, new TypeToken<List<PrioridadRegaloDTO>>() {}.getType());
    }
}
