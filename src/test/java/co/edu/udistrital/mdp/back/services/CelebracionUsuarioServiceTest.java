package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.CelebracionRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;

public class CelebracionUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CelebracionRepository celebracionRepository;

    @InjectMocks
    private CelebracionUsuarioService celebracionUsuarioService;

    private UsuarioEntity usuario;
    private CelebracionEntity celebracion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        usuario = new UsuarioEntity();
        usuario.setId(1L);

        celebracion = new CelebracionEntity();
        celebracion.setId(20L);

        // Inicializar lista de invitados para evitar NullPointerException
        celebracion.setInvitados(new ArrayList<>());

        celebracion.getInvitados().add(usuario);
    }

    @Test
    public void testObtenerCelebracionesDeUsuario_Success() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        usuario.setCelebracionesInvitado(List.of(celebracion));

        List<CelebracionEntity> celebraciones = celebracionUsuarioService.obtenerCelebracionesDeUsuario(1L);
        assertEquals(1, celebraciones.size());
        assertEquals(celebracion, celebraciones.get(0));
    }

    @Test
    public void testEstaInvitadoEnCelebracion_True() {
        when(celebracionRepository.findById(20L)).thenReturn(Optional.of(celebracion));
        assertTrue(celebracionUsuarioService.estaInvitadoEnCelebracion(1L, 20L));
    }

    @Test
    public void testEstaInvitadoEnCelebracion_False() {
        when(celebracionRepository.findById(20L)).thenReturn(Optional.of(new CelebracionEntity()));
        assertFalse(celebracionUsuarioService.estaInvitadoEnCelebracion(1L, 20L));
    }

    @Test
    public void testRemoverInvitacion_Success() {
        when(celebracionRepository.findById(20L)).thenReturn(Optional.of(celebracion));
        celebracionUsuarioService.removerInvitacion(1L, 20L);
        verify(celebracionRepository).save(celebracion);
        assertTrue(celebracion.getInvitados().isEmpty());
    }

    @Test
    public void testRemoverInvitacion_NotInvited() {
        when(celebracionRepository.findById(20L)).thenReturn(Optional.of(new CelebracionEntity()));
        assertThrows(IllegalArgumentException.class, () -> celebracionUsuarioService.removerInvitacion(1L, 20L));
    }
}
