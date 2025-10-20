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

<<<<<<< HEAD
@DataJpaTest
@Transactional
@Import(CatalogoTiendasService.class)
public class CatalogoTiendasServiceTest {
=======
class CatalogoTiendasServiceTest {
>>>>>>> 9dfa48a3575148cdd0d23fd015d6f7a2feb3d13f

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
<<<<<<< HEAD
    void setUp() {
        clearData();
        insertData();
    }
=======
    void setup() {
        MockitoAnnotations.openMocks(this);
>>>>>>> 9dfa48a3575148cdd0d23fd015d6f7a2feb3d13f

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
<<<<<<< HEAD
        CatalogoTiendasEntity nuevo = factory.manufacturePojo(CatalogoTiendasEntity.class);
        nuevo.setNombre("NuevoCatalogo");
        nuevo.setTiendas(new ArrayList<>(tiendasList));
=======
        when(catalogoTiendasRepository.findByNombre("Catalogo1")).thenReturn(List.of());
        when(catalogoTiendasRepository.save(catalogo)).thenReturn(catalogo);
>>>>>>> 9dfa48a3575148cdd0d23fd015d6f7a2feb3d13f

        CatalogoTiendasEntity creado = catalogoTiendasService.crearCatalogo(nuevo);

        assertNotNull(creado.getId());
        assertEquals("NuevoCatalogo", creado.getNombre());
    }

    @Test
    void testCrearCatalogo_NombreVacio() {
<<<<<<< HEAD
        CatalogoTiendasEntity nuevo = factory.manufacturePojo(CatalogoTiendasEntity.class);
        nuevo.setNombre("");

=======
        catalogo.setNombre("");
>>>>>>> 9dfa48a3575148cdd0d23fd015d6f7a2feb3d13f
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.crearCatalogo(nuevo));

        assertEquals("El nombre del catálogo no puede ser nulo o vacío.", ex.getMessage());
    }

    @Test
    void testCrearCatalogo_Duplicado() {
<<<<<<< HEAD
        CatalogoTiendasEntity duplicado = factory.manufacturePojo(CatalogoTiendasEntity.class);
        duplicado.setNombre(catalogosList.get(0).getNombre());

=======
        when(catalogoTiendasRepository.findByNombre("Catalogo1")).thenReturn(List.of(new CatalogoTiendasEntity()));
>>>>>>> 9dfa48a3575148cdd0d23fd015d6f7a2feb3d13f
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.crearCatalogo(duplicado));

        assertEquals("Ya existe un catálogo con ese nombre.", ex.getMessage());
    }

    @Test
    void testActualizarCatalogo_Success() {
<<<<<<< HEAD
        CatalogoTiendasEntity catalogo = catalogosList.get(0);
=======
        when(catalogoTiendasRepository.findById(1L)).thenReturn(Optional.of(catalogo));
        when(catalogoTiendasRepository.findByNombre("NuevoNombre")).thenReturn(List.of());
        when(catalogoTiendasRepository.save(catalogo)).thenReturn(catalogo);
>>>>>>> 9dfa48a3575148cdd0d23fd015d6f7a2feb3d13f

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
<<<<<<< HEAD
        CatalogoTiendasEntity catalogo = catalogosList.get(0);
        String nombreDuplicado = catalogosList.get(1).getNombre();
=======
        CatalogoTiendasEntity otroCatalogo = new CatalogoTiendasEntity();
        otroCatalogo.setId(2L);
        otroCatalogo.setNombre("NuevoNombre");

        when(catalogoTiendasRepository.findById(1L)).thenReturn(Optional.of(catalogo));
        when(catalogoTiendasRepository.findByNombre("NuevoNombre")).thenReturn(List.of(otroCatalogo));
>>>>>>> 9dfa48a3575148cdd0d23fd015d6f7a2feb3d13f

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.actualizarCatalogo(catalogo.getId(), nombreDuplicado, "desc", new ArrayList<>()));

        assertEquals("El nombre ya está en uso por otro catálogo.", ex.getMessage());
    }

    @Test
<<<<<<< HEAD
    void testEliminarCatalogo_Success() {
        CatalogoTiendasEntity nuevo = factory.manufacturePojo(CatalogoTiendasEntity.class);
        nuevo.setNombre("Eliminar");
        nuevo.setTiendas(new ArrayList<>());
        entityManager.persist(nuevo);
=======
    void testActualizarCatalogo_ListaTiendasVaciaNoPermitida() {
        catalogo.getTiendas().add(tiendaMock);
        when(catalogoTiendasRepository.findById(1L)).thenReturn(Optional.of(catalogo));
        when(catalogoTiendasRepository.findByNombre(anyString())).thenReturn(List.of());
>>>>>>> 9dfa48a3575148cdd0d23fd015d6f7a2feb3d13f

        catalogoTiendasService.eliminarCatalogo(nuevo.getId());

        assertFalse(catalogoTiendasRepository.findById(nuevo.getId()).isPresent());
    }

    @Test
<<<<<<< HEAD
    void testEliminarCatalogo_ConTiendas() {
        CatalogoTiendasEntity catalogo = catalogosList.get(0);
=======
    void testEliminarCatalogo_Success() {
        catalogo.setTiendas(new ArrayList<>());
        when(catalogoTiendasRepository.findById(1L)).thenReturn(Optional.of(catalogo));

        catalogoTiendasService.eliminarCatalogo(1L);
        verify(catalogoTiendasRepository, times(1)).delete(catalogo);
    }

    @Test
    void testEliminarCatalogo_ConTiendas() {
        catalogo.getTiendas().add(tiendaMock);
        when(catalogoTiendasRepository.findById(1L)).thenReturn(Optional.of(catalogo));
>>>>>>> 9dfa48a3575148cdd0d23fd015d6f7a2feb3d13f

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> catalogoTiendasService.eliminarCatalogo(catalogo.getId()));

        assertEquals("No se puede eliminar un catálogo que tiene tiendas asociadas.", ex.getMessage());
    }

    @Test
    void testEliminarTiendaDeCatalogo_Success() {
<<<<<<< HEAD
        CatalogoTiendasEntity catalogo = catalogosList.get(0);
        TiendaEntity tienda = tiendasList.get(0);
=======
        catalogo.getTiendas().add(tiendaMock);
        when(catalogoTiendasRepository.findById(1L)).thenReturn(Optional.of(catalogo));
        when(tiendaRepository.findById(10L)).thenReturn(Optional.of(tiendaMock));
>>>>>>> 9dfa48a3575148cdd0d23fd015d6f7a2feb3d13f

        catalogoTiendasService.eliminarTiendaDeCatalogo(catalogo.getId(), tienda.getId());

        CatalogoTiendasEntity actualizado = catalogoTiendasRepository.findById(catalogo.getId()).get();
        assertTrue(actualizado.getTiendas().isEmpty());
    }

    @Test
    void testEliminarTiendaDeCatalogo_TiendaNoPertenece() {
<<<<<<< HEAD
        CatalogoTiendasEntity catalogo = catalogosList.get(0);
        TiendaEntity tienda = tiendasList.get(1);
=======
        when(catalogoTiendasRepository.findById(1L)).thenReturn(Optional.of(catalogo));
        when(tiendaRepository.findById(10L)).thenReturn(Optional.of(tiendaMock));
>>>>>>> 9dfa48a3575148cdd0d23fd015d6f7a2feb3d13f

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.eliminarTiendaDeCatalogo(catalogo.getId(), tienda.getId()));

        assertEquals("La tienda no pertenece a este catálogo.", ex.getMessage());
    }
}
