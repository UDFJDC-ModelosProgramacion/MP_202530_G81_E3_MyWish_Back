package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.back.entities.CatalogoTiendasEntity;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.CatalogoTiendasRepository;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(CatalogoTiendasService.class)
public class CatalogoTiendasServiceTest {

    @Autowired
    private CatalogoTiendasService catalogoTiendasService;

    @Autowired
    private CatalogoTiendasRepository catalogoTiendasRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CatalogoTiendasEntity> catalogosList = new ArrayList<>();
    private List<TiendaEntity> tiendasList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from CatalogoTiendasEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TiendaEntity").executeUpdate();
    }

    private void insertData() {
        // Crear tiendas
        for (int i = 0; i < 2; i++) {
            TiendaEntity tienda = factory.manufacturePojo(TiendaEntity.class);
            entityManager.persist(tienda);
            tiendasList.add(tienda);
        }

        // Crear catálogos
        for (int i = 0; i < 2; i++) {
            CatalogoTiendasEntity catalogo = factory.manufacturePojo(CatalogoTiendasEntity.class);
            catalogo.setNombre("Catalogo" + i);
            catalogo.setTiendas(new ArrayList<>(tiendasList.subList(0, 1)));
            entityManager.persist(catalogo);
            catalogosList.add(catalogo);
        }
    }

    @Test
    void testCrearCatalogo_Success() {
        CatalogoTiendasEntity nuevo = factory.manufacturePojo(CatalogoTiendasEntity.class);
        nuevo.setNombre("NuevoCatalogo");
        nuevo.setTiendas(new ArrayList<>(tiendasList));

        CatalogoTiendasEntity creado = catalogoTiendasService.crearCatalogo(nuevo);

        assertNotNull(creado.getId());
        assertEquals("NuevoCatalogo", creado.getNombre());
    }

    @Test
    void testCrearCatalogo_NombreVacio() {
        CatalogoTiendasEntity nuevo = factory.manufacturePojo(CatalogoTiendasEntity.class);
        nuevo.setNombre("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.crearCatalogo(nuevo));

        assertEquals("El nombre del catálogo no puede ser nulo o vacío.", ex.getMessage());
    }

    @Test
    void testCrearCatalogo_Duplicado() {
        CatalogoTiendasEntity duplicado = factory.manufacturePojo(CatalogoTiendasEntity.class);
        duplicado.setNombre(catalogosList.get(0).getNombre());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.crearCatalogo(duplicado));

        assertEquals("Ya existe un catálogo con ese nombre.", ex.getMessage());
    }

    @Test
    void testActualizarCatalogo_Success() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);

        CatalogoTiendasEntity actualizado = catalogoTiendasService.actualizarCatalogo(
                catalogo.getId(),
                "NuevoNombre",
                "NuevaDescripcion",
                new ArrayList<>(tiendasList)
        );

        assertEquals("NuevoNombre", actualizado.getNombre());
        assertEquals("NuevaDescripcion", actualizado.getDescripcion());
        assertFalse(actualizado.getTiendas().isEmpty());
    }

    @Test
    void testActualizarCatalogo_NombreDuplicado() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);
        String nombreDuplicado = catalogosList.get(1).getNombre();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.actualizarCatalogo(catalogo.getId(), nombreDuplicado, "desc", new ArrayList<>()));

        assertEquals("El nombre ya está en uso por otro catálogo.", ex.getMessage());
    }

    @Test
    void testEliminarCatalogo_Success() {
        CatalogoTiendasEntity nuevo = factory.manufacturePojo(CatalogoTiendasEntity.class);
        nuevo.setNombre("Eliminar");
        nuevo.setTiendas(new ArrayList<>());
        entityManager.persist(nuevo);

        catalogoTiendasService.eliminarCatalogo(nuevo.getId());

        assertFalse(catalogoTiendasRepository.findById(nuevo.getId()).isPresent());
    }

    @Test
    void testEliminarCatalogo_ConTiendas() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> catalogoTiendasService.eliminarCatalogo(catalogo.getId()));

        assertEquals("No se puede eliminar un catálogo que tiene tiendas asociadas.", ex.getMessage());
    }

    @Test
    void testEliminarTiendaDeCatalogo_Success() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);
        TiendaEntity tienda = tiendasList.get(0);

        catalogoTiendasService.eliminarTiendaDeCatalogo(catalogo.getId(), tienda.getId());

        CatalogoTiendasEntity actualizado = catalogoTiendasRepository.findById(catalogo.getId()).get();
        assertTrue(actualizado.getTiendas().isEmpty());
    }

    @Test
    void testEliminarTiendaDeCatalogo_TiendaNoPertenece() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);
        TiendaEntity tienda = tiendasList.get(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.eliminarTiendaDeCatalogo(catalogo.getId(), tienda.getId()));

        assertEquals("La tienda no pertenece a este catálogo.", ex.getMessage());
    }
}
