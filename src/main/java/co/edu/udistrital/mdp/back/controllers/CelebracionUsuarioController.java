package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.dto.CelebracionDetailDTO;
import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.services.CelebracionUsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar las operaciones relacionadas entre
 * Celebraciones y Usuarios (invitaciones, eliminación de invitaciones, etc.).
 *
 * Provee endpoints para:
 *  - Consultar celebraciones donde un usuario es invitado.
 *  - Verificar si un usuario está invitado a una celebración.
 *  - Remover invitaciones de usuarios a celebraciones.
 */
@Slf4j
@RestController
@RequestMapping("/celebraciones-usuarios")
public class CelebracionUsuarioController {

    @Autowired
    private CelebracionUsuarioService celebracionUsuarioService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obtiene todas las celebraciones a las que un usuario fue invitado.
     *
     * @param usuarioId Identificador del usuario.
     * @return Lista de celebraciones en las que el usuario está invitado.
     * @throws EntityNotFoundException si el usuario no existe.
     */
    @GetMapping("/usuario/{usuarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CelebracionDetailDTO> obtenerCelebracionesDeUsuario(@PathVariable Long usuarioId)
            throws EntityNotFoundException {

        log.info("GET /celebraciones-usuarios/usuario/{}", usuarioId);

        List<CelebracionEntity> celebraciones = celebracionUsuarioService.obtenerCelebracionesDeUsuario(usuarioId);

        return modelMapper.map(celebraciones, new TypeToken<List<CelebracionDetailDTO>>() {
        }.getType());
    }

    /**
     * Verifica si un usuario está invitado a una celebración específica.
     *

     */
    @GetMapping("/usuario/{usuarioId}/celebracion/{celebracionId}")
    @ResponseStatus(code = HttpStatus.OK)
    public boolean estaInvitadoEnCelebracion(@PathVariable Long usuarioId, @PathVariable Long celebracionId)
            throws EntityNotFoundException {

        log.info("GET /celebraciones-usuarios/usuario/{}/celebracion/{}", usuarioId, celebracionId);

        return celebracionUsuarioService.estaInvitadoEnCelebracion(usuarioId, celebracionId);
    }

    /**
     * Elimina la invitación de un usuario a una celebración específica.
     *

     */
    @DeleteMapping("/usuario/{usuarioId}/celebracion/{celebracionId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removerInvitacion(@PathVariable Long usuarioId, @PathVariable Long celebracionId)
            throws EntityNotFoundException, IllegalArgumentException {

        log.info("DELETE /celebraciones-usuarios/usuario/{}/celebracion/{}", usuarioId, celebracionId);

        celebracionUsuarioService.removerInvitacion(usuarioId, celebracionId);
    }
}
