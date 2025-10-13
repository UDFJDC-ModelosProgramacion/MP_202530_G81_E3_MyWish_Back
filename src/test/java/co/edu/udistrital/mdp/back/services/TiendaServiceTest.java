package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;

class TiendaServiceTest {

    @Mock
    private TiendaRepository tiendaRepo;

    @InjectMocks
    private TiendaService tiendaService;

    private TiendaEntity tienda;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tienda = new TiendaEntity();
        tienda.setId(1L);
        tienda.setNombre("Tienda Test");
        tienda.setDescripcion("Descripción de prueba");
    }

    @Test
    void testGetAll() {
        when(tiendaRepo.findAll()).thenReturn(List.of(tienda));
        List<TiendaEntity> result = tiendaService.getAll();
        assertEquals(1, result.size());
        assertEquals("Tienda Test", result.get(0).getNombre());
    }

    @Test
    void testGetById() {
        when(tiendaRepo.findById(1L)).thenReturn(Optional.of(tienda));
        TiendaEntity result = tiendaService.getById(1L);
        assertEquals("Tienda Test", result.getNombre());
    }

    @Test
    void testCreate() {
        when(tiendaRepo.save(any(TiendaEntity.class))).thenReturn(tienda);
        TiendaEntity result = tiendaService.create(tienda);
        assertEquals("Tienda Test", result.getNombre());
    }

    @Test
    void testUpdate() {
        when(tiendaRepo.findById(1L)).thenReturn(Optional.of(tienda));
        when(tiendaRepo.save(any(TiendaEntity.class))).thenReturn(tienda);
        TiendaEntity nueva = new TiendaEntity();
        nueva.setNombre("Tienda Actualizada");

        TiendaEntity result = tiendaService.update(1L, nueva);
        assertEquals("Tienda Actualizada", result.getNombre());
    }

    @Test
    void testDelete() {
        when(tiendaRepo.findById(1L)).thenReturn(Optional.of(tienda));
        doNothing().when(tiendaRepo).delete(tienda);
        assertDoesNotThrow(() -> tiendaService.delete(1L));
    }

    @Test
    void testFindByNombre() {
        when(tiendaRepo.findByNombreContainingIgnoreCase("test")).thenReturn(List.of(tienda));
        List<TiendaEntity> result = tiendaService.findByNombre("test");
        assertEquals(1, result.size());
    }
}
