package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.PrioridadRegaloEntity;
import co.edu.udistrital.mdp.back.entities.RegaloEntity;
import co.edu.udistrital.mdp.back.repositories.PrioridadRegaloRepository;
import co.edu.udistrital.mdp.back.repositories.RegaloRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(PrioridadRegaloService.class)
class PrioridadRegaloServiceTest {

    @Autowired
    private PrioridadRegaloService prioridadRegaloService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PrioridadRegaloRepository prioridadRegaloRepository;

    @Autowired
    private RegaloRepository regaloRepository;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<PrioridadRegaloEntity> prioridadList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from RegaloEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PrioridadRegaloEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            PrioridadRegaloEntity prioridad = factory.manufacturePojo(PrioridadRegaloEntity.class);
            prioridad.setNombre("Prioridad " + i);
            prioridad.setNivel(i + 1);
            prioridad.setEsPorDefecto(false);
            entityManager.persist(prioridad);
            prioridadList.add(prioridad);
        }
    }

    // =====================================================
    // CREATE TESTS
    // =====================================================

    @Test
    void testCreatePrioridadRegalo_Success() {
        PrioridadRegaloEntity nueva = factory.manufacturePojo(PrioridadRegaloEntity.class);
        nueva.setNombre("Alta");
        nueva.setNivel(5);
        nueva.setDescripcion("Prioridad alta");
        nueva.setColor("#FF0000");

        PrioridadRegaloEntity creada = prioridadRegaloService.createPrioridadRegalo(nueva);
        
        assertNotNull(creada);
        assertNotNull(creada.getId());
        assertEquals("Alta", creada.getNombre());
        assertEquals(5, creada.getNivel());
    }

    @Test
    void testCreatePrioridadRegalo_NivelMenorA1_LanzaExcepcion() {
        PrioridadRegaloEntity nueva = factory.manufacturePojo(PrioridadRegaloEntity.class);
        nueva.setNombre("Invalida");
        nueva.setNivel(0);

        assertThrows(IllegalArgumentException.class, () ->
                prioridadRegaloService.createPrioridadRegalo(nueva));
    }

    @Test
    void testCreatePrioridadRegalo_NivelMayorA10_LanzaExcepcion() {
        PrioridadRegaloEntity nueva = factory.manufacturePojo(PrioridadRegaloEntity.class);
        nueva.setNombre("Invalida");
        nueva.setNivel(11);

        assertThrows(IllegalArgumentException.class, () ->
                prioridadRegaloService.createPrioridadRegalo(nueva));
    }

    @Test
    void testCreatePrioridadRegalo_NivelNull_LanzaExcepcion() {
        PrioridadRegaloEntity nueva = factory.manufacturePojo(PrioridadRegaloEntity.class);
        nueva.setNombre("Invalida");
        nueva.setNivel(null);

        assertThrows(IllegalArgumentException.class, () ->
                prioridadRegaloService.createPrioridadRegalo(nueva));
    }

    @Test
    void testCreatePrioridadRegalo_NombreDuplicado_LanzaExcepcion() {
        PrioridadRegaloEntity nueva = factory.manufacturePojo(PrioridadRegaloEntity.class);
        nueva.setNombre(prioridadList.get(0).getNombre());
        nueva.setNivel(5);

        assertThrows(IllegalArgumentException.class, () ->
                prioridadRegaloService.createPrioridadRegalo(nueva));
    }

    @Test
    void testCreatePrioridadRegalo_NivelDuplicado_LanzaExcepcion() {
        PrioridadRegaloEntity nueva = factory.manufacturePojo(PrioridadRegaloEntity.class);
        nueva.setNombre("Nueva Prioridad");
        nueva.setNivel(prioridadList.get(0).getNivel());

        assertThrows(IllegalArgumentException.class, () ->
                prioridadRegaloService.createPrioridadRegalo(nueva));
    }

    @Test
    void testCreatePrioridadRegalo_Esencial_EsPorDefectoTrue() {
        PrioridadRegaloEntity nueva = factory.manufacturePojo(PrioridadRegaloEntity.class);
        nueva.setNombre("Esencial");
        nueva.setNivel(5);
        nueva.setEsPorDefecto(false);

        PrioridadRegaloEntity creada = prioridadRegaloService.createPrioridadRegalo(nueva);
        
        assertTrue(creada.getEsPorDefecto());
    }

    // =====================================================
    // UPDATE TESTS
    // =====================================================

    @Test
    void testUpdatePrioridadRegalo_Success() {
        PrioridadRegaloEntity existente = prioridadList.get(0);
        PrioridadRegaloEntity actualizada = factory.manufacturePojo(PrioridadRegaloEntity.class);
        actualizada.setDescripcion("Descripción actualizada");
        actualizada.setColor("#00FF00");

        PrioridadRegaloEntity resultado = prioridadRegaloService.updatePrioridadRegalo(existente.getId(), actualizada);
        
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        assertEquals("#00FF00", resultado.getColor());
    }

    @Test
    void testUpdatePrioridadRegalo_NoExiste_LanzaExcepcion() {
        PrioridadRegaloEntity actualizada = factory.manufacturePojo(PrioridadRegaloEntity.class);
        
        assertThrows(EntityNotFoundException.class, () ->
                prioridadRegaloService.updatePrioridadRegalo(999L, actualizada));
    }

    @Test
    void testUpdatePrioridadRegalo_NivelConRegalosAsociados_LanzaExcepcion() {
        PrioridadRegaloEntity existente = prioridadList.get(0);
        
        // Crear un regalo asociado a la prioridad
        RegaloEntity regalo = factory.manufacturePojo(RegaloEntity.class);
        regalo.setPrioridad(existente);
        entityManager.persist(regalo);
        entityManager.flush();

        PrioridadRegaloEntity actualizada = factory.manufacturePojo(PrioridadRegaloEntity.class);
        actualizada.setNivel(7);

        assertThrows(IllegalStateException.class, () ->
                prioridadRegaloService.updatePrioridadRegalo(existente.getId(), actualizada));
    }

    @Test
    void testUpdatePrioridadRegalo_NivelDuplicado_LanzaExcepcion() {
        PrioridadRegaloEntity existente = prioridadList.get(0);
        PrioridadRegaloEntity actualizada = factory.manufacturePojo(PrioridadRegaloEntity.class);
        actualizada.setNivel(prioridadList.get(1).getNivel());

        assertThrows(IllegalArgumentException.class, () ->
                prioridadRegaloService.updatePrioridadRegalo(existente.getId(), actualizada));
    }

    // =====================================================
    // DELETE TESTS
    // =====================================================

    @Test
    void testDeletePrioridadRegalo_Success() {
        PrioridadRegaloEntity prioridad = prioridadList.get(1);
        
        prioridadRegaloService.deletePrioridadRegalo(prioridad.getId());
        
        assertNull(entityManager.find(PrioridadRegaloEntity.class, prioridad.getId()));
    }

    @Test
    void testDeletePrioridadRegalo_NoExiste_LanzaExcepcion() {
        assertThrows(EntityNotFoundException.class, () ->
                prioridadRegaloService.deletePrioridadRegalo(999L));
    }

    @Test
    void testDeletePrioridadRegalo_ConRegalosAsociados_LanzaExcepcion() {
        PrioridadRegaloEntity prioridad = prioridadList.get(0);
        
        // Crear un regalo asociado a la prioridad
        RegaloEntity regalo = factory.manufacturePojo(RegaloEntity.class);
        regalo.setPrioridad(prioridad);
        entityManager.persist(regalo);
        entityManager.flush();

        assertThrows(IllegalStateException.class, () ->
                prioridadRegaloService.deletePrioridadRegalo(prioridad.getId()));
    }

    @Test
    void testDeletePrioridadRegalo_PorDefecto_LanzaExcepcion() {
        PrioridadRegaloEntity prioridad = prioridadList.get(0);
        prioridad.setEsPorDefecto(true);
        entityManager.persist(prioridad);
        entityManager.flush();

        assertThrows(IllegalStateException.class, () ->
                prioridadRegaloService.deletePrioridadRegalo(prioridad.getId()));
    }

    // =====================================================
    // GET TESTS
    // =====================================================

    @Test
    void testGetAllPrioridadesRegalo() {
        List<PrioridadRegaloEntity> lista = prioridadRegaloService.getAllPrioridadesRegalo();
        
        assertEquals(prioridadList.size(), lista.size());
    }

    @Test
    void testGetPrioridadRegaloById_Success() {
        PrioridadRegaloEntity prioridad = prioridadList.get(0);
        
        PrioridadRegaloEntity encontrada = prioridadRegaloService.getPrioridadRegaloById(prioridad.getId());
        
        assertEquals(prioridad.getId(), encontrada.getId());
        assertEquals(prioridad.getNombre(), encontrada.getNombre());
    }

    @Test
    void testGetPrioridadRegaloById_NoExiste_LanzaExcepcion() {
        assertThrows(EntityNotFoundException.class, () ->
                prioridadRegaloService.getPrioridadRegaloById(999L));
    }

    @Test
    void testGetPrioridadesPorNivel() {
        Integer nivel = prioridadList.get(0).getNivel();
        
        List<PrioridadRegaloEntity> lista = prioridadRegaloService.getPrioridadesPorNivel(nivel);
        
        assertFalse(lista.isEmpty());
        assertEquals(nivel, lista.get(0).getNivel());
    }
}