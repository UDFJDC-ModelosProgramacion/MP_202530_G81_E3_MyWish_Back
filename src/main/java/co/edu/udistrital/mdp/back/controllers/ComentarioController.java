package co.edu.udistrital.mdp.back.controllers;


import co.edu.udistrital.mdp.back.dto.ComentarioDTO;
import co.edu.udistrital.mdp.back.dto.ComentarioDetailDTO;
import co.edu.udistrital.mdp.back.entities.ComentarioEntity;
import co.edu.udistrital.mdp.back.services.ComentarioService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Controlador REST para la gestión de comentarios.
 * Proporciona operaciones CRUD básicas sobre la entidad ComentarioEntity.
 */
@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obtiene todos los comentarios almacenados.
     *
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ComentarioDetailDTO> findAll() {
        List<ComentarioEntity> comentarios = comentarioService.getComentarios();
        Type listType = new TypeToken<List<ComentarioDetailDTO>>() {}.getType();
        return modelMapper.map(comentarios, listType);
    }

    /**
     * Obtiene un comentario específico por su ID.
     *

     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ComentarioDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        ComentarioEntity comentarioEntity = comentarioService.getComentario(id);
        return modelMapper.map(comentarioEntity, ComentarioDetailDTO.class);
    }

    /**
     * Crea un nuevo comentario.
     *

     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ComentarioDTO create(@RequestBody ComentarioDTO comentarioDTO) {
        ComentarioEntity comentarioEntity = modelMapper.map(comentarioDTO, ComentarioEntity.class);
        ComentarioEntity nuevoComentario = comentarioService.createComentario(comentarioEntity);
        return modelMapper.map(nuevoComentario, ComentarioDTO.class);
    }

    /**
     * Actualiza los datos de un comentario existente.
     *

     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ComentarioDTO update(@PathVariable Long id, @RequestBody ComentarioDTO comentarioDTO) {
        ComentarioEntity actualizado = comentarioService.updateComentario(
                id,
                comentarioDTO.getTexto(),
                comentarioDTO.getCalificacion()
        );
        return modelMapper.map(actualizado, ComentarioDTO.class);
    }

    /**
     * Elimina un comentario.
     *

     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        comentarioService.deleteComentario(id);
    }
}
