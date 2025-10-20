package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.*;
import co.edu.udistrital.mdp.back.exceptions.CorreoNoEnviadoException;
import co.edu.udistrital.mdp.back.repositories.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MensajeInvitacionService {

    @Autowired 
    private MensajeInvitacionRepository mensajeRepo;

    @Autowired 
    private CelebracionRepository celebracionRepo;

    @Autowired 
    private ListaRegalosRepository listaRepo;

    @Autowired 
    private UsuarioRepository usuarioRepo;

    @Autowired 
    private JavaMailSender mailSender;

    // =====================================================
    // MÉTODO GENÉRICO DE ENVÍO DE INVITACIONES
    // =====================================================
    @Transactional
    public MensajeInvitacionEntity enviarInvitacion(
            String tipo, Long entidadId, String correoDestinatario, String textoMensaje) {

        log.info("Enviando invitación tipo {} al correo {}", tipo, correoDestinatario);

        // Buscar destinatario
        UsuarioEntity destinatario = usuarioRepo.findByCorreo(correoDestinatario);
        if (destinatario == null)
            throw new EntityNotFoundException("No existe usuario con correo " + correoDestinatario);

        MensajeInvitacionEntity mensaje = new MensajeInvitacionEntity();
        mensaje.setDestinatario(destinatario);
        mensaje.setMensaje(textoMensaje);
        mensaje.setFechaEnvio(new Date());

        UsuarioEntity remitente;
        String asunto;
        String titulo;
        String nombreEntidad;

        // --- Según el tipo (celebración o lista) ---
        switch (tipo.toLowerCase()) {
            case "celebracion" -> {
                CelebracionEntity celebracion = celebracionRepo.findById(entidadId)
                        .orElseThrow(() -> new EntityNotFoundException("La celebración no existe."));
                remitente = celebracion.getOrganizador();
                nombreEntidad = celebracion.getNombre();
                mensaje.setCelebracion(celebracion);
                asunto = "🎉 Invitación a " + nombreEntidad;
                titulo = "¡Has sido invitado a una celebración!";
            }
            case "lista" -> {
                ListaRegalosEntity lista = listaRepo.findById(entidadId)
                        .orElseThrow(() -> new EntityNotFoundException("La lista de regalos no existe."));
                remitente = lista.getCreador();
                nombreEntidad = lista.getNombre();
                mensaje.setListaRegalos(lista);
                asunto = "Invitación a lista de regalos: " + nombreEntidad;
                titulo = "¡Te han invitado a participar en una lista de regalos!";
            }
            default -> throw new IllegalArgumentException("Tipo de invitación no válido: " + tipo);
        }

        mensaje.setRemitente(remitente);

        // Enviar correo
        try {
            enviarCorreo(destinatario.getCorreo(), remitente.getNombre(), asunto, titulo, textoMensaje);
        } catch (MessagingException e) {
            throw new CorreoNoEnviadoException("Error al enviar el correo: " + e.getMessage(), e);
        }

        // Guardar en BD
        return mensajeRepo.save(mensaje);
    }

    // =====================================================
    // MÉTODO ÚNICO DE ENVÍO DE CORREOS
    // =====================================================
    private void enviarCorreo(String destinatario, String remitenteNombre, String asunto, String titulo, String cuerpo)
            throws MessagingException {

        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(
                "<h2>" + titulo + "</h2>" +
                        "<p><b>" + remitenteNombre + "</b> te ha enviado una invitación.</p>" +
                        "<p>Mensaje: " + cuerpo + "</p>" +
                        "<p><i>¡Esperamos verte pronto!</i></p>",
                true);
        mailSender.send(mensaje);
    }

    // =====================================================
    // CRUD BÁSICO
    // =====================================================

    @Transactional(readOnly = true)
    public List<MensajeInvitacionEntity> getAll() {
        return mensajeRepo.findAll();
    }

    // 🔹 No es necesario @Transactional aquí, solo lectura
    public MensajeInvitacionEntity getById(Long id) {
        return mensajeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mensaje no encontrado con id " + id));
    }

    @Transactional
    public MensajeInvitacionEntity update(Long id, MensajeInvitacionEntity nuevo) {
        MensajeInvitacionEntity existente = getById(id);
        existente.setMensaje(nuevo.getMensaje());
        existente.setFechaEnvio(nuevo.getFechaEnvio());
        return mensajeRepo.save(existente);
    }

    @Transactional
    public void delete(Long id) {
        MensajeInvitacionEntity existente = getById(id);
        mensajeRepo.delete(existente);
    }
}
