package co.edu.udistrital.mdp.back.controllers;


import co.edu.udistrital.mdp.back.dto.UsuarioDTO;
import co.edu.udistrital.mdp.back.dto.UsuarioDetailDTO;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.services.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Controlador REST para la gestión de usuarios.
 * 
 * Captura las peticiones HTTP relacionadas con el recurso "usuarios" y las
 * delega en el {@link UsuarioService}.
 * Permite crear, consultar, actualizar y eliminar usuarios.
 * 
 * Las respuestas se devuelven en formato JSON.
 */
@Slf4j
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Retorna la lista de todos los usuarios registrados.
     
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<UsuarioDetailDTO> findAll() {
        log.info("GET /usuarios - Consultar todos los usuarios");

        // ⚠️ Supongamos que el servicio tiene un método getUsuarios()
        // Si no lo tiene, puedes implementarlo en UsuarioService.
        List<UsuarioEntity> usuarios = usuarioService.getAllUsuarios();

        Type listType = new TypeToken<List<UsuarioDetailDTO>>() {}.getType();
        return modelMapper.map(usuarios, listType);
    }

    /**
     * Retorna un usuario según su ID.
     *
    
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UsuarioDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        log.info("GET /usuarios/{} - Consultar usuario por ID", id);

        // También requiere método getUsuario(id) en UsuarioService
        UsuarioEntity usuarioEntity = usuarioService.getUsuario(id);

        return modelMapper.map(usuarioEntity, UsuarioDetailDTO.class);
    }

    /**
     * Crea un nuevo usuario.
     *
     
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UsuarioDTO create(@RequestBody UsuarioDTO usuarioDTO) {
        log.info("POST /usuarios - Crear nuevo usuario");

        UsuarioEntity usuarioEntity = usuarioService.crearUsuario(
                modelMapper.map(usuarioDTO, UsuarioEntity.class)
        );

        return modelMapper.map(usuarioEntity, UsuarioDTO.class);
    }

    /**
     * Actualiza los datos básicos de un usuario existente (por ejemplo, el correo).
     *
     
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UsuarioDTO update(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO)
            throws EntityNotFoundException {
        log.info("PUT /usuarios/{} - Actualizar usuario", id);

        UsuarioEntity updatedUsuario = usuarioService.actualizarCorreoUsuario(
                id, usuarioDTO.getCorreo()
        );

        return modelMapper.map(updatedUsuario, UsuarioDTO.class);
    }

    /**
     * Elimina un usuario del sistema.
     *
     
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalStateException {
        log.info("DELETE /usuarios/{} - Eliminar usuario", id);

        usuarioService.eliminarUsuario(id);
    }
}
