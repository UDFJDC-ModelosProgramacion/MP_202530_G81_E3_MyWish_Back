package co.edu.udistrital.mdp.back.services;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ListaRegalosUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ListaRegalosRepository listaRegalosRepository;

    public List<ListaRegalosEntity> obtenerListasDeUsuario(Long usuarioId) {
        log.info("Obteniendo listas de regalos del usuario {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id " + usuarioId));

        return usuario.getListasInvitado();
    }

    public boolean estaInvitadoEnLista(Long usuarioId, Long listaId) {
        log.info("Verificando invitación del usuario {} en lista {}", usuarioId, listaId);

        ListaRegalosEntity lista = listaRegalosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException("Lista no encontrada con id " + listaId));

        return lista.getInvitados().stream()
                .anyMatch(u -> u.getId().equals(usuarioId));
    }

    @Transactional
    public void removerInvitacion(Long usuarioId, Long listaId) {
        log.info("Removiendo invitación de usuario {} en lista {}", usuarioId, listaId);

        ListaRegalosEntity lista = listaRegalosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException("Lista no encontrada con id " + listaId));

        boolean eliminado = lista.getInvitados().removeIf(u -> u.getId().equals(usuarioId));

        if (!eliminado) {
            throw new IllegalArgumentException("Usuario no invitado en esta lista.");
        }

        listaRegalosRepository.save(lista);
    }
}   