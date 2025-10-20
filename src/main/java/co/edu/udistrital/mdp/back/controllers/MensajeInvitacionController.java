package co.edu.udistrital.mdp.back.controllers;

import co.edu.udistrital.mdp.back.dto.MensajeInvitacionDTO;
import co.edu.udistrital.mdp.back.entities.MensajeInvitacionEntity;
import co.edu.udistrital.mdp.back.services.MensajeInvitacionService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de mensajes de invitación.
 * Se encarga de recibir las peticiones HTTP relacionadas con los mensajes y delegarlas al servicio.
 */
@RestController
@RequestMapping("/mensajes-invitacion")
public class MensajeInvitacionController {

    @Autowired
    private MensajeInvitacionService mensajeService;

    @Autowired
    private ModelMapper modelMapper;

    // =====================================================
    // MÉTODO: Obtener todos los mensajes
    // =====================================================
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<MensajeInvitacionDTO> findAll() {
        List<MensajeInvitacionEntity> mensajes = mensajeService.getAll();
        return modelMapper.map(mensajes, new TypeToken<List<MensajeInvitacionDTO>>(){}.getType());
    }

    // =====================================================
    // MÉTODO: Obtener un mensaje por ID
    // =====================================================
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MensajeInvitacionDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        MensajeInvitacionEntity mensaje = mensajeService.getById(id);
        return modelMapper.map(mensaje, MensajeInvitacionDTO.class);
    }

    // =====================================================
    // MÉTODO: Crear y enviar una invitación
    // =====================================================
    /**
     * Envía una invitación por correo electrónico y la guarda en la base de datos.
     *
     * @param tipo Tipo de invitación ("celebracion" o "lista")
     * @param entidadId ID de la celebración o lista asociada
     * @param correoDestinatario Correo electrónico del usuario destinatario
     * @param textoMensaje Texto del mensaje a enviar
     * @return MensajeInvitacionDTO con la información guardada
     */
    @PostMapping("/enviar")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MensajeInvitacionDTO enviarInvitacion(
            @RequestParam String tipo,
            @RequestParam Long entidadId,
            @RequestParam String correoDestinatario,
            @RequestParam String textoMensaje) {

        MensajeInvitacionEntity mensaje = mensajeService.enviarInvitacion(tipo, entidadId, correoDestinatario, textoMensaje);
        return modelMapper.map(mensaje, MensajeInvitacionDTO.class);
    }

    // =====================================================
    // MÉTODO: Actualizar un mensaje existente
    // =====================================================
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MensajeInvitacionDTO update(@PathVariable Long id, @RequestBody MensajeInvitacionDTO mensajeDTO)
            throws EntityNotFoundException {

        MensajeInvitacionEntity mensaje = mensajeService.update(id, modelMapper.map(mensajeDTO, MensajeInvitacionEntity.class));
        return modelMapper.map(mensaje, MensajeInvitacionDTO.class);
    }

    // =====================================================
    // MÉTODO: Eliminar un mensaje
    // =====================================================
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        mensajeService.delete(id);
    }
}
