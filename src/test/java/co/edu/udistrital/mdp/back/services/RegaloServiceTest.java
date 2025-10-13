package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.*;
import co.edu.udistrital.mdp.back.repositories.*;
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
@Import(RegaloService.class)
class RegaloServiceTest {

    @Autowired
    private RegaloService service;

    @Autowired
    private RegaloRepository regaloRepo;

    @Autowired
    private TiendaRepository tiendaRepo;

    @Autowired
    private EstadoCompraRepository estadoRepo;

    @Autowired
    private PrioridadRegaloRepository prioridadRepo;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<RegaloEntity> regaloList = new ArrayList<>();
    private TiendaEntity tienda;
    private EstadoCompraEntity estado;
    private PrioridadRegaloEntity prioridad;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from RegaloEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TiendaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EstadoCompraEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PrioridadRegaloEntity").executeUpdate();
    }

    private void insertData() {
        tienda = factory.manufacturePojo(TiendaEntity.class);
        entityManager.persist(tienda);

        estado = factory.manufacturePojo(EstadoCompraEntity.class);
        entityManager.persist(estado);

        prioridad = factory.manufacturePojo(PrioridadRegaloEntity.class);
        entityManager.persist(prioridad);

        for (int i = 0; i < 3; i++) {
            RegaloEntity regalo = factory.manufacturePojo(RegaloEntity.class);
            regalo.setTienda(tienda);
            regalo.setEstadoCompra(estado);
            regalo.setPrioridad(prioridad);
            entityManager.persist(regalo);
            regaloList.add(regalo);
        }
    }

    // --- TESTS CRUD ---

    @Test
    void testGetAll() {
        List<RegaloEntity> list = service.getAll();
        assertEquals(regaloList.size(), list.size());
    }

    @Test
    void testGetByIdSuccess() {
        RegaloEntity found = service.getById(regaloList.get(0).getId());
        assertNotNull(found);
        assertEquals(regaloList.get(0).getId(), found.getId());
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.getById(999L));
    }

    @Test
    void testCreateSuccess() {
        RegaloEntity nuevo = factory.manufacturePojo(RegaloEntity.class);
        nuevo.setTienda(tienda);
        nuevo.setEstadoCompra(estado);
        nuevo.setPrioridad(prioridad);

        RegaloEntity creado = service.create(nuevo);
        assertNotNull(creado.getId());
        assertEquals(nuevo.getDescripcion(), creado.getDescripcion());
    }

    @Test
    void testUpdateSuccess() {
        RegaloEntity base = regaloList.get(0);
        RegaloEntity cambios = factory.manufacturePojo(RegaloEntity.class);

        cambios.setTienda(tienda);
        cambios.setEstadoCompra(estado);
        cambios.setPrioridad(prioridad);

        RegaloEntity actualizado = service.update(base.getId(), cambios);
        assertEquals(cambios.getDescripcion(), actualizado.getDescripcion());
    }

    @Test
    void testUpdateNotFound() {
        RegaloEntity cambios = factory.manufacturePojo(RegaloEntity.class);
        assertThrows(EntityNotFoundException.class, () -> service.update(999L, cambios));
    }

    @Test
    void testDeleteSuccess() {
        RegaloEntity base = regaloList.get(0);
        service.delete(base.getId());
        assertThrows(EntityNotFoundException.class, () -> service.getById(base.getId()));
    }

    // --- TESTS DE CONSULTAS ---

    @Test
    void testFindByCategoria() {
        RegaloEntity base = regaloList.get(0);
        List<RegaloEntity> result = service.findByCategoria(base.getCategoria());
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByDescripcion() {
        RegaloEntity base = regaloList.get(0);
        List<RegaloEntity> result = service.findByDescripcion(base.getDescripcion().substring(0, 2));
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByPrecioMenorYMayor() {
        List<RegaloEntity> menor = service.findByPrecioMenorA(999999.0);
        List<RegaloEntity> mayor = service.findByPrecioMayorA(0.0);
        assertNotNull(menor);
        assertNotNull(mayor);
    }

    @Test
    void testFindByTiendaSuccess() {
        List<RegaloEntity> result = service.findByTienda(tienda.getId());
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByTiendaNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.findByTienda(999L));
    }

    @Test
    void testFindByEstadoCompraSuccess() {
        List<RegaloEntity> result = service.findByEstadoCompra(estado.getId());
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByEstadoCompraNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.findByEstadoCompra(999L));
    }

    @Test
    void testFindByPrioridadSuccess() {
        List<RegaloEntity> result = service.findByPrioridad(prioridad.getId());
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByPrioridadNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.findByPrioridad(999L));
    }
}
