package co.edu.udistrital.mdp.back.controllers;


import co.edu.udistrital.mdp.back.dto.ListaRegalosDetailDTO;
import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.services.ListaRegalosUsuarioService;
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
 * Controlador REST para gestionar la relación entre usuarios y listas de regalos.
 * Permite consultar las listas en las que un usuario está invitado, verificar si
 * está invitado a una lista y removerlo de una lista.
 */
@Slf4j
@RestController
@RequestMapping("/usuarios")
public class ListaRegalosUsuarioController {

    @Autowired
    private ListaRegalosUsuarioService listaRegalosUsuarioService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obtiene todas las listas de regalos en las que un usuario está invitado.
     *

     */
    @GetMapping("/{usuarioId}/listas-invitado")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ListaRegalosDetailDTO> obtenerListasDeUsuario(@PathVariable Long usuarioId)
            throws EntityNotFoundException {
        log.info("Solicitud para obtener listas en las que el usuario {} está invitado", usuarioId);

        List<ListaRegalosEntity> listas = listaRegalosUsuarioService.obtenerListasDeUsuario(usuarioId);
        Type listType = new TypeToken<List<ListaRegalosDetailDTO>>() {}.getType();
        return modelMapper.map(listas, listType);
    }

    /**
     * Verifica si un usuario está invitado a una lista de regalos.
     *

     */
    @GetMapping("/{usuarioId}/listas/{listaId}/invitado")
    @ResponseStatus(code = HttpStatus.OK)
    public boolean estaInvitadoEnLista(@PathVariable Long usuarioId, @PathVariable Long listaId)
            throws EntityNotFoundException {
        log.info("Solicitud para verificar si el usuario {} está invitado a la lista {}", usuarioId, listaId);
        return listaRegalosUsuarioService.estaInvitadoEnLista(usuarioId, listaId);
    }

    /**
     * Elimina la invitación de un usuario en una lista de regalos.
     *

     */
    @DeleteMapping("/{usuarioId}/listas/{listaId}/invitacion")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removerInvitacion(@PathVariable Long usuarioId, @PathVariable Long listaId)
            throws EntityNotFoundException, IllegalArgumentException {
        log.info("Solicitud para remover la invitación del usuario {} en la lista {}", usuarioId, listaId);
        listaRegalosUsuarioService.removerInvitacion(usuarioId, listaId);
    }
}
