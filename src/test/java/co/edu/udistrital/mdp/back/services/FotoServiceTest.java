package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.FotoEntity;
import co.edu.udistrital.mdp.back.entities.RegaloEntity;
import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.entities.ComentarioEntity;
import co.edu.udistrital.mdp.back.repositories.FotoRepository;
import co.edu.udistrital.mdp.back.repositories.RegaloRepository;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;
import co.edu.udistrital.mdp.back.repositories.ComentarioRepository;
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

    @Autowired
    private TiendaRepository tiendaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<FotoEntity> fotoList = new ArrayList<>();
    private List<RegaloEntity> regaloList = new ArrayList<>();
    private List<ListaRegalosEntity> listaRegalosList = new ArrayList<>();
    private List<TiendaEntity> tiendaList = new ArrayList<>();
    private List<ComentarioEntity> comentarioList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from FotoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from RegaloEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ListaRegalosEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TiendaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ComentarioEntity").executeUpdate();
    }

    private void insertData() {
        // Crear regalos
        for (int i = 0; i < 3; i++) {
            RegaloEntity regalo = factory.manufacturePojo(RegaloEntity.class);
            entityManager.persist(regalo);
            regaloList.add(regalo);
        }

        // Crear listas de regalos
        for (int i = 0; i < 2; i++) {
            ListaRegalosEntity lista = factory.manufacturePojo(ListaRegalosEntity.class);
            entityManager.persist(lista);
            listaRegalosList.add(lista);
        }

        // Crear tiendas
        for (int i = 0; i < 2; i++) {
            TiendaEntity tienda = factory.manufacturePojo(TiendaEntity.class);
            entityManager.persist(tienda);
            tiendaList.add(tienda);
        }

        // Crear comentarios
        for (int i = 0; i < 2; i++) {
            ComentarioEntity comentario = factory.manufacturePojo(ComentarioEntity.class);
            entityManager.persist(comentario);
            comentarioList.add(comentario);
        }

        // Crear fotos para diferentes entidades
        // Foto para regalo (OneToOne)
        FotoEntity fotoRegalo = factory.manufacturePojo(FotoEntity.class);
        fotoRegalo.setUrl("https://ejemplo.com/regalo1.jpg");
        fotoRegalo.setTipoArchivo("image/jpeg");
        fotoRegalo.setTamanioBytes(1024L * 1024L);
        fotoRegalo.setEsPrincipal(false);
        fotoRegalo.setRegalo(regaloList.get(0));
        fotoRegalo.setListaRegalos(null);
        fotoRegalo.setTienda(null);
        fotoRegalo.setComentario(null);
        entityManager.persist(fotoRegalo);
        fotoList.add(fotoRegalo);

        // Foto para tienda (ManyToOne)
        FotoEntity fotoTienda = factory.manufacturePojo(FotoEntity.class);
        fotoTienda.setUrl("https://ejemplo.com/tienda1.jpg");
        fotoTienda.setTipoArchivo("image/png");
        fotoTienda.setTamanioBytes(2048L * 1024L);
        fotoTienda.setEsPrincipal(true);
        fotoTienda.setRegalo(null);
        fotoTienda.setListaRegalos(null);
        fotoTienda.setTienda(tiendaList.get(0));
        fotoTienda.setComentario(null);
        entityManager.persist(fotoTienda);
        fotoList.add(fotoTienda);

        // Foto para comentario (ManyToOne)
        FotoEntity fotoComentario = factory.manufacturePojo(FotoEntity.class);
        fotoComentario.setUrl("https://ejemplo.com/comentario1.jpg");
        fotoComentario.setTipoArchivo("image/gif");
        fotoComentario.setTamanioBytes(512L * 1024L);
        fotoComentario.setEsPrincipal(false);
        fotoComentario.setRegalo(null);
        fotoComentario.setListaRegalos(null);
        fotoComentario.setTienda(null);
        fotoComentario.setComentario(comentarioList.get(0));
        entityManager.persist(fotoComentario);
        fotoList.add(fotoComentario);
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
        nueva.setRegalo(regaloList.get(1)); // Regalo sin foto
        nueva.setListaRegalos(null);
        nueva.setTienda(null);
        nueva.setComentario(null);

        FotoEntity creada = fotoService.createFoto(nueva);
        
        assertNotNull(creada);
        assertNotNull(creada.getId());
        assertEquals("https://ejemplo.com/nueva.jpg", creada.getUrl());
    }

    @Test
    void testCreateFoto_RegaloYaTieneFoto_LanzaExcepcion() {
        // Intentar crear segunda foto para el mismo regalo que ya tiene una
        FotoEntity segundaFoto = factory.manufacturePojo(FotoEntity.class);
        segundaFoto.setUrl("https://ejemplo.com/segunda.jpg");
        segundaFoto.setTipoArchivo("image/jpeg");
        segundaFoto.setRegalo(regaloList.get(0)); // Mismo regalo que ya tiene foto

        assertThrows(IllegalStateException.class, () ->
                fotoService.createFoto(segundaFoto));
    }

    @Test
    void testCreateFoto_ListaRegalosYaTieneFoto_LanzaExcepcion() {
        // Primero crear una foto para una lista de regalos
        FotoEntity primeraFoto = factory.manufacturePojo(FotoEntity.class);
        primeraFoto.setUrl("https://ejemplo.com/primera-lista.jpg");
        primeraFoto.setTipoArchivo("image/jpeg");
        primeraFoto.setListaRegalos(listaRegalosList.get(0));
        entityManager.persist(primeraFoto);
        entityManager.flush();

        // Intentar crear segunda foto para la misma lista
        FotoEntity segundaFoto = factory.manufacturePojo(FotoEntity.class);
        segundaFoto.setUrl("https://ejemplo.com/segunda-lista.jpg");
        segundaFoto.setListaRegalos(listaRegalosList.get(0)); // Misma lista

        assertThrows(IllegalStateException.class, () ->
                fotoService.createFoto(segundaFoto));
    }

    @Test
    void testCreateFoto_URLInvalida_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("ejemplo.com/foto.jpg"); // Sin http/https
        nueva.setRegalo(regaloList.get(1));

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
    }

    @Test
    void testCreateFoto_TamanioExcedido_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://ejemplo.com/grande.jpg");
        nueva.setTamanioBytes(11L * 1024L * 1024L); // 11MB (excede el límite)
        nueva.setRegalo(regaloList.get(1));

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
    }

    @Test
    void testCreateFoto_FormatoInvalido_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://ejemplo.com/archivo.pdf");
        nueva.setTipoArchivo("application/pdf");
        nueva.setRegalo(regaloList.get(1));

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
        nueva.setRegalo(regaloList.get(1));
        nueva.setListaRegalos(listaRegalosList.get(1)); // Dos entidades

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
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
        FotoEntity existente = fotoList.get(1); // Foto de tienda (puede tener múltiples)
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
        FotoEntity foto = fotoList.get(2);
        
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
    void testGetFotosByListaRegalosId() {
        // Crear una foto para lista de regalos
        FotoEntity fotoLista = factory.manufacturePojo(FotoEntity.class);
        fotoLista.setUrl("https://ejemplo.com/lista.jpg");
        fotoLista.setListaRegalos(listaRegalosList.get(1));
        entityManager.persist(fotoLista);
        entityManager.flush();

        List<FotoEntity> fotos = fotoService.getFotosByListaRegalosId(listaRegalosList.get(1).getId());
        
        assertFalse(fotos.isEmpty());
        assertEquals(listaRegalosList.get(1).getId(), fotos.get(0).getListaRegalos().getId());
    }

    @Test
    void testGetFotosByTiendaId() {
        List<FotoEntity> fotos = fotoService.getFotosByTiendaId(tiendaList.get(0).getId());
        
        assertFalse(fotos.isEmpty());
        assertEquals(tiendaList.get(0).getId(), fotos.get(0).getTienda().getId());
    }

    @Test
    void testGetFotosByComentarioId() {
        List<FotoEntity> fotos = fotoService.getFotosByComentarioId(comentarioList.get(0).getId());
        
        assertFalse(fotos.isEmpty());
        assertEquals(comentarioList.get(0).getId(), fotos.get(0).getComentario().getId());
    }

    @Test
    void testGetFotosPrincipales() {
        List<FotoEntity> principales = fotoService.getFotosPrincipales();
        
        assertFalse(principales.isEmpty());
        assertTrue(principales.stream().allMatch(FotoEntity::getEsPrincipal));
    }

    // =====================================================
    // TESTS DE VALIDACIÓN DE FORMATOS
    // =====================================================

    @Test
    void testCreateFoto_FormatoPNG_Success() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://ejemplo.com/foto.png");
        nueva.setTipoArchivo("image/png");
        nueva.setTamanioBytes(1024L * 1024L);
        nueva.setRegalo(regaloList.get(2));
        nueva.setListaRegalos(null);
        nueva.setTienda(null);
        nueva.setComentario(null);

        FotoEntity creada = fotoService.createFoto(nueva);
        
        assertNotNull(creada);
        assertEquals("image/png", creada.getTipoArchivo());
    }

    @Test
    void testCreateFoto_FormatoGIF_Success() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setUrl("https://ejemplo.com/animacion.gif");
        nueva.setTipoArchivo("image/gif");
        nueva.setTamanioBytes(1024L * 1024L);
        nueva.setRegalo(regaloList.get(2));
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
        nueva.setRegalo(regaloList.get(2));
        nueva.setListaRegalos(null);
        nueva.setTienda(null);
        nueva.setComentario(null);

        FotoEntity creada = fotoService.createFoto(nueva);
        
        assertNotNull(creada);
        assertEquals("image/webp", creada.getTipoArchivo());
    }
}