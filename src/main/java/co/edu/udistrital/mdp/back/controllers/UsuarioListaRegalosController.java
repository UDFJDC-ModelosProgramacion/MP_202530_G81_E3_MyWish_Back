package co.edu.udistrital.mdp.back.controllers;



import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.dto.ListaRegalosDetailDTO;
import co.edu.udistrital.mdp.back.services.UsuarioListaRegalosService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/listas-regalos")
public class UsuarioListaRegalosController {

    @Autowired
    private UsuarioListaRegalosService usuarioListaRegalosService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Agregar un invitado a una lista de regalos
     * POST /listas-regalos/{listaId}/invitados/{usuarioId}
     */
    @PostMapping(value = "/{listaId}/invitados/{usuarioId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ListaRegalosDetailDTO addInvitado(
            @PathVariable Long listaId,
            @PathVariable Long usuarioId) throws EntityNotFoundException, IllegalArgumentException {

        ListaRegalosEntity lista = usuarioListaRegalosService.addInvitado(listaId, usuarioId);
        return modelMapper.map(lista, ListaRegalosDetailDTO.class);
    }

    /**
     * Remover un invitado de una lista de regalos
     * DELETE /listas-regalos/{listaId}/invitados/{usuarioId}
     */
    @DeleteMapping(value = "/{listaId}/invitados/{usuarioId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeInvitado(
            @PathVariable Long listaId,
            @PathVariable Long usuarioId) throws EntityNotFoundException {

        usuarioListaRegalosService.removeInvitado(listaId, usuarioId);
    }

    /**
     * Verificar si un usuario está invitado a una lista
     * GET /listas-regalos/{listaId}/invitados/{usuarioId}
     */
    @GetMapping(value = "/{listaId}/invitados/{usuarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public boolean isUsuarioInvitado(
            @PathVariable Long listaId,
            @PathVariable Long usuarioId) throws EntityNotFoundException {

        return usuarioListaRegalosService.isUsuarioInvitado(listaId, usuarioId);
    }
}
