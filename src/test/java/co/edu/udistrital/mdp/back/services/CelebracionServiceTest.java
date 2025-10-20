package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.entities.MensajeInvitacionEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(CelebracionService.class)
class CelebracionServiceTest {

    @Autowired
    private CelebracionService celebracionService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CelebracionEntity> celebracionList = new ArrayList<>();
    private List<UsuarioEntity> usuarioList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MensajeInvitacionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from CelebracionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            UsuarioEntity usuario = factory.manufacturePojo(UsuarioEntity.class);
            entityManager.persist(usuario);
            usuarioList.add(usuario);
        }

        for (int i = 0; i < 3; i++) {
            CelebracionEntity celebracion = factory.manufacturePojo(CelebracionEntity.class);
            celebracion.setFecha(LocalDate.now().plusDays(5));
            celebracion.setOrganizador(usuarioList.get(0));
            entityManager.persist(celebracion);
            celebracionList.add(celebracion);
        }
    }

    // =====================================================
    // CREATE TESTS
    // =====================================================

    @Test
    void testCreateCelebracion_Success() {
        CelebracionEntity nueva = factory.manufacturePojo(CelebracionEntity.class);
        nueva.setFecha(LocalDate.now().plusDays(10));
        nueva.setNombre("Cumpleaños de prueba");

        CelebracionEntity creada = celebracionService.createCelebracion(nueva, usuarioList.get(0).getId());
        assertNotNull(creada);
        assertEquals(nueva.getNombre(), creada.getNombre());
        assertEquals(usuarioList.get(0).getId(), creada.getOrganizador().getId());
    }

    @Test
    void testCreateCelebracion_SinNombre_LanzaExcepcion() {
        CelebracionEntity nueva = factory.manufacturePojo(CelebracionEntity.class);
        nueva.setNombre(null);
        nueva.setFecha(LocalDate.now().plusDays(5));

        assertThrows(IllegalArgumentException.class, () ->
                celebracionService.createCelebracion(nueva, usuarioList.get(0).getId()));
    }

    @Test
    void testCreateCelebracion_FechaAnterior_LanzaExcepcion() {
        CelebracionEntity nueva = factory.manufacturePojo(CelebracionEntity.class);
        nueva.setNombre("Evento pasado");
        nueva.setFecha(LocalDate.now().minusDays(1));

        assertThrows(IllegalArgumentException.class, () ->
                celebracionService.createCelebracion(nueva, usuarioList.get(0).getId()));
    }

    @Test
    void testCreateCelebracion_UsuarioNoExiste_LanzaExcepcion() {
        CelebracionEntity nueva = factory.manufacturePojo(CelebracionEntity.class);
        nueva.setNombre("Evento válido");
        nueva.setFecha(LocalDate.now().plusDays(3));

        assertThrows(EntityNotFoundException.class, () ->
                celebracionService.createCelebracion(nueva, 999L));
    }

    // =====================================================
    // UPDATE TESTS
    // =====================================================

    @Test
    void testUpdateCelebracion_Success() {
        CelebracionEntity existente = celebracionList.get(0);
        CelebracionEntity actualizada = factory.manufacturePojo(CelebracionEntity.class);
        actualizada.setNombre("Celebración actualizada");
        actualizada.setOrganizador(existente.getOrganizador());
        actualizada.setFecha(LocalDate.now().plusDays(3));

        CelebracionEntity result = celebracionService.updateCelebracion(existente.getId(), actualizada);
        assertEquals("Celebración actualizada", result.getNombre());
        assertEquals(existente.getOrganizador().getId(), result.getOrganizador().getId());
    }

    @Test
    void testUpdateCelebracion_NoExiste_LanzaExcepcion() {
        CelebracionEntity actualizada = factory.manufacturePojo(CelebracionEntity.class);
        assertThrows(EntityNotFoundException.class, () ->
                celebracionService.updateCelebracion(999L, actualizada));
    }

    @Test
    void testUpdateCelebracion_CambiaOrganizador_LanzaExcepcion() {
        CelebracionEntity existente = celebracionList.get(0);
        CelebracionEntity actualizada = factory.manufacturePojo(CelebracionEntity.class);
        actualizada.setOrganizador(usuarioList.get(1)); // diferente organizador

        assertThrows(IllegalStateException.class, () ->
                celebracionService.updateCelebracion(existente.getId(), actualizada));
    }

    // =====================================================
    // DELETE TESTS
    // =====================================================

    @Test
    void testDeleteCelebracion_Success() {
        CelebracionEntity celebracion = celebracionList.get(1);

        MensajeInvitacionEntity mensaje = factory.manufacturePojo(MensajeInvitacionEntity.class);
        mensaje.setCelebracion(celebracion);
        entityManager.persist(mensaje);

        celebracionService.deleteCelebracion(celebracion.getId());

        assertNull(entityManager.find(CelebracionEntity.class, celebracion.getId()));
        List<MensajeInvitacionEntity> mensajes = entityManager.getEntityManager()
                .createQuery("SELECT m FROM MensajeInvitacionEntity m WHERE m.celebracion.id = :id", MensajeInvitacionEntity.class)
                .setParameter("id", celebracion.getId())
                .getResultList();
        assertTrue(mensajes.isEmpty());
    }

    @Test
    void testDeleteCelebracion_NoExiste_LanzaExcepcion() {
        assertThrows(EntityNotFoundException.class, () ->
                celebracionService.deleteCelebracion(999L));
    }

    // =====================================================
    // GET TESTS
    // =====================================================

    @Test
    void testGetAllCelebraciones() {
        List<CelebracionEntity> lista = celebracionService.getAllCelebraciones();
        assertEquals(celebracionList.size(), lista.size());
    }

    @Test
    void testGetCelebracionById_Success() {
        CelebracionEntity celebracion = celebracionList.get(0);
        CelebracionEntity encontrada = celebracionService.getCelebracionById(celebracion.getId());
        assertEquals(celebracion.getId(), encontrada.getId());
    }

    @Test
    void testGetCelebracionById_NoExiste_LanzaExcepcion() {
        assertThrows(EntityNotFoundException.class, () ->
                celebracionService.getCelebracionById(999L));
    }
}
