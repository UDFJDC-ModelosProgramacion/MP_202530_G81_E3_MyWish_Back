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
import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
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
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioEntity resultado = usuarioService.crearUsuario(usuario);

        assertEquals(usuario.getCorreo(), resultado.getCorreo());
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
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuario.setCorreo("original@mail.com");

        UsuarioEntity actualizado = usuarioService.actualizarCorreoUsuario(1L, "nuevo@mail.com");
        assertEquals("nuevo@mail.com", actualizado.getCorreo());
    }

    @Test
    public void testEliminarUsuario_SinRelaciones_EliminaCorrectamente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(listaRegalosRepository.findByCreadorId(1L)).thenReturn(List.of());
        when(celebracionRepository.findByOrganizadorId(1L)).thenReturn(List.of());

        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    public void testEliminarUsuario_ConListas_LanzaExcepcion() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(listaRegalosRepository.findByCreadorId(1L)).thenReturn(List.of(new ListaRegalosEntity()));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            usuarioService.eliminarUsuario(1L);
        });
        assertEquals("No se puede eliminar un usuario con listas creadas activas.", exception.getMessage());
    }

    @Test
    public void testEliminarUsuario_ConCelebraciones_LanzaExcepcion() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(listaRegalosRepository.findByCreadorId(1L)).thenReturn(List.of());
        when(celebracionRepository.findByOrganizadorId(1L)).thenReturn(List.of(new CelebracionEntity()));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            usuarioService.eliminarUsuario(1L);
        });
        assertEquals("No se puede eliminar un usuario con celebraciones organizadas pendientes.", exception.getMessage());
    }
}
