package co.edu.udistrital.mdp.back.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;
import co.edu.udistrital.mdp.back.repositories.CelebracionRepository;

@Slf4j

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ListaRegalosRepository listaRegalosRepository;

    @Autowired
    private CelebracionRepository celebracionRepository;

    // Crear usuario con validaciones
    public UsuarioEntity crearUsuario(UsuarioEntity usuario) {
        log.info("Crear usuario: " + usuario.getCorreo());

        if (usuario.getCorreo() == null || usuario.getCorreo().isEmpty()) {
            throw new IllegalArgumentException("No se puede crear un usuario con correo nulo o vacío.");
        }

        if (usuarioRepository.findByCorreo(usuario.getCorreo()) != null) {
            throw new IllegalArgumentException("El correo ya existe en el sistema.");
        }

        if (usuario.getFechaNacimiento() != null && usuario.getFechaNacimiento().after(new Date())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }

        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario (solo correo)
    @Transactional
    public UsuarioEntity actualizarCorreoUsuario(Long usuarioId, String nuevoCorreo) {
        log.info("Actualizar correo usuario ID: " + usuarioId);
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (nuevoCorreo == null || nuevoCorreo.isEmpty()) {
            throw new IllegalArgumentException("El correo no puede ser nulo o vacío.");
        }

        if (!nuevoCorreo.equals(usuario.getCorreo()) && usuarioRepository.findByCorreo(nuevoCorreo) != null) {
            throw new IllegalArgumentException("El nuevo correo ya existe en el sistema.");
        }

        usuario.setCorreo(nuevoCorreo);
        return usuarioRepository.save(usuario);
    }

    // Eliminar usuario con validaciones
    @Transactional
    public void eliminarUsuario(Long usuarioId) {
        log.info("Eliminar usuario ID: " + usuarioId);
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<ListaRegalosEntity> listasCreadas = listaRegalosRepository.findByCreadorId(usuarioId);
        if (!listasCreadas.isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un usuario con listas creadas activas.");
        }

        List<CelebracionEntity> celebraciones = celebracionRepository.findByOrganizadorId(usuarioId);
        if (!celebraciones.isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un usuario con celebraciones organizadas pendientes.");
        }

        usuarioRepository.delete(usuario);
    }

    // Invitar usuario a lista de regalos (con validaciones)
    @Transactional
    public void invitarUsuarioALista(Long usuarioId, Long listaRegalosId) {
        log.info("Invitar usuario ID {} a lista de regalos ID {}", usuarioId, listaRegalosId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        ListaRegalosEntity lista = listaRegalosRepository.findById(listaRegalosId)
                .orElseThrow(() -> new IllegalArgumentException("Lista de regalos no encontrada"));

        // No se puede invitar al creador de la lista
        if (lista.getCreador() != null && lista.getCreador().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("No se puede invitar al creador de la lista como invitado.");
        }

        // No puede haber duplicados
        if (lista.getInvitados().contains(usuario)) {
            throw new IllegalArgumentException("El usuario ya está invitado a la lista.");
        }

        lista.getInvitados().add(usuario);
        listaRegalosRepository.save(lista);
    }

    // Invitar usuario a celebración (con validaciones)
    @Transactional
    public void invitarUsuarioACelebracion(Long usuarioId, Long celebracionId) {
        log.info("Invitar usuario ID {} a celebración ID {}", usuarioId, celebracionId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        CelebracionEntity celebracion = celebracionRepository.findById(celebracionId)
                .orElseThrow(() -> new IllegalArgumentException("Celebración no encontrada"));

        // No se puede invitar al organizador
        if (celebracion.getOrganizador() != null && celebracion.getOrganizador().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("No se puede invitar al organizador como invitado.");
        }

        // No puede haber duplicados
        List<UsuarioEntity> invitados = celebracion.getInvitados();
        if (invitados != null && invitados.contains(usuario)) {
            throw new IllegalArgumentException("El usuario ya está invitado a la celebración.");
        }

        invitados.add(usuario);
        celebracionRepository.save(celebracion);
    }
}
