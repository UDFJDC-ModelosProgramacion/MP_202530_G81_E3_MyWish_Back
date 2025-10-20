package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(UsuarioListaRegalosService.class)
class UsuarioListaRegalosServiceTest {

    @Autowired
    private UsuarioListaRegalosService service;

    @Autowired
    private ListaRegalosRepository listaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<UsuarioEntity> usuarioList = new ArrayList<>();
    private List<ListaRegalosEntity> listaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ListaRegalosEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
    }

    private void insertData() {
        // Crear usuarios
        for (int i = 0; i < 3; i++) {
            UsuarioEntity usuario = factory.manufacturePojo(UsuarioEntity.class);
            entityManager.persist(usuario);
            usuarioList.add(usuario);
        }
        // Crear listas de regalos
        for (int i = 0; i < 3; i++) {
            ListaRegalosEntity lista = factory.manufacturePojo(ListaRegalosEntity.class);
            lista.setCreador(usuarioList.get(0)); // asignamos un creador fijo
            entityManager.persist(lista);
            listaList.add(lista);
        }
        // Asignar un invitado inicial a la primera lista
        listaList.get(0).getInvitados().add(usuarioList.get(1));
    }

    @Test
    void testAddInvitadoSuccess() {
        ListaRegalosEntity lista = listaList.get(0);
        UsuarioEntity invitado = usuarioList.get(2);

        ListaRegalosEntity resultado = service.addInvitado(lista.getId(), invitado.getId());

        assertTrue(resultado.getInvitados().contains(invitado));
    }

    @Test
    void testAddInvitadoSameAsCreadorThrows() {
        ListaRegalosEntity lista = listaList.get(0);
        UsuarioEntity creador = lista.getCreador();

        Long listaId = lista.getId();
        Long creadorId = creador.getId();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addInvitado(listaId, creadorId));
    }

    @Test
    void testAddInvitadoDuplicateThrows() {
        ListaRegalosEntity lista = listaList.get(0);
        UsuarioEntity invitadoExistente = usuarioList.get(1);

        Long listaId = lista.getId();
        Long invitadoId = invitadoExistente.getId();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addInvitado(listaId, invitadoId));
    }

    @Test
    void testRemoveInvitadoSuccess() {
        ListaRegalosEntity lista = listaList.get(0);
        UsuarioEntity invitado = usuarioList.get(1);

        ListaRegalosEntity resultado = service.removeInvitado(lista.getId(), invitado.getId());

        assertFalse(resultado.getInvitados().contains(invitado));
    }

    @Test
    void testIsUsuarioInvitadoTrue() {
        ListaRegalosEntity lista = listaList.get(0);
        UsuarioEntity invitado = usuarioList.get(1);

        assertTrue(service.isUsuarioInvitado(lista.getId(), invitado.getId()));
    }

    @Test
    void testIsUsuarioInvitadoFalse() {
        ListaRegalosEntity lista = listaList.get(0);
        UsuarioEntity noInvitado = usuarioList.get(2);

        assertFalse(service.isUsuarioInvitado(lista.getId(), noInvitado.getId()));
    }

    @Test
    void testAddInvitadoListaNotFoundThrows() {
        UsuarioEntity usuario = usuarioList.get(1);
        Long usuarioId = usuario.getId();
        Long listaId = 999L; // id inexistente

        assertThrows(
                EntityNotFoundException.class,
                () -> service.addInvitado(listaId, usuarioId));
    }

    @Test
    void testAddInvitadoUsuarioNotFoundThrows() {
        ListaRegalosEntity lista = listaList.get(0);
        Long listaId = lista.getId();
        Long usuarioIdInexistente = 999L;

        assertThrows(
                EntityNotFoundException.class,
                () -> service.addInvitado(listaId, usuarioIdInexistente));
    }

}
