package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ListaRegalosUsuarioService.class)
class ListaRegalosUsuarioServiceTest {

    @Autowired
    private ListaRegalosUsuarioService listaRegalosUsuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ListaRegalosRepository listaRegalosRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<UsuarioEntity> usuarios = new ArrayList<>();
    private List<ListaRegalosEntity> listas = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ListaRegalosEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
    }

    // Versión corregida de insertData() con relación bidireccional
    private void insertData() {
        // Crear usuario
        UsuarioEntity usuario = factory.manufacturePojo(UsuarioEntity.class);
        if (usuario.getListasInvitado() == null) {
            usuario.setListasInvitado(new ArrayList<>());
        }
        entityManager.persist(usuario);
        usuarios.add(usuario);

        // Crear lista y asociarla al usuario
        ListaRegalosEntity lista = factory.manufacturePojo(ListaRegalosEntity.class);
        if (lista.getInvitados() == null) {
            lista.setInvitados(new ArrayList<>());
        }

        // Vinculación bidireccional
        lista.getInvitados().add(usuario);
        usuario.getListasInvitado().add(lista);

        entityManager.persist(lista);
        entityManager.merge(usuario); // actualiza relación en la DB

        listas.add(lista);
    }

    @Test
    void testObtenerListasDeUsuario_Success() {
        Long usuarioId = usuarios.get(0).getId();
        List<ListaRegalosEntity> result = listaRegalosUsuarioService.obtenerListasDeUsuario(usuarioId);
        assertEquals(1, result.size());
        assertEquals(listas.get(0).getId(), result.get(0).getId());
    }

    @Test
    void testObtenerListasDeUsuario_UserNotFound() {
        assertThrows(EntityNotFoundException.class, 
            () -> listaRegalosUsuarioService.obtenerListasDeUsuario(999L));
    }

    @Test
    void testEstaInvitadoEnLista_True() {
        Long usuarioId = usuarios.get(0).getId();
        Long listaId = listas.get(0).getId();
        assertTrue(listaRegalosUsuarioService.estaInvitadoEnLista(usuarioId, listaId));
    }

    @Test
    void testEstaInvitadoEnLista_False() {
        Long usuarioId = usuarios.get(0).getId();
        ListaRegalosEntity nuevaLista = factory.manufacturePojo(ListaRegalosEntity.class);
        if (nuevaLista.getInvitados() == null) {
            nuevaLista.setInvitados(new ArrayList<>());
        }
        entityManager.persist(nuevaLista);
        assertFalse(listaRegalosUsuarioService.estaInvitadoEnLista(usuarioId, nuevaLista.getId()));
    }

    @Test
    void testRemoverInvitacion_Success() {
        Long usuarioId = usuarios.get(0).getId();
        Long listaId = listas.get(0).getId();

        listaRegalosUsuarioService.removerInvitacion(usuarioId, listaId);

        ListaRegalosEntity listaActualizada = listaRegalosRepository.findById(listaId).get();
        assertTrue(listaActualizada.getInvitados().isEmpty());
    }

    @Test
    void testRemoverInvitacion_NotInvited() {
        UsuarioEntity otroUsuario = factory.manufacturePojo(UsuarioEntity.class);
        if (otroUsuario.getListasInvitado() == null) {
            otroUsuario.setListasInvitado(new ArrayList<>());
        }
        entityManager.persist(otroUsuario);
        Long otroUsuarioId = otroUsuario.getId();
        Long listaId = listas.get(0).getId();

        assertThrows(IllegalArgumentException.class, 
            () -> listaRegalosUsuarioService.removerInvitacion(otroUsuarioId, listaId));
    }
}
