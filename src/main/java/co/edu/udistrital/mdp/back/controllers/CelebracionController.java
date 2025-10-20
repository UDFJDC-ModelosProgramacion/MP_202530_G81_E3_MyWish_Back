package co.edu.udistrital.mdp.back.controllers;


import co.edu.udistrital.mdp.back.dto.CelebracionDTO;
import co.edu.udistrital.mdp.back.dto.CelebracionDetailDTO;
import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.services.CelebracionService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/celebraciones")
public class CelebracionController {

    @Autowired
    private CelebracionService celebracionService;

    @Autowired
    private ModelMapper modelMapper;

    // =====================================================
    // GET ALL
    // =====================================================
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CelebracionDetailDTO> findAll() {
        List<CelebracionEntity> celebraciones = celebracionService.getAllCelebraciones();
        Type listType = new TypeToken<List<CelebracionDetailDTO>>() {}.getType();
        return modelMapper.map(celebraciones, listType);
    }

    // =====================================================
    // GET BY ID
    // =====================================================
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CelebracionDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        CelebracionEntity celebracionEntity = celebracionService.getCelebracionById(id);
        return modelMapper.map(celebracionEntity, CelebracionDetailDTO.class);
    }

    // =====================================================
    // CREATE
    // =====================================================
    @PostMapping("/{organizadorId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CelebracionDTO create(@RequestBody CelebracionDTO celebracionDTO, @PathVariable Long organizadorId) {
        CelebracionEntity celebracionEntity = celebracionService.createCelebracion(
                modelMapper.map(celebracionDTO, CelebracionEntity.class),
                organizadorId
        );
        return modelMapper.map(celebracionEntity, CelebracionDTO.class);
    }

    // =====================================================
    // UPDATE
    // =====================================================
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CelebracionDTO update(@PathVariable Long id, @RequestBody CelebracionDTO celebracionDTO)
            throws EntityNotFoundException {

        CelebracionEntity celebracionEntity = celebracionService.updateCelebracion(
                id, modelMapper.map(celebracionDTO, CelebracionEntity.class)
        );

        return modelMapper.map(celebracionEntity, CelebracionDTO.class);
    }

    // =====================================================
    // DELETE
    // =====================================================
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        celebracionService.deleteCelebracion(id);
    }
}
