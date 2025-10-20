package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(UsuarioCelebracionService.class)
class UsuarioCelebracionServiceTest {

    @Autowired
    private UsuarioCelebracionService usuarioCelebracionService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<UsuarioEntity> usuarioList = new ArrayList<>();
    private List<CelebracionEntity> celebracionList = new ArrayList<>();

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
        // Crear usuarios
        for (int i = 0; i < 3; i++) {
            UsuarioEntity usuario = factory.manufacturePojo(UsuarioEntity.class);
            entityManager.persist(usuario);
            usuarioList.add(usuario);
        }

        // Crear celebraciones
        for (int i = 0; i < 3; i++) {
            CelebracionEntity celebracion = factory.manufacturePojo(CelebracionEntity.class);
            celebracion.setOrganizador(usuarioList.get(0)); // Todos organizados por el primer usuario
            entityManager.persist(celebracion);
            celebracionList.add(celebracion);
        }

        // Agregar un invitado inicial
        celebracionList.get(0).getInvitados().add(usuarioList.get(1));
    }

    @Test
    void testAddInvitadoExitoso() {
        CelebracionEntity celebracion = celebracionList.get(1);
        UsuarioEntity invitado = usuarioList.get(2);

        CelebracionEntity updated = usuarioCelebracionService.addInvitado(celebracion.getId(), invitado.getId());

        assertTrue(updated.getInvitados().contains(invitado));
    }

    @Test
    void testAddInvitadoMismoOrganizador() {
        CelebracionEntity celebracion = celebracionList.get(0);
        UsuarioEntity organizador = usuarioList.get(0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioCelebracionService.addInvitado(celebracion.getId(), organizador.getId()));

        assertEquals("El invitado no puede ser el mismo usuario que el organizador de la celebración.", exception.getMessage());
    }

    @Test
    void testAddInvitadoDuplicado() {
        CelebracionEntity celebracion = celebracionList.get(0);
        UsuarioEntity invitadoExistente = usuarioList.get(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioCelebracionService.addInvitado(celebracion.getId(), invitadoExistente.getId()));

        assertEquals("El usuario ya está invitado a esta celebración.", exception.getMessage());
    }

    @Test
    void testRemoveInvitadoExitoso() {
        CelebracionEntity celebracion = celebracionList.get(0);
        UsuarioEntity invitado = usuarioList.get(1);

        CelebracionEntity updated = usuarioCelebracionService.removeInvitado(celebracion.getId(), invitado.getId());

        assertFalse(updated.getInvitados().contains(invitado));
    }

    @Test
    void testRemoveInvitadoUsuarioNoExiste() {
        CelebracionEntity celebracion = celebracionList.get(0);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> usuarioCelebracionService.removeInvitado(celebracion.getId(), 999L));

        assertEquals("Usuario no encontrado con id 999", exception.getMessage());
    }

    @Test
    void testIsUsuarioInvitado() {
        CelebracionEntity celebracion = celebracionList.get(0);
        UsuarioEntity invitado = usuarioList.get(1);
        UsuarioEntity noInvitado = usuarioList.get(2);

        assertTrue(usuarioCelebracionService.isUsuarioInvitado(celebracion.getId(), invitado.getId()));
        assertFalse(usuarioCelebracionService.isUsuarioInvitado(celebracion.getId(), noInvitado.getId()));
    }
}
