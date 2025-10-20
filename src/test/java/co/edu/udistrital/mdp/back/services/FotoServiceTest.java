package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.*;
import co.edu.udistrital.mdp.back.repositories.FotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FotoServiceTest {

    @Mock
    private FotoRepository fotoRepository;

    @InjectMocks
    private FotoService fotoService;

    private FotoEntity fotoBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fotoBase = new FotoEntity();
        fotoBase.setId(1L);
        fotoBase.setUrl("https://example.com/foto.jpg");
        fotoBase.setDescripcion("Foto de prueba");
        fotoBase.setTipoArchivo("image/jpeg");
        fotoBase.setTamanioBytes(1024L);
        fotoBase.setEsPrincipal(false);
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Test
    void createFoto_conUrlInvalida_deberiaLanzarExcepcion() {
        fotoBase.setUrl("ftp://archivo.jpg");
        assertThrows(IllegalArgumentException.class, () -> fotoService.createFoto(fotoBase));
    }

    @Test
    void createFoto_conTamanioExcesivo_deberiaLanzarExcepcion() {
        fotoBase.setUrl("https://imagen.jpg");
        fotoBase.setTamanioBytes(20 * 1024 * 1024L); // 20 MB
        assertThrows(IllegalArgumentException.class, () -> fotoService.createFoto(fotoBase));
    }

    @Test
    void createFoto_conTipoArchivoInvalido_deberiaLanzarExcepcion() {
        fotoBase.setUrl("https://imagen.xyz");
        fotoBase.setTipoArchivo("application/pdf");
        assertThrows(IllegalArgumentException.class, () -> fotoService.createFoto(fotoBase));
    }

    @Test
    void createFoto_sinEntidadAsociada_deberiaLanzarExcepcion() {
        fotoBase.setUrl("https://imagen.jpg");
        assertThrows(IllegalArgumentException.class, () -> fotoService.createFoto(fotoBase));
    }

    @Test
    void createFoto_conMultiplesEntidadesAsociadas_deberiaLanzarExcepcion() {
        fotoBase.setUrl("https://imagen.jpg");
        fotoBase.setRegalo(new RegaloEntity());
        fotoBase.setTienda(new TiendaEntity());
        assertThrows(IllegalArgumentException.class, () -> fotoService.createFoto(fotoBase));
    }

    @Test
    void createFoto_regaloYaTieneFoto_deberiaLanzarExcepcion() {
        RegaloEntity regalo = new RegaloEntity();
        regalo.setId(1L);
        fotoBase.setRegalo(regalo);

        when(fotoRepository.findByRegaloId(1L)).thenReturn(List.of(new FotoEntity()));

        fotoBase.setUrl("https://imagen.jpg");

        assertThrows(IllegalStateException.class, () -> fotoService.createFoto(fotoBase));
    }

    @Test
    void createFoto_validaYUnicaDebeGuardar() {
        TiendaEntity tienda = new TiendaEntity();
        tienda.setId(5L);
        fotoBase.setTienda(tienda);
        when(fotoRepository.save(any(FotoEntity.class))).thenReturn(fotoBase);

        FotoEntity creada = fotoService.createFoto(fotoBase);

        assertNotNull(creada);
        verify(fotoRepository, times(1)).save(fotoBase);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Test
    void updateFoto_inexistente_deberiaLanzarExcepcion() {
        when(fotoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> fotoService.updateFoto(99L, fotoBase));
    }

    @Test
    void updateFoto_cambiaEntidadAsociada_deberiaLanzarExcepcion() {
        RegaloEntity regalo = new RegaloEntity();
        regalo.setId(1L);
        fotoBase.setRegalo(regalo);

        FotoEntity existente = new FotoEntity();
        existente.setId(1L);
        existente.setTienda(new TiendaEntity());

        when(fotoRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalStateException.class, () -> fotoService.updateFoto(1L, fotoBase));
    }

    @Test
    void updateFoto_cambiaDescripcionYPrincipalDebeActualizar() {
        TiendaEntity tienda = new TiendaEntity();
        tienda.setId(10L);

        FotoEntity existente = new FotoEntity();
        existente.setId(1L);
        existente.setTienda(tienda);
        existente.setEsPrincipal(false);

        when(fotoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(fotoRepository.findByTiendaId(10L)).thenReturn(List.of(existente));
        when(fotoRepository.save(any(FotoEntity.class))).thenReturn(existente);

        fotoBase.setTienda(tienda);
        fotoBase.setDescripcion("Nueva descripción");
        fotoBase.setEsPrincipal(true);

        FotoEntity actualizado = fotoService.updateFoto(1L, fotoBase);

        assertEquals("Nueva descripción", actualizado.getDescripcion());
        assertTrue(actualizado.getEsPrincipal());
        verify(fotoRepository, atLeastOnce()).save(any(FotoEntity.class));
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Test
    void deleteFoto_inexistente_deberiaLanzarExcepcion() {
        when(fotoRepository.findById(77L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> fotoService.deleteFoto(77L));
    }

    @Test
    void deleteFoto_existenteDebeEliminar() {
        when(fotoRepository.findById(1L)).thenReturn(Optional.of(fotoBase));

        fotoService.deleteFoto(1L);

        verify(fotoRepository, times(1)).delete(fotoBase);
    }

    // =====================================================
    // GET
    // =====================================================

    @Test
    void getFotoById_inexistente_deberiaLanzarExcepcion() {
        when(fotoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> fotoService.getFotoById(1L));
    }

    @Test
    void getFotoById_existenteDebeRetornar() {
        when(fotoRepository.findById(1L)).thenReturn(Optional.of(fotoBase));
        FotoEntity foto = fotoService.getFotoById(1L);
        assertEquals(fotoBase, foto);
    }

    @Test
    void getAllFotos_debeRetornarLista() {
        when(fotoRepository.findAll()).thenReturn(List.of(fotoBase));
        List<FotoEntity> lista = fotoService.getAllFotos();
        assertEquals(1, lista.size());
        verify(fotoRepository, times(1)).findAll();
    }
}

