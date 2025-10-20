package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.CelebracionRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Transactional
@Import(CelebracionUsuarioService.class)
class CelebracionUsuarioServiceTest {

    @Autowired
    private CelebracionUsuarioService celebracionUsuarioService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CelebracionRepository celebracionRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private UsuarioEntity usuario;
    private CelebracionEntity celebracion;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM CelebracionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM UsuarioEntity").executeUpdate();
    }

    private void insertData() {
        // Crear usuario
        usuario = factory.manufacturePojo(UsuarioEntity.class);
        usuario.setCelebracionesInvitado(new ArrayList<>());
        entityManager.persist(usuario);

        // Crear celebración
        celebracion = factory.manufacturePojo(CelebracionEntity.class);
        celebracion.setInvitados(new ArrayList<>());
        celebracion.getInvitados().add(usuario);
        entityManager.persist(celebracion);

        // Relación inversa (usuario -> celebración)
        usuario.getCelebracionesInvitado().add(celebracion);

        entityManager.flush();
    }

    @Test
    void testObtenerCelebracionesDeUsuario_Success() {
        List<CelebracionEntity> celebraciones = celebracionUsuarioService.obtenerCelebracionesDeUsuario(usuario.getId());

        assertNotNull(celebraciones);
        assertEquals(1, celebraciones.size());
        assertEquals(celebracion.getId(), celebraciones.get(0).getId());
    }

    @Test
    void testEstaInvitadoEnCelebracion_True() {
        boolean resultado = celebracionUsuarioService.estaInvitadoEnCelebracion(usuario.getId(), celebracion.getId());
        assertTrue(resultado);
    }

    @Test
    void testEstaInvitadoEnCelebracion_False() {
        UsuarioEntity otroUsuario = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(otroUsuario);

        boolean resultado = celebracionUsuarioService.estaInvitadoEnCelebracion(otroUsuario.getId(), celebracion.getId());
        assertFalse(resultado);
    }

    @Test
    void testRemoverInvitacion_Success() {
        celebracionUsuarioService.removerInvitacion(usuario.getId(), celebracion.getId());

        CelebracionEntity celebracionActualizada = celebracionRepository.findById(celebracion.getId())
                .orElseThrow(() -> new EntityNotFoundException("Celebración no encontrada"));

        assertTrue(celebracionActualizada.getInvitados().isEmpty());
    }

    @Test
    void testRemoverInvitacion_NotInvited() {
        UsuarioEntity otro = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(otro);

        assertThrows(IllegalArgumentException.class, () -> 
            celebracionUsuarioService.removerInvitacion(otro.getId(), celebracion.getId())
        );
    }
}
