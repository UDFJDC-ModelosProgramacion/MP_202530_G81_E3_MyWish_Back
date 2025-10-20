package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;
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
@Import(TiendaService.class)
class TiendaServiceTest {

    @Autowired
    private TiendaService tiendaService;

    @Autowired
    private TiendaRepository tiendaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<TiendaEntity> tiendas = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from TiendaEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            TiendaEntity tienda = factory.manufacturePojo(TiendaEntity.class);
            tienda.setLink("https://tienda" + i + ".com"); // ✅ garantizar link válido
            entityManager.persist(tienda);
            tiendas.add(tienda);
        }
    }

    // ---------------------------------------------------------------------
    // TESTS CRUD
    // ---------------------------------------------------------------------

    @Test
    void testGetAll() {
        List<TiendaEntity> list = tiendaService.getAll();
        assertEquals(3, list.size());
    }

    @Test
    void testGetByIdSuccess() {
        TiendaEntity entity = tiendas.get(0);
        TiendaEntity found = tiendaService.getById(entity.getId());
        assertNotNull(found);
        assertEquals(entity.getNombre(), found.getNombre());
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> tiendaService.getById(999L));
    }

    @Test
    void testCreateTienda() {
        TiendaEntity nueva = factory.manufacturePojo(TiendaEntity.class);
        nueva.setNombre("Nueva Tienda");
        nueva.setDescripcion("Tienda de prueba");
        nueva.setLink("https://example.com"); // ✅ asegurar link válido

        TiendaEntity creada = tiendaService.create(nueva);

        assertNotNull(creada);
        assertEquals(nueva.getNombre(), creada.getNombre());
        assertEquals(4, tiendaRepository.findAll().size());
    }

    @Test
    void testUpdateTienda() {
        TiendaEntity existente = tiendas.get(0);
        TiendaEntity cambios = factory.manufacturePojo(TiendaEntity.class);
        cambios.setLink("https://nuevalink.com"); // ✅ link válido

        TiendaEntity actualizada = tiendaService.update(existente.getId(), cambios);
        assertEquals(cambios.getNombre(), actualizada.getNombre());
        assertEquals(cambios.getDescripcion(), actualizada.getDescripcion());
    }

    @Test
    void testUpdateTiendaInexistenteDebeFallar() {
        TiendaEntity cambios = factory.manufacturePojo(TiendaEntity.class);
        cambios.setLink("https://valid.com");
        assertThrows(IllegalArgumentException.class, () -> tiendaService.update(9999L, cambios));
    }

    @Test
    void testDeleteTienda() {
        TiendaEntity entity = tiendas.get(0);
        tiendaService.delete(entity.getId());
        assertFalse(tiendaRepository.findById(entity.getId()).isPresent());
    }

    @Test
    void testDeleteTiendaInexistenteDebeFallar() {
        assertThrows(IllegalArgumentException.class, () -> tiendaService.delete(9999L));
    }

    // ---------------------------------------------------------------------
    // TESTS DE REGLAS DE NEGOCIO
    // ---------------------------------------------------------------------

    @Test
    void testFindByNombre() {
        TiendaEntity t = tiendas.get(0);
        List<TiendaEntity> result = tiendaService.findByNombre(t.getNombre());
        assertFalse(result.isEmpty());
    }

    @Test
    void testCreateTiendaSinNombreDebeFallar() {
        TiendaEntity nueva = factory.manufacturePojo(TiendaEntity.class);
        nueva.setNombre("   ");
        nueva.setLink("https://example.com");
        assertThrows(IllegalArgumentException.class, () -> tiendaService.create(nueva));
    }

    @Test
    void testCreateTiendaSinDescripcionDebeFallar() {
        TiendaEntity nueva = factory.manufacturePojo(TiendaEntity.class);
        nueva.setDescripcion(null);
        nueva.setLink("https://example.com");
        assertThrows(IllegalArgumentException.class, () -> tiendaService.create(nueva));
    }

    @Test
    void testCreateTiendaConLinkInvalidoDebeFallar() {
        TiendaEntity nueva = factory.manufacturePojo(TiendaEntity.class);
        nueva.setLink("tienda-invalida.com"); // ❌ sin http
        nueva.setDescripcion("Descripción válida");
        nueva.setNombre("Tienda X");
        assertThrows(IllegalArgumentException.class, () -> tiendaService.create(nueva));
    }

    @Test
    void testCrearTiendaConNombreDuplicadoDebeFallar() {
        TiendaEntity existente = tiendas.get(0);

        TiendaEntity duplicada = factory.manufacturePojo(TiendaEntity.class);
        duplicada.setNombre(existente.getNombre());
        duplicada.setDescripcion("Otra tienda con mismo nombre");
        duplicada.setLink("https://example2.com");

        assertThrows(IllegalArgumentException.class, () -> tiendaService.create(duplicada));
    }
}
