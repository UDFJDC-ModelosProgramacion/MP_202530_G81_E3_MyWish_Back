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

    private List<CatalogoTiendasEntity> catalogosList;
    private List<TiendaEntity> tiendasList;

    @BeforeEach
    void setUp() {
        catalogosList = new ArrayList<>();
        tiendasList = new ArrayList<>();
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from CatalogoTiendasEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TiendaEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 2; i++) {
            TiendaEntity tienda = factory.manufacturePojo(TiendaEntity.class);
            entityManager.persist(tienda);
            tiendasList.add(tienda);
        }

        for (int i = 0; i < 2; i++) {
            CatalogoTiendasEntity catalogo = factory.manufacturePojo(CatalogoTiendasEntity.class);
            catalogo.setNombre("Catalogo" + i);
            catalogo.setTiendas(new ArrayList<>(tiendasList.subList(0, 1)));
            entityManager.persist(catalogo);
            catalogosList.add(catalogo);
        }
    }

    @Test
    void crearCatalogo_deberiaPersistirCorrectamente() {
        CatalogoTiendasEntity nuevo = factory.manufacturePojo(CatalogoTiendasEntity.class);
        nuevo.setNombre("NuevoCatalogo");
        nuevo.setTiendas(new ArrayList<>(tiendasList));

        CatalogoTiendasEntity creado = catalogoTiendasService.crearCatalogo(nuevo);

        assertNotNull(creado.getId());
        assertEquals("NuevoCatalogo", creado.getNombre());

        CatalogoTiendasEntity persistido = catalogoTiendasRepository.findById(creado.getId()).orElse(null);
        assertNotNull(persistido);
    }

    @Test
    void crearCatalogo_deberiaLanzarExcepcion_siNombreVacio() {
        CatalogoTiendasEntity nuevo = factory.manufacturePojo(CatalogoTiendasEntity.class);
        nuevo.setNombre("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.crearCatalogo(nuevo));

        assertEquals(CatalogoTiendasService.MENSAJE_NOMBRE_VACIO, ex.getMessage());
    }

    @Test
    void crearCatalogo_deberiaLanzarExcepcion_siNombreDuplicado() {
        CatalogoTiendasEntity duplicado = factory.manufacturePojo(CatalogoTiendasEntity.class);
        duplicado.setNombre(catalogosList.get(0).getNombre());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.crearCatalogo(duplicado));

        assertEquals(CatalogoTiendasService.MENSAJE_NOMBRE_CATALOGO_DUPLICADO, ex.getMessage());
    }

    @Test
    void crearCatalogo_deberiaManejarTiendasNull() {
        CatalogoTiendasEntity nuevo = factory.manufacturePojo(CatalogoTiendasEntity.class);
        nuevo.setNombre("ConTiendasNull");
        nuevo.setTiendas(null);

        CatalogoTiendasEntity creado = catalogoTiendasService.crearCatalogo(nuevo);

        assertNotNull(creado.getId());
        assertNotNull(creado.getTiendas());
        assertTrue(creado.getTiendas().isEmpty());
    }

    @Test
    void actualizarCatalogo_deberiaActualizarTodosLosCampos() {
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
    void actualizarCatalogo_deberiaLanzarExcepcion_siNombreDuplicado() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);
        String nombreDuplicado = catalogosList.get(1).getNombre();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.actualizarCatalogo(catalogo.getId(), nombreDuplicado, "desc", new ArrayList<>()));

        assertEquals(CatalogoTiendasService.MENSAJE_NOMBRE_EN_USO_OTRO, ex.getMessage());
    }

    @Test
    void eliminarCatalogo_deberiaEliminarCorrectamente() {
        CatalogoTiendasEntity nuevo = factory.manufacturePojo(CatalogoTiendasEntity.class);
        nuevo.setNombre("Eliminar");
        nuevo.setTiendas(new ArrayList<>());
        entityManager.persist(nuevo);

        catalogoTiendasService.eliminarCatalogo(nuevo.getId());

        assertFalse(catalogoTiendasRepository.findById(nuevo.getId()).isPresent());
    }

    @Test
    void eliminarCatalogo_deberiaLanzarExcepcion_siTieneTiendas() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> catalogoTiendasService.eliminarCatalogo(catalogo.getId()));

        assertEquals(CatalogoTiendasService.MENSAJE_NO_ELIMINAR_TIENDAS_ASOCIADAS, ex.getMessage());
    }

    @Test
    void eliminarTiendaDeCatalogo_deberiaEliminarCorrectamente() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);
        TiendaEntity tienda = tiendasList.get(0);

        catalogoTiendasService.eliminarTiendaDeCatalogo(catalogo.getId(), tienda.getId());

        CatalogoTiendasEntity actualizado = catalogoTiendasRepository.findById(catalogo.getId()).get();
        assertTrue(actualizado.getTiendas().isEmpty());
    }

    @Test
    void eliminarTiendaDeCatalogo_deberiaLanzarExcepcion_siTiendaNoPertenece() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);
        TiendaEntity tienda = tiendasList.get(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.eliminarTiendaDeCatalogo(catalogo.getId(), tienda.getId()));

        assertEquals(CatalogoTiendasService.MENSAJE_TIENDA_NO_PERTENECE_CATALOGO, ex.getMessage());
    }

    @Test
    void actualizarCatalogo_deberiaActualizarSoloNombre() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);

        CatalogoTiendasEntity actualizado = catalogoTiendasService.actualizarCatalogo(
                catalogo.getId(),
                "SoloNombre",
                catalogo.getDescripcion(),
                catalogo.getTiendas()
        );

        assertEquals("SoloNombre", actualizado.getNombre());
        assertEquals(catalogo.getDescripcion(), actualizado.getDescripcion());
    }

    @Test
    void actualizarCatalogo_deberiaActualizarSoloDescripcion() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);

        CatalogoTiendasEntity actualizado = catalogoTiendasService.actualizarCatalogo(
                catalogo.getId(),
                catalogo.getNombre(),
                "SoloDescripcion",
                catalogo.getTiendas()
        );

        assertEquals("SoloDescripcion", actualizado.getDescripcion());
        assertEquals(catalogo.getNombre(), actualizado.getNombre());
    }

    @Test
    void actualizarCatalogo_deberiaActualizarSoloNombre() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);

        CatalogoTiendasEntity actualizado = catalogoTiendasService.actualizarCatalogo(
                catalogo.getId(),
                "SoloNombre",
                catalogo.getDescripcion(),
                catalogo.getTiendas()
        );

        assertEquals("SoloNombre", actualizado.getNombre());
        assertEquals(catalogo.getDescripcion(), actualizado.getDescripcion());
    }

    @Test
    void actualizarCatalogo_deberiaActualizarSoloDescripcion() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);

        CatalogoTiendasEntity actualizado = catalogoTiendasService.actualizarCatalogo(
                catalogo.getId(),
                catalogo.getNombre(),
                "SoloDescripcion",
                catalogo.getTiendas()
        );

        assertEquals("SoloDescripcion", actualizado.getDescripcion());
        assertEquals(catalogo.getNombre(), actualizado.getNombre());
    }
}
