package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.EstadoCompraEntity;
import co.edu.udistrital.mdp.back.entities.RegaloEntity;
import co.edu.udistrital.mdp.back.repositories.EstadoCompraRepository;
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
@Import(EstadoCompraService.class)
class EstadoCompraServiceTest {

    @Autowired
    private EstadoCompraService estadoCompraService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EstadoCompraRepository estadoCompraRepository;

    @Autowired
    private RegaloRepository regaloRepository;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<EstadoCompraEntity> estadoCompraList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from RegaloEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EstadoCompraEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            EstadoCompraEntity estado = factory.manufacturePojo(EstadoCompraEntity.class);
            estado.setNombre("Estado " + i);
            estado.setEsPorDefecto(false);
            entityManager.persist(estado);
            estadoCompraList.add(estado);
        }
    }

    // =====================================================
    // CREATE TESTS
    // =====================================================

    @Test
    void testCreateEstadoCompra_Success() {
        EstadoCompraEntity nuevo = factory.manufacturePojo(EstadoCompraEntity.class);
        nuevo.setNombre("Comprado");
        nuevo.setDescripcion("Regalo ya comprado");
        nuevo.setColor("#00FF00");

        EstadoCompraEntity creado = estadoCompraService.createEstadoCompra(nuevo);
        
        assertNotNull(creado);
        assertNotNull(creado.getId());
        assertEquals("Comprado", creado.getNombre());
        assertEquals("Regalo ya comprado", creado.getDescripcion());
    }

    @Test
    void testCreateEstadoCompra_SinNombre_LanzaExcepcion() {
        EstadoCompraEntity nuevo = factory.manufacturePojo(EstadoCompraEntity.class);
        nuevo.setNombre(null);

        assertThrows(IllegalArgumentException.class, () ->
                estadoCompraService.createEstadoCompra(nuevo));
    }

    @Test
    void testCreateEstadoCompra_NombreVacio_LanzaExcepcion() {
        EstadoCompraEntity nuevo = factory.manufacturePojo(EstadoCompraEntity.class);
        nuevo.setNombre("   ");

        assertThrows(IllegalArgumentException.class, () ->
                estadoCompraService.createEstadoCompra(nuevo));
    }

    @Test
    void testCreateEstadoCompra_NombreDuplicado_LanzaExcepcion() {
        EstadoCompraEntity nuevo = factory.manufacturePojo(EstadoCompraEntity.class);
        nuevo.setNombre(estadoCompraList.get(0).getNombre());

        assertThrows(IllegalArgumentException.class, () ->
                estadoCompraService.createEstadoCompra(nuevo));
    }

    @Test
    void testCreateEstadoCompra_Pendiente_EsPorDefectoTrue() {
        EstadoCompraEntity nuevo = factory.manufacturePojo(EstadoCompraEntity.class);
        nuevo.setNombre("Pendiente");
        nuevo.setEsPorDefecto(false);

        EstadoCompraEntity creado = estadoCompraService.createEstadoCompra(nuevo);
        
        assertTrue(creado.getEsPorDefecto());
    }

    // =====================================================
    // UPDATE TESTS
    // =====================================================

    @Test
    void testUpdateEstadoCompra_Success() {
        EstadoCompraEntity existente = estadoCompraList.get(0);
        EstadoCompraEntity actualizado = factory.manufacturePojo(EstadoCompraEntity.class);
        actualizado.setDescripcion("Descripción actualizada");
        actualizado.setColor("#FF0000");

        EstadoCompraEntity resultado = estadoCompraService.updateEstadoCompra(existente.getId(), actualizado);
        
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        assertEquals("#FF0000", resultado.getColor());
    }

    @Test
    void testUpdateEstadoCompra_NoExiste_LanzaExcepcion() {
        EstadoCompraEntity actualizado = factory.manufacturePojo(EstadoCompraEntity.class);
        
        assertThrows(EntityNotFoundException.class, () ->
                estadoCompraService.updateEstadoCompra(999L, actualizado));
    }

    @Test
    void testUpdateEstadoCompra_NombreConRegalosAsociados_LanzaExcepcion() {
        EstadoCompraEntity existente = estadoCompraList.get(0);
        
        // Crear un regalo asociado al estado
        RegaloEntity regalo = factory.manufacturePojo(RegaloEntity.class);
        regalo.setEstadoCompra(existente);
        entityManager.persist(regalo);
        entityManager.flush();

        EstadoCompraEntity actualizado = factory.manufacturePojo(EstadoCompraEntity.class);
        actualizado.setNombre("Nuevo Nombre");

        assertThrows(IllegalStateException.class, () ->
                estadoCompraService.updateEstadoCompra(existente.getId(), actualizado));
    }

    @Test
    void testUpdateEstadoCompra_NombreDuplicado_LanzaExcepcion() {
        EstadoCompraEntity existente = estadoCompraList.get(0);
        EstadoCompraEntity actualizado = factory.manufacturePojo(EstadoCompraEntity.class);
        actualizado.setNombre(estadoCompraList.get(1).getNombre());

        assertThrows(IllegalArgumentException.class, () ->
                estadoCompraService.updateEstadoCompra(existente.getId(), actualizado));
    }

    // =====================================================
    // DELETE TESTS
    // =====================================================

    @Test
    void testDeleteEstadoCompra_Success() {
        EstadoCompraEntity estado = estadoCompraList.get(1);
        
        estadoCompraService.deleteEstadoCompra(estado.getId());
        
        assertNull(entityManager.find(EstadoCompraEntity.class, estado.getId()));
    }

    @Test
    void testDeleteEstadoCompra_NoExiste_LanzaExcepcion() {
        assertThrows(EntityNotFoundException.class, () ->
                estadoCompraService.deleteEstadoCompra(999L));
    }

    @Test
    void testDeleteEstadoCompra_ConRegalosAsociados_LanzaExcepcion() {
        EstadoCompraEntity estado = estadoCompraList.get(0);
        
        // Crear un regalo asociado al estado
        RegaloEntity regalo = factory.manufacturePojo(RegaloEntity.class);
        regalo.setEstadoCompra(estado);
        entityManager.persist(regalo);
        entityManager.flush();

        assertThrows(IllegalStateException.class, () ->
                estadoCompraService.deleteEstadoCompra(estado.getId()));
    }

    @Test
    void testDeleteEstadoCompra_PorDefecto_LanzaExcepcion() {
        EstadoCompraEntity estado = estadoCompraList.get(0);
        estado.setEsPorDefecto(true);
        entityManager.persist(estado);
        entityManager.flush();

        assertThrows(IllegalStateException.class, () ->
                estadoCompraService.deleteEstadoCompra(estado.getId()));
    }

    // =====================================================
    // GET TESTS
    // =====================================================

    @Test
    void testGetAllEstadosCompra() {
        List<EstadoCompraEntity> lista = estadoCompraService.getAllEstadosCompra();
        
        assertEquals(estadoCompraList.size(), lista.size());
    }

    @Test
    void testGetEstadoCompraById_Success() {
        EstadoCompraEntity estado = estadoCompraList.get(0);
        
        EstadoCompraEntity encontrado = estadoCompraService.getEstadoCompraById(estado.getId());
        
        assertEquals(estado.getId(), encontrado.getId());
        assertEquals(estado.getNombre(), encontrado.getNombre());
    }

    @Test
    void testGetEstadoCompraById_NoExiste_LanzaExcepcion() {
        assertThrows(EntityNotFoundException.class, () ->
                estadoCompraService.getEstadoCompraById(999L));
    }

    @Test
    void testGetEstadoCompraPorDefecto() {
        EstadoCompraEntity estado = estadoCompraList.get(0);
        estado.setEsPorDefecto(true);
        entityManager.persist(estado);
        entityManager.flush();

        EstadoCompraEntity porDefecto = estadoCompraService.getEstadoCompraPorDefecto();
        
        assertNotNull(porDefecto);
        assertTrue(porDefecto.getEsPorDefecto());
    }
}