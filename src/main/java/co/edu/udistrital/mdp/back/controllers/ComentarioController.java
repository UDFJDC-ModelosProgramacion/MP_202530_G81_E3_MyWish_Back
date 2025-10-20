package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.entities.ComentarioEntity;
import co.edu.udistrital.mdp.back.services.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping
    public List<ComentarioEntity> findAll() {
        return comentarioService.getAllComentarios();
    }

    @GetMapping("/{id}")
    public ComentarioEntity findOne(@PathVariable Long id) {
        return comentarioService.getComentarioById(id);
    }

    @PostMapping
    public ComentarioEntity create(@RequestBody ComentarioEntity comentario) {
        return comentarioService.createComentario(comentario);
    }

    @PutMapping("/{id}")
    public ComentarioEntity update(@PathVariable Long id, @RequestBody ComentarioEntity comentario) {
        return comentarioService.updateComentario(id, comentario.getTexto(), comentario.getCalificacion());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        comentarioService.deleteComentario(id);
    }
}
