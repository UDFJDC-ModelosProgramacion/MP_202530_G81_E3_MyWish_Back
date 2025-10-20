package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.*;
import co.edu.udistrital.mdp.back.repositories.FotoRepository;
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
@Import(FotoService.class)
class FotoServiceTest {

    @Autowired
    private FotoService fotoService;

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<FotoEntity> fotoList = new ArrayList<>();
    private TiendaEntity tienda;
    private ComentarioEntity comentario;
    private RegaloEntity regalo;
    private ListaRegalosEntity listaRegalos;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from FotoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TiendaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ComentarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from RegaloEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ListaRegalosEntity").executeUpdate();
    }

    private void insertData() {
        tienda = factory.manufacturePojo(TiendaEntity.class);
        entityManager.persist(tienda);

        comentario = factory.manufacturePojo(ComentarioEntity.class);
        entityManager.persist(comentario);

        regalo = factory.manufacturePojo(RegaloEntity.class);
        entityManager.persist(regalo);

        listaRegalos = factory.manufacturePojo(ListaRegalosEntity.class);
        entityManager.persist(listaRegalos);

        for (int i = 0; i < 3; i++) {
            FotoEntity foto = factory.manufacturePojo(FotoEntity.class);
            foto.setUrl("https://ejemplo.com/foto" + i + ".jpg");
            foto.setTipoArchivo("image/jpeg");
            foto.setTamanioBytes(500000L);
            foto.setTienda(tienda);
            entityManager.persist(foto);
            fotoList.add(foto);
        }
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Test
    void testCreateFotoSuccess() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://valid.com/foto.jpg");
        nueva.setTipoArchivo("image/png");
        nueva.setTamanioBytes(1000000L);
        nueva.setTienda(tienda);

        FotoEntity result = fotoService.createFoto(nueva);

        assertNotNull(result.getId());
        assertEquals("https://valid.com/foto.jpg", result.getUrl());
        assertEquals(tienda.getId(), result.getTienda().getId());
    }

    @Test
    void testCreateFotoUrlInvalidaThrows() {
        FotoEntity foto = factory.manufacturePojo(FotoEntity.class);
        foto.setUrl("ftp://invalida.jpg");
        foto.setTienda(tienda);

        assertThrows(IllegalArgumentException.class, () -> fotoService.createFoto(foto));
    }

    @Test
    void testCreateFotoTamanioExcedeThrows() {
        FotoEntity foto = factory.manufacturePojo(FotoEntity.class);
        foto.setUrl("https://valida.com/foto.jpg");
        foto.setTamanioBytes(15L * 1024 * 1024); // 15 MB
        foto.setTienda(tienda);

        assertThrows(IllegalArgumentException.class, () -> fotoService.createFoto(foto));
    }

    @Test
    void testCreateFotoTipoArchivoInvalidoThrows() {
        FotoEntity foto = factory.manufacturePojo(FotoEntity.class);
        foto.setUrl("https://valida.com/foto.jpg");
        foto.setTipoArchivo("application/pdf");
        foto.setTienda(tienda);

        assertThrows(IllegalArgumentException.class, () -> fotoService.createFoto(foto));
    }

    @Test
    void testCreateFotoConMultiplesEntidadesThrows() {
        FotoEntity foto = factory.manufacturePojo(FotoEntity.class);
        foto.setUrl("https://valida.com/foto.jpg");
        foto.setTienda(tienda);
        foto.setComentario(comentario);

        assertThrows(IllegalArgumentException.class, () -> fotoService.createFoto(foto));
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Test
    void testUpdateFotoDescripcionYPrincipalSuccess() {
        FotoEntity existente = fotoList.get(0);
        FotoEntity cambios = new FotoEntity();
        cambios.setDescripcion("Nueva descripción");
        cambios.setEsPrincipal(true);

        FotoEntity result = fotoService.updateFoto(existente.getId(), cambios);

        assertEquals("Nueva descripción", result.getDescripcion());
        assertTrue(result.getEsPrincipal());
    }

    @Test
    void testUpdateFotoNoExisteThrows() {
        FotoEntity cambios = new FotoEntity();
        cambios.setDescripcion("Cambio");

        assertThrows(EntityNotFoundException.class, () -> fotoService.updateFoto(999L, cambios));
    }

    @Test
    void testUpdateFotoCambiaEntidadThrows() {
        FotoEntity existente = fotoList.get(0);
        FotoEntity cambios = new FotoEntity();
        cambios.setRegalo(regalo);

        assertThrows(IllegalStateException.class, () -> fotoService.updateFoto(existente.getId(), cambios));
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Test
    void testDeleteFotoSuccess() {
        FotoEntity existente = fotoList.get(1);
        fotoService.deleteFoto(existente.getId());

        FotoEntity deleted = entityManager.find(FotoEntity.class, existente.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteFotoNoExisteThrows() {
        assertThrows(EntityNotFoundException.class, () -> fotoService.deleteFoto(999L));
    }

    // =====================================================
    // GET
    // =====================================================

    @Test
    void testGetFotoByIdSuccess() {
        FotoEntity existente = fotoList.get(0);
        FotoEntity result = fotoService.getFotoById(existente.getId());

        assertEquals(existente.getId(), result.getId());
    }

    @Test
    void testGetFotoByIdNoExisteThrows() {
        assertThrows(EntityNotFoundException.class, () -> fotoService.getFotoById(999L));
    }

    @Test
    void testGetFotosPrincipales() {
        fotoList.get(0).setEsPrincipal(true);
        entityManager.persist(fotoList.get(0));

        List<FotoEntity> principales = fotoService.getFotosPrincipales();
        assertFalse(principales.isEmpty());
        assertTrue(principales.stream().allMatch(FotoEntity::getEsPrincipal));
    }
}
