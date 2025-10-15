package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.ComentarioEntity;
import co.edu.udistrital.mdp.back.repositories.ComentarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Transactional
    public ComentarioEntity createComentario(ComentarioEntity comentario) {
        log.info("Creando comentario...");

        if (comentario.getTexto() == null || comentario.getTexto().isBlank()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío.");
        }

        if (comentario.getUsuario() == null) {
            throw new IllegalArgumentException("El comentario debe estar asociado a un usuario válido.");
        }

        if (comentario.getTienda() == null) {
            throw new IllegalArgumentException("El comentario debe estar asociado a una tienda válida.");
        }

        return comentarioRepository.save(comentario);
    }

    public List<ComentarioEntity> getComentarios() {
        return comentarioRepository.findAll();
    }

    public ComentarioEntity getComentario(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado."));
    }

    @Transactional
    public ComentarioEntity updateComentario(Long id, String nuevoTexto, int nuevaCalificacion) {
        ComentarioEntity comentario = getComentario(id);

        if (nuevoTexto == null || nuevoTexto.isBlank()) {
            throw new IllegalArgumentException("El texto del comentario no puede estar vacío.");
        }

        if (nuevaCalificacion < 0 || nuevaCalificacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 0 y 5.");
        }

        comentario.setTexto(nuevoTexto);
        comentario.setCalificacion(nuevaCalificacion);
        return comentarioRepository.save(comentario);
    }

    @Transactional
    public void deleteComentario(Long id) {
        ComentarioEntity comentario = getComentario(id);
        comentarioRepository.delete(comentario);
    }
}