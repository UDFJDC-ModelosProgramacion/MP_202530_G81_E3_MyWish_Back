package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;
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
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(ListaRegalosService.class)
class ListaRegalosServiceTest {

    @Autowired
    private ListaRegalosService listaRegalosService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ListaRegalosRepository listaRegalosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<UsuarioEntity> usuarioList = new ArrayList<>();
    private List<ListaRegalosEntity> listaRegalosList = new ArrayList<>();

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
        for (int i = 0; i < 3; i++) {
            UsuarioEntity usuario = factory.manufacturePojo(UsuarioEntity.class);
            entityManager.persist(usuario);
            usuarioList.add(usuario);
        }

        for (int i = 0; i < 3; i++) {
            ListaRegalosEntity lista = factory.manufacturePojo(ListaRegalosEntity.class);
            lista.setCreador(usuarioList.get(0));
            lista.setFecha(new Date(System.currentTimeMillis() + 86400000)); // mañana
            lista.setNombre("Lista " + i);
            entityManager.persist(lista);
            listaRegalosList.add(lista);
        }
    }

    // =====================================================
    // CREATE TESTS
    // =====================================================

    @Test
    void testCreateListaRegalos_Success() {
        ListaRegalosEntity nueva = factory.manufacturePojo(ListaRegalosEntity.class);
        nueva.setCreador(usuarioList.get(0));
        nueva.setFecha(new Date(System.currentTimeMillis() + 86400000)); // futura
        nueva.setNombre("Cumpleaños sorpresa");

        ListaRegalosEntity creada = listaRegalosService.createListaRegalos(nueva);
        assertNotNull(creada);
        assertEquals(nueva.getNombre(), creada.getNombre());
        assertEquals(usuarioList.get(0).getId(), creada.getCreador().getId());
    }

    @Test
    void testCreateListaRegalos_SinNombre_LanzaExcepcion() {
        ListaRegalosEntity nueva = factory.manufacturePojo(ListaRegalosEntity.class);
        nueva.setNombre(null);
        nueva.setCreador(usuarioList.get(0));
        nueva.setFecha(new Date(System.currentTimeMillis() + 86400000));

        assertThrows(IllegalArgumentException.class, () -> listaRegalosService.createListaRegalos(nueva));
    }

    @Test
    void testCreateListaRegalos_FechaPasada_LanzaExcepcion() {
        ListaRegalosEntity nueva = factory.manufacturePojo(ListaRegalosEntity.class);
        nueva.setNombre("Navidad");
        nueva.setFecha(new Date(System.currentTimeMillis() - 86400000)); // ayer
        nueva.setCreador(usuarioList.get(0));

        assertThrows(IllegalArgumentException.class, () -> listaRegalosService.createListaRegalos(nueva));
    }

    @Test
    void testCreateListaRegalos_CreadorNoExiste_LanzaExcepcion() {
        UsuarioEntity falso = factory.manufacturePojo(UsuarioEntity.class);
        falso.setId(999L);

        ListaRegalosEntity nueva = factory.manufacturePojo(ListaRegalosEntity.class);
        nueva.setNombre("Fiesta");
        nueva.setFecha(new Date(System.currentTimeMillis() + 86400000));
        nueva.setCreador(falso);

        assertThrows(EntityNotFoundException.class, () -> listaRegalosService.createListaRegalos(nueva));
    }

    // =====================================================
    // UPDATE TESTS
    // =====================================================

    @Test
    void testUpdateListaRegalos_Success() {
        ListaRegalosEntity existente = listaRegalosList.get(0);
        ListaRegalosEntity actualizada = factory.manufacturePojo(ListaRegalosEntity.class);
        actualizada.setCreador(existente.getCreador());
        actualizada.setNombre("Lista actualizada");
        actualizada.setFecha(new Date(System.currentTimeMillis() + 86400000));

        ListaRegalosEntity result = listaRegalosService.updateListaRegalos(existente.getId(), actualizada);
        assertEquals("Lista actualizada", result.getNombre());
    }

    @Test
    void testUpdateListaRegalos_NoExiste_LanzaExcepcion() {
        ListaRegalosEntity actualizada = factory.manufacturePojo(ListaRegalosEntity.class);
        actualizada.setCreador(usuarioList.get(0));
        assertThrows(EntityNotFoundException.class, () -> listaRegalosService.updateListaRegalos(999L, actualizada));
    }

    @Test
    void testUpdateListaRegalos_CambiaCreador_LanzaExcepcion() {
        ListaRegalosEntity existente = listaRegalosList.get(0);
        ListaRegalosEntity actualizada = factory.manufacturePojo(ListaRegalosEntity.class);
        actualizada.setCreador(usuarioList.get(1)); // otro creador

        Long listaId = existente.getId();

        assertThrows(IllegalArgumentException.class,
                () -> listaRegalosService.updateListaRegalos(listaId, actualizada));
    }

    @Test
    void testUpdateListaRegalos_FechaAnterior_LanzaExcepcion() {
        ListaRegalosEntity existente = listaRegalosList.get(0);

        ListaRegalosEntity actualizada = factory.manufacturePojo(ListaRegalosEntity.class);
        actualizada.setCreador(existente.getCreador());
        actualizada.setFecha(new Date(System.currentTimeMillis() - 86400000)); // ayer

        Long listaId = existente.getId();

        assertThrows(IllegalArgumentException.class,
                () -> listaRegalosService.updateListaRegalos(listaId, actualizada));
    }

    // =====================================================
    // DELETE TESTS
    // =====================================================

    @Test
    void testDeleteListaRegalos_Success() {
        ListaRegalosEntity lista = listaRegalosList.get(1);
        listaRegalosService.deleteListaRegalos(lista.getId());
        assertNull(entityManager.find(ListaRegalosEntity.class, lista.getId()));
    }

    @Test
    void testDeleteListaRegalos_NoExiste_LanzaExcepcion() {
        assertThrows(EntityNotFoundException.class, () -> listaRegalosService.deleteListaRegalos(999L));
    }

    // =====================================================
    // GET TESTS
    // =====================================================

    @Test
    void testGetAllListas() {
        List<ListaRegalosEntity> listas = listaRegalosService.getAllListas();
        assertEquals(listaRegalosList.size(), listas.size());
    }

    @Test
    void testGetListaById_Success() {
        ListaRegalosEntity lista = listaRegalosList.get(0);
        ListaRegalosEntity encontrada = listaRegalosService.getListaById(lista.getId());
        assertEquals(lista.getId(), encontrada.getId());
    }

    @Test
    void testGetListaById_NoExiste_LanzaExcepcion() {
        assertThrows(EntityNotFoundException.class, () -> listaRegalosService.getListaById(999L));
    }

    @Test
    void testGetListasByCreador() {
        List<ListaRegalosEntity> listas = listaRegalosService.getListasByCreador(usuarioList.get(0).getId());
        assertFalse(listas.isEmpty());
        assertEquals(usuarioList.get(0).getId(), listas.get(0).getCreador().getId());
    }
}
