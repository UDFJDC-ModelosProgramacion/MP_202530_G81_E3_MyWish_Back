package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;
import co.edu.udistrital.mdp.back.repositories.CelebracionRepository;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ListaRegalosRepository listaRegalosRepository;

    @Mock
    private CelebracionRepository celebracionRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioEntity usuario;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setCorreo("test@example.com");
        usuario.setFechaNacimiento(new Date(System.currentTimeMillis() - 100000)); // fecha pasada
    }

    @Test
    public void testCrearUsuario_Valido() {
        when(usuarioRepository.findByCorreo(usuario.getCorreo())).thenReturn(null);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        UsuarioEntity resultado = usuarioService.crearUsuario(usuario);
        assertEquals(usuario, resultado);
    }

    @Test
    public void testCrearUsuario_CorreoNulo() {
        usuario.setCorreo(null);
        assertThrows(IllegalArgumentException.class, () -> usuarioService.crearUsuario(usuario));
    }

    @Test
    public void testActualizarCorreoUsuario_Exitoso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByCorreo("nuevo@mail.com")).thenReturn(null);
        usuario.setCorreo("original@mail.com");

        UsuarioEntity actualizado = usuarioService.actualizarCorreoUsuario(1L, "nuevo@mail.com");
        assertEquals("nuevo@mail.com", actualizado.getCorreo());
    }

    @Test
    public void testEliminarUsuario_SinRelaciones() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(listaRegalosRepository.findByCreadorId(1L)).thenReturn(List.of());
        when(celebracionRepository.findByOrganizadorId(1L)).thenReturn(List.of());

        usuarioService.eliminarUsuario(1L);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    public void testEliminarUsuario_ConListas() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(listaRegalosRepository.findByCreadorId(1L)).thenReturn(List.of(new ListaRegalosEntity()));

        assertThrows(IllegalStateException.class, () -> usuarioService.eliminarUsuario(1L));
    }
}
