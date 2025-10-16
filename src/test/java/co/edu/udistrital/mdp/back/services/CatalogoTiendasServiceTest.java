package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.edu.udistrital.mdp.back.entities.CatalogoTiendasEntity;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.CatalogoTiendasRepository;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;

class CatalogoTiendasServiceTest {

    @Mock
    private CatalogoTiendasRepository catalogoTiendasRepository;

    @Mock
    private TiendaRepository tiendaRepository;

    @Mock
    private TiendaEntity tiendaMock;  // Mock en lugar de instancia real

    @InjectMocks
    private CatalogoTiendasService catalogoTiendasService;

    private CatalogoTiendasEntity catalogo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        catalogo = new CatalogoTiendasEntity();
        catalogo.setId(1L);
        catalogo.setNombre("Catalogo1");
        catalogo.setDescripcion("Descripcion1");
        catalogo.setTiendas(new ArrayList<>());
    }

    @Test
    void testCrearCatalogo_Success() {
        when(catalogoTiendasRepository.findByNombre("Catalogo1")).thenReturn(List.of());
        when(catalogoTiendasRepository.save(catalogo)).thenReturn(catalogo);

        CatalogoTiendasEntity creado = catalogoTiendasService.crearCatalogo(catalogo);
        assertEquals("Catalogo1", creado.getNombre());
        verify(catalogoTiendasRepository, times(1)).save(catalogo);
    }

    @Test
    void testCrearCatalogo_NombreVacio() {
        catalogo.setNombre("");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.crearCatalogo(catalogo));
        assertEquals("El nombre del catálogo no puede ser nulo o vacío.", ex.getMessage());
    }

    @Test
    void testCrearCatalogo_Duplicado() {
        when(catalogoTiendasRepository.findByNombre("Catalogo1")).thenReturn(List.of(new CatalogoTiendasEntity()));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.crearCatalogo(catalogo));
        assertEquals("Ya existe un catálogo con ese nombre.", ex.getMessage());
    }

    @Test
    void testActualizarCatalogo_Success() {
        when(catalogoTiendasRepository.findById(1L)).thenReturn(Optional.of(catalogo));
        when(catalogoTiendasRepository.findByNombre("NuevoNombre")).thenReturn(List.of());
        when(catalogoTiendasRepository.save(catalogo)).thenReturn(catalogo);

        List<TiendaEntity> nuevasTiendas = List.of(tiendaMock);

        CatalogoTiendasEntity actualizado = catalogoTiendasService.actualizarCatalogo(1L, "NuevoNombre", "NuevaDescripcion", nuevasTiendas);
        assertEquals("NuevoNombre", actualizado.getNombre());
        assertEquals("NuevaDescripcion", actualizado.getDescripcion());
        assertEquals(1, actualizado.getTiendas().size());
    }

    @Test
    void testActualizarCatalogo_NombreDuplicado() {
        CatalogoTiendasEntity otroCatalogo = new CatalogoTiendasEntity();
        otroCatalogo.setId(2L);
        otroCatalogo.setNombre("NuevoNombre");

        when(catalogoTiendasRepository.findById(1L)).thenReturn(Optional.of(catalogo));
        when(catalogoTiendasRepository.findByNombre("NuevoNombre")).thenReturn(List.of(otroCatalogo));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.actualizarCatalogo(1L, "NuevoNombre", "Desc", List.of()));
        assertEquals("El nombre ya está en uso por otro catálogo.", ex.getMessage());
    }

    @Test
    void testActualizarCatalogo_ListaTiendasVaciaNoPermitida() {
        catalogo.getTiendas().add(tiendaMock);
        when(catalogoTiendasRepository.findById(1L)).thenReturn(Optional.of(catalogo));
        when(catalogoTiendasRepository.findByNombre(anyString())).thenReturn(List.of());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.actualizarCatalogo(1L, "Nombre", "Desc", new ArrayList<>()));
        assertEquals("No se puede dejar vacía la lista de tiendas si ya tenía elementos.", ex.getMessage());
    }

    @Test
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

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> catalogoTiendasService.eliminarCatalogo(1L));
        assertEquals("No se puede eliminar un catálogo que tiene tiendas asociadas.", ex.getMessage());
    }

    @Test
    void testEliminarTiendaDeCatalogo_Success() {
        catalogo.getTiendas().add(tiendaMock);
        when(catalogoTiendasRepository.findById(1L)).thenReturn(Optional.of(catalogo));
        when(tiendaRepository.findById(10L)).thenReturn(Optional.of(tiendaMock));

        catalogoTiendasService.eliminarTiendaDeCatalogo(1L, 10L);

        assertFalse(catalogo.getTiendas().contains(tiendaMock));
        verify(catalogoTiendasRepository, times(1)).save(catalogo);
    }

    @Test
    void testEliminarTiendaDeCatalogo_TiendaNoPertenece() {
        when(catalogoTiendasRepository.findById(1L)).thenReturn(Optional.of(catalogo));
        when(tiendaRepository.findById(10L)).thenReturn(Optional.of(tiendaMock));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> catalogoTiendasService.eliminarTiendaDeCatalogo(1L, 10L));
        assertEquals("La tienda no pertenece a este catálogo.", ex.getMessage());
    }
    
}
