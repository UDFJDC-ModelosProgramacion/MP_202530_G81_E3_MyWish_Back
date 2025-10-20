package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.FotoEntity;
import co.edu.udistrital.mdp.back.repositories.FotoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(FotoService.class)
public class FotoServiceTest {

    @Autowired
    private FotoService fotoService;

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private FotoEntity fotoPrueba;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        fotoRepository.deleteAll();
    }

    private void insertData() {
        fotoPrueba = new FotoEntity();
        fotoPrueba.setUrl("https://example.com/imagen.jpg");
        fotoPrueba.setDescripcion("Foto de prueba");
        fotoPrueba.setTipoArchivo("image/jpeg");
        fotoPrueba.setTamanioBytes(500000L);
        fotoPrueba.setEsPrincipal(true);
        entityManager.persist(fotoPrueba);
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Test
    void testCreateFoto_Valida() {
        FotoEntity nuevaFoto = new FotoEntity();
        nuevaFoto.setUrl("https://miservidor.com/foto.png");
        nuevaFoto.setTipoArchivo("image/png");
        nuevaFoto.setTamanioBytes(2048L);
        nuevaFoto.setDescripcion("Foto válida");
        nuevaFoto.setEsPrincipal(false);

        FotoEntity resultado = fotoService.createFoto(nuevaFoto);

        assertNotNull(resultado);
        assertEquals("https://miservidor.com/foto.png", resultado.getUrl());
        assertNotNull(entityManager.find(FotoEntity.class, resultado.getId()));
    }

    @Test
    void testCreateFoto_UrlInvalida() {
        FotoEntity foto = new FotoEntity();
        foto.setUrl("ftp://servidor.com/foto.png");
        foto.setTipoArchivo("image/png");

        assertThrows(IllegalArgumentException.class, () -> fotoService.createFoto(foto));
    }

    @Test
    void testCreateFoto_TamanoExcedido() {
        FotoEntity foto = new FotoEntity();
        foto.setUrl("https://servidor.com/foto.jpg");
        foto.setTipoArchivo("image/jpeg");
        foto.setTamanioBytes(11L * 1024 * 1024); // 11MB

        assertThrows(IllegalArgumentException.class, () -> fotoService.createFoto(foto));
    }

    @Test
    void testCreateFoto_TipoArchivoInvalido() {
        FotoEntity foto = new FotoEntity();
        foto.setUrl("https://servidor.com/foto.txt");
        foto.setTipoArchivo("text/plain");

        assertThrows(IllegalArgumentException.class, () -> fotoService.createFoto(foto));
    }

    // =====================================================
    // GET
    // =====================================================

    @Test
    void testGetAllFotos() {
        List<FotoEntity> lista = fotoService.getAllFotos();
        assertEquals(1, lista.size());
        assertEquals(fotoPrueba.getUrl(), lista.get(0).getUrl());
    }

    @Test
    void testGetFotoById_Existente() {
        FotoEntity encontrada = fotoService.getFotoById(fotoPrueba.getId());
        assertNotNull(encontrada);
        assertEquals(fotoPrueba.getId(), encontrada.getId());
    }

    @Test
    void testGetFotoById_NoExistente() {
        assertThrows(EntityNotFoundException.class, () -> fotoService.getFotoById(999L));
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Test
    void testUpdateFoto_DescripcionYPrincipal() {
        FotoEntity actualizada = new FotoEntity();
        actualizada.setDescripcion("Nueva descripción");
        actualizada.setEsPrincipal(false);

        FotoEntity resultado = fotoService.updateFoto(fotoPrueba.getId(), actualizada);

        assertEquals("Nueva descripción", resultado.getDescripcion());
        assertFalse(resultado.getEsPrincipal());
    }

    @Test
    void testUpdateFoto_NoExistente() {
        FotoEntity datos = new FotoEntity();
        datos.setDescripcion("Cambio");
        assertThrows(EntityNotFoundException.class, () -> fotoService.updateFoto(999L, datos));
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Test
    void testDeleteFoto_Existente() {
        fotoService.deleteFoto(fotoPrueba.getId());
        FotoEntity eliminada = entityManager.find(FotoEntity.class, fotoPrueba.getId());
        assertNull(eliminada);
    }

    @Test
    void testDeleteFoto_NoExistente() {
        assertThrows(EntityNotFoundException.class, () -> fotoService.deleteFoto(999L));
    }
}