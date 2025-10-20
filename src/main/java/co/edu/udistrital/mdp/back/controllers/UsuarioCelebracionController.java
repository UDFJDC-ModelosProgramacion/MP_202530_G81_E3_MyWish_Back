package co.edu.udistrital.mdp.back.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.services.UsuarioCelebracionService;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/celebraciones")
public class UsuarioCelebracionController {

    @Autowired
    private UsuarioCelebracionService usuarioCelebracionService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Agregar un invitado a una celebración
     
     */
    @PostMapping("/{celebracionId}/invitados/{usuarioId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CelebracionEntity addInvitado(@PathVariable Long celebracionId, @PathVariable Long usuarioId)
            throws EntityNotFoundException, IllegalArgumentException {

        CelebracionEntity celebracion = usuarioCelebracionService.addInvitado(celebracionId, usuarioId);
        return modelMapper.map(celebracion, CelebracionEntity.class);
    }

    /**
     * Remover un invitado de una celebración
     */
    @DeleteMapping("/{celebracionId}/invitados/{usuarioId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeInvitado(@PathVariable Long celebracionId, @PathVariable Long usuarioId)
            throws EntityNotFoundException {
        usuarioCelebracionService.removeInvitado(celebracionId, usuarioId);
    }

    /**
     * Consultar si un usuario está invitado a una celebración
     */
    @GetMapping("/{celebracionId}/invitados/{usuarioId}/check")
    @ResponseStatus(code = HttpStatus.OK)
    public boolean isUsuarioInvitado(@PathVariable Long celebracionId, @PathVariable Long usuarioId)
            throws EntityNotFoundException {
        return usuarioCelebracionService.isUsuarioInvitado(celebracionId, usuarioId);
    }
}
