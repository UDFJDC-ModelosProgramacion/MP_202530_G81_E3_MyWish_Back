package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.FotoEntity;
import co.edu.udistrital.mdp.back.entities.RegaloEntity;
import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.repositories.FotoRepository;
import co.edu.udistrital.mdp.back.repositories.RegaloRepository;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;
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
    private TestEntityManager entityManager;

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private RegaloRepository regaloRepository;

    @Autowired
    private ListaRegalosRepository listaRegalosRepository;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<FotoEntity> fotoList = new ArrayList<>();
    private List<RegaloEntity> regaloList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from FotoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from RegaloEntity").executeUpdate();
    }

    private void insertData() {
        // Crear regalos
        for (int i = 0; i < 2; i++) {
            RegaloEntity regalo = factory.manufacturePojo(RegaloEntity.class);
            entityManager.persist(regalo);
            regaloList.add(regalo);
        }

        // Crear fotos
        for (int i = 0; i < 3; i++) {
            FotoEntity foto = factory.manufacturePojo(FotoEntity.class);
            foto.setUrl("https://ejemplo.com/foto" + i + ".jpg");
            foto.setTipoArchivo("image/jpeg");
            foto.setTamanioBytes(1024L * 1024L); // 1MB
            foto.setEsPrincipal(false);
            foto.setRegalo(regaloList.get(0));
            entityManager.persist(foto);
            fotoList.add(foto);
        }
    }

    // =====================================================
    // CREATE TESTS
    // =====================================================

    @Test
    void testCreateFoto_Success() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://ejemplo.com/nueva.jpg");
        nueva.setTipoArchivo("image/jpeg");
        nueva.setTamanioBytes(2048L * 1024L); // 2MB
        nueva.setDescripcion("Foto de prueba");
        nueva.setRegalo(regaloList.get(1));
        nueva.setListaRegalos(null);
        nueva.setTienda(null);
        nueva.setComentario(null);

        FotoEntity creada = fotoService.createFoto(nueva);
        
        assertNotNull(creada);
        assertNotNull(creada.getId());
        assertEquals("https://ejemplo.com/nueva.jpg", creada.getUrl());
    }

    @Test
    void testCreateFoto_URLInvalida_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("ejemplo.com/foto.jpg"); // Sin http/https
        nueva.setRegalo(regaloList.get(0));

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
    }

    @Test
    void testCreateFoto_URLNull_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl(null);
        nueva.setRegalo(regaloList.get(0));

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
    }

    @Test
    void testCreateFoto_TamanioExcedido_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://ejemplo.com/grande.jpg");
        nueva.setTamanioBytes(11L * 1024L * 1024L); // 11MB (excede el límite)
        nueva.setRegalo(regaloList.get(0));

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
    }

    @Test
    void testCreateFoto_FormatoInvalido_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://ejemplo.com/archivo.pdf");
        nueva.setTipoArchivo("application/pdf");
        nueva.setRegalo(regaloList.get(0));

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
    }

    @Test
    void testCreateFoto_SinEntidadAsociada_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://ejemplo.com/foto.jpg");
        nueva.setTipoArchivo("image/jpeg");
        nueva.setRegalo(null);
        nueva.setListaRegalos(null);
        nueva.setTienda(null);
        nueva.setComentario(null);

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
    }

    @Test
    void testCreateFoto_VariasEntidadesAsociadas_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://ejemplo.com/foto.jpg");
        nueva.setTipoArchivo("image/jpeg");
        nueva.setRegalo(regaloList.get(0));
        nueva.setListaRegalos(new ListaRegalosEntity()); // Dos entidades

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
    }

    @Test
    void testCreateFoto_FormatoPNG_Success() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://ejemplo.com/foto.png");
        nueva.setTipoArchivo("image/png");
        nueva.setTamanioBytes(1024L * 1024L);
        nueva.setRegalo(regaloList.get(1));
        nueva.setListaRegalos(null);
        nueva.setTienda(null);
        nueva.setComentario(null);

        FotoEntity creada = fotoService.createFoto(nueva);
        
        assertNotNull(creada);
        assertEquals("image/png", creada.getTipoArchivo());
    }

    // =====================================================
    // UPDATE TESTS
    // =====================================================

    @Test
    void testUpdateFoto_Success() {
        FotoEntity existente = fotoList.get(0);
        FotoEntity actualizada = factory.manufacturePojo(FotoEntity.class);
        actualizada.setDescripcion("Descripción actualizada");
        actualizada.setEsPrincipal(false);

        FotoEntity resultado = fotoService.updateFoto(existente.getId(), actualizada);
        
        assertEquals("Descripción actualizada", resultado.getDescripcion());
    }

    @Test
    void testUpdateFoto_NoExiste_LanzaExcepcion() {
        FotoEntity actualizada = factory.manufacturePojo(FotoEntity.class);
        
        assertThrows(EntityNotFoundException.class, () ->
                fotoService.updateFoto(999L, actualizada));
    }

    @Test
    void testUpdateFoto_CambiarEntidadAsociada_LanzaExcepcion() {
        FotoEntity existente = fotoList.get(0);
        FotoEntity actualizada = factory.manufacturePojo(FotoEntity.class);
        actualizada.setRegalo(regaloList.get(1)); // Diferente regalo

        assertThrows(IllegalStateException.class, () ->
                fotoService.updateFoto(existente.getId(), actualizada));
    }

    @Test
    void testUpdateFoto_MarcarComoPrincipal_DesmarcaOtras() {
        FotoEntity existente = fotoList.get(0);
        FotoEntity actualizada = factory.manufacturePojo(FotoEntity.class);
        actualizada.setEsPrincipal(true);

        FotoEntity resultado = fotoService.updateFoto(existente.getId(), actualizada);
        
        assertTrue(resultado.getEsPrincipal());
    }

    // =====================================================
    // DELETE TESTS
    // =====================================================

    @Test
    void testDeleteFoto_Success() {
        FotoEntity foto = fotoList.get(1);
        
        fotoService.deleteFoto(foto.getId());
        
        assertNull(entityManager.find(FotoEntity.class, foto.getId()));
    }

    @Test
    void testDeleteFoto_NoExiste_LanzaExcepcion() {
        assertThrows(EntityNotFoundException.class, () ->
                fotoService.deleteFoto(999L));
    }

    // =====================================================
    // GET TESTS
    // =====================================================

    @Test
    void testGetAllFotos() {
        List<FotoEntity> lista = fotoService.getAllFotos();
        
        assertEquals(fotoList.size(), lista.size());
    }

    @Test
    void testGetFotoById_Success() {
        FotoEntity foto = fotoList.get(0);
        
        FotoEntity encontrada = fotoService.getFotoById(foto.getId());
        
        assertEquals(foto.getId(), encontrada.getId());
        assertEquals(foto.getUrl(), encontrada.getUrl());
    }

    @Test
    void testGetFotoById_NoExiste_LanzaExcepcion() {
        assertThrows(EntityNotFoundException.class, () ->
                fotoService.getFotoById(999L));
    }

    @Test
    void testGetFotosByRegaloId() {
        List<FotoEntity> fotos = fotoService.getFotosByRegaloId(regaloList.get(0).getId());
        
        assertFalse(fotos.isEmpty());
        assertEquals(regaloList.get(0).getId(), fotos.get(0).getRegalo().getId());
    }

    @Test
    void testGetFotosPrincipales() {
        // Marcar una foto como principal
        FotoEntity foto = fotoList.get(0);
        foto.setEsPrincipal(true);
        entityManager.persist(foto);
        entityManager.flush();

        List<FotoEntity> principales = fotoService.getFotosPrincipales();
        
        assertFalse(principales.isEmpty());
        assertTrue(principales.stream().allMatch(FotoEntity::getEsPrincipal));
    }

    // =====================================================
    // TESTS DE VALIDACIÓN DE FORMATOS
    // =====================================================

    @Test
    void testCreateFoto_FormatoGIF_Success() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://ejemplo.com/animacion.gif");
        nueva.setTipoArchivo("image/gif");
        nueva.setTamanioBytes(1024L * 1024L);
        nueva.setRegalo(regaloList.get(1));
        nueva.setListaRegalos(null);
        nueva.setTienda(null);
        nueva.setComentario(null);

        FotoEntity creada = fotoService.createFoto(nueva);
        
        assertNotNull(creada);
        assertEquals("image/gif", creada.getTipoArchivo());
    }

    @Test
    void testCreateFoto_FormatoWEBP_Success() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://ejemplo.com/moderna.webp");
        nueva.setTipoArchivo("image/webp");
        nueva.setTamanioBytes(1024L * 1024L);
        nueva.setRegalo(regaloList.get(1));
        nueva.setListaRegalos(null);
        nueva.setTienda(null);
        nueva.setComentario(null);

        FotoEntity creada = fotoService.createFoto(nueva);
        
        assertNotNull(creada);
        assertEquals("image/webp", creada.getTipoArchivo());
    }
}