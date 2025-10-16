package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;

class ListaRegalosUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ListaRegalosRepository listaRegalosRepository;

    @InjectMocks
    private ListaRegalosUsuarioService listaRegalosUsuarioService;

    private UsuarioEntity usuario;
    private ListaRegalosEntity lista;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        usuario = new UsuarioEntity();
        usuario.setId(1L);

        lista = new ListaRegalosEntity();
        lista.setId(10L);
        lista.getInvitados().add(usuario);
    }

    @Test
    void testObtenerListasDeUsuario_Success() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        usuario.setListasInvitado(List.of(lista));

        List<ListaRegalosEntity> listas = listaRegalosUsuarioService.obtenerListasDeUsuario(1L);
        assertEquals(1, listas.size());
        assertEquals(lista, listas.get(0));
    }

    @Test
    void testObtenerListasDeUsuario_UserNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> listaRegalosUsuarioService.obtenerListasDeUsuario(1L));
    }

    @Test
    void testEstaInvitadoEnLista_True() {
        when(listaRegalosRepository.findById(10L)).thenReturn(Optional.of(lista));
        assertTrue(listaRegalosUsuarioService.estaInvitadoEnLista(1L, 10L));
    }

    @Test
    void testEstaInvitadoEnLista_False() {
        when(listaRegalosRepository.findById(10L)).thenReturn(Optional.of(new ListaRegalosEntity()));
        assertFalse(listaRegalosUsuarioService.estaInvitadoEnLista(1L, 10L));
    }

    @Test
    void testRemoverInvitacion_Success() {
        when(listaRegalosRepository.findById(10L)).thenReturn(Optional.of(lista));
        listaRegalosUsuarioService.removerInvitacion(1L, 10L);
        verify(listaRegalosRepository).save(lista);
        assertTrue(lista.getInvitados().isEmpty());
    }

    @Test
    void testRemoverInvitacion_NotInvited() {
        when(listaRegalosRepository.findById(10L)).thenReturn(Optional.of(new ListaRegalosEntity()));
        assertThrows(IllegalArgumentException.class, () -> listaRegalosUsuarioService.removerInvitacion(1L, 10L));
    }
}
