package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.dto.FotoDTO;
import co.edu.udistrital.mdp.back.entities.FotoEntity;
import co.edu.udistrital.mdp.back.services.FotoService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fotos")
public class FotoController {

    @Autowired
    private FotoService fotoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FotoDTO> findAll() {
        List<FotoEntity> fotos = fotoService.getAllFotos();
        return modelMapper.map(fotos, new TypeToken<List<FotoDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FotoDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        FotoEntity fotoEntity = fotoService.getFotoById(id);
        return modelMapper.map(fotoEntity, FotoDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FotoDTO create(@RequestBody FotoDTO fotoDTO) throws EntityNotFoundException {
        FotoEntity fotoEntity = fotoService.createFoto(
                modelMapper.map(fotoDTO, FotoEntity.class)
        );
        return modelMapper.map(fotoEntity, FotoDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FotoDTO update(@PathVariable Long id, @RequestBody FotoDTO fotoDTO) throws EntityNotFoundException {
        FotoEntity fotoEntity = fotoService.updateFoto(
                id,
                modelMapper.map(fotoDTO, FotoEntity.class)
        );
        return modelMapper.map(fotoEntity, FotoDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        fotoService.deleteFoto(id);
    }

    @GetMapping("/regalo/{regaloId}")
    @ResponseStatus(HttpStatus.OK)
    public List<FotoDTO> findByRegaloId(@PathVariable Long regaloId) {
        List<FotoEntity> fotos = fotoService.getFotosByRegaloId(regaloId);
        return modelMapper.map(fotos, new TypeToken<List<FotoDTO>>() {}.getType());
    }

    @GetMapping("/lista-regalos/{listaId}")
    @ResponseStatus(HttpStatus.OK)
    public List<FotoDTO> findByListaRegalosId(@PathVariable Long listaId) {
        List<FotoEntity> fotos = fotoService.getFotosByListaRegalosId(listaId);
        return modelMapper.map(fotos, new TypeToken<List<FotoDTO>>() {}.getType());
    }

    @GetMapping("/principales")
    @ResponseStatus(HttpStatus.OK)
    public List<FotoDTO> findPrincipales() {
        List<FotoEntity> fotos = fotoService.getFotosPrincipales();
        return modelMapper.map(fotos, new TypeToken<List<FotoDTO>>() {}.getType());
    }
}
