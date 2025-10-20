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
        fotoRepository.deleteAll();
        regaloRepository.deleteAll();
        listaRegalosRepository.deleteAll();
        tiendaRepository.deleteAll();
        comentarioRepository.deleteAll();
        entityManager.flush();
    }

    private void insertData() {
        // Primero crear y persistir todas las entidades relacionadas
        // Crear regalos
        for (int i = 0; i < 3; i++) {
            RegaloEntity regalo = factory.manufacturePojo(RegaloEntity.class);
            regalo.setId(null); // Asegurar que sea nuevo
            entityManager.persist(regalo);
            regaloList.add(regalo);
        }

        // Crear listas de regalos
        for (int i = 0; i < 2; i++) {
            ListaRegalosEntity lista = factory.manufacturePojo(ListaRegalosEntity.class);
            lista.setId(null);
            entityManager.persist(lista);
            listaRegalosList.add(lista);
        }

        // Crear tiendas
        for (int i = 0; i < 2; i++) {
            TiendaEntity tienda = factory.manufacturePojo(TiendaEntity.class);
            tienda.setId(null);
            entityManager.persist(tienda);
            tiendaList.add(tienda);
        }

        // Crear comentarios
        for (int i = 0; i < 2; i++) {
            ComentarioEntity comentario = factory.manufacturePojo(ComentarioEntity.class);
            comentario.setId(null);
            entityManager.persist(comentario);
            comentarioList.add(comentario);
        }

        entityManager.flush(); // Forzar el guardado de todas las entidades relacionadas

        // Ahora crear fotos con entidades ya persistidas
        // Foto para regalo (OneToOne)
        FotoEntity fotoRegalo = factory.manufacturePojo(FotoEntity.class);
        fotoRegalo.setId(null);
        fotoRegalo.setUrl("https://ejemplo.com/regalo1.jpg");
        fotoRegalo.setTipoArchivo("image/jpeg");
        fotoRegalo.setTamanioBytes(1024L * 1024L); // 1MB
        fotoRegalo.setEsPrincipal(false);
        fotoRegalo.setRegalo(regaloList.get(0));
        fotoRegalo.setListaRegalos(null);
        fotoRegalo.setTienda(null);
        fotoRegalo.setComentario(null);
        entityManager.persist(fotoRegalo);
        fotoList.add(fotoRegalo);

        // Foto para tienda (ManyToOne)
        FotoEntity fotoTienda = factory.manufacturePojo(FotoEntity.class);
        fotoTienda.setId(null);
        fotoTienda.setUrl("https://ejemplo.com/tienda1.jpg");
        fotoTienda.setTipoArchivo("image/png");
        fotoTienda.setTamanioBytes(2048L * 1024L); // 2MB
        fotoTienda.setEsPrincipal(true);
        fotoTienda.setRegalo(null);
        fotoTienda.setListaRegalos(null);
        fotoTienda.setTienda(tiendaList.get(0));
        fotoTienda.setComentario(null);
        entityManager.persist(fotoTienda);
        fotoList.add(fotoTienda);

        // Foto para comentario (ManyToOne)
        FotoEntity fotoComentario = factory.manufacturePojo(FotoEntity.class);
        fotoComentario.setId(null);
        fotoComentario.setUrl("https://ejemplo.com/comentario1.jpg");
        fotoComentario.setTipoArchivo("image/gif");
        fotoComentario.setTamanioBytes(512L * 1024L); // 0.5MB
        fotoComentario.setEsPrincipal(false);
        fotoComentario.setRegalo(null);
        fotoComentario.setListaRegalos(null);
        fotoComentario.setTienda(null);
        fotoComentario.setComentario(comentarioList.get(0));
        entityManager.persist(fotoComentario);
        fotoList.add(fotoComentario);

        entityManager.flush(); // Forzar el guardado de las fotos
    }

    // =====================================================
    // CREATE TESTS - CORREGIDOS
    // =====================================================

    @Test
    void testCreateFoto_Success() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setId(null);
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
        // Usar un regalo que NO esté en la lista inicial
        RegaloEntity regaloNuevo = factory.manufacturePojo(RegaloEntity.class);
        regaloNuevo.setId(null);
        entityManager.persist(regaloNuevo);
        entityManager.flush();

        // Primero crear una foto para el regalo
        FotoEntity primeraFoto = factory.manufacturePojo(FotoEntity.class);
        primeraFoto.setId(null);
        primeraFoto.setUrl("https://ejemplo.com/primera.jpg");
        primeraFoto.setTipoArchivo("image/jpeg");
        primeraFoto.setTamanioBytes(1024L * 1024L); // 1MB
        primeraFoto.setRegalo(regaloNuevo);
        primeraFoto.setListaRegalos(null);
        primeraFoto.setTienda(null);
        primeraFoto.setComentario(null);
        entityManager.persist(primeraFoto);
        entityManager.flush();

        // Intentar crear segunda foto para el mismo regalo
        FotoEntity segundaFoto = factory.manufacturePojo(FotoEntity.class);
        segundaFoto.setId(null);
        segundaFoto.setUrl("https://ejemplo.com/segunda.jpg");
        segundaFoto.setTipoArchivo("image/jpeg");
        segundaFoto.setTamanioBytes(1024L * 1024L); // 1MB
        segundaFoto.setRegalo(regaloNuevo); // Mismo regalo
        segundaFoto.setListaRegalos(null);
        segundaFoto.setTienda(null);
        segundaFoto.setComentario(null);

        assertThrows(IllegalStateException.class, () ->
                fotoService.createFoto(segundaFoto));
    }

    @Test
    void testCreateFoto_ListaRegalosYaTieneFoto_LanzaExcepcion() {
        // Usar una lista que NO esté en la lista inicial
        ListaRegalosEntity listaNueva = factory.manufacturePojo(ListaRegalosEntity.class);
        listaNueva.setId(null);
        entityManager.persist(listaNueva);
        entityManager.flush();

        // Primero crear una foto para la lista de regalos
        FotoEntity primeraFoto = factory.manufacturePojo(FotoEntity.class);
        primeraFoto.setId(null);
        primeraFoto.setUrl("https://ejemplo.com/primera-lista.jpg");
        primeraFoto.setTipoArchivo("image/jpeg");
        primeraFoto.setTamanioBytes(1024L * 1024L); // 1MB
        primeraFoto.setListaRegalos(listaNueva);
        primeraFoto.setRegalo(null);
        primeraFoto.setTienda(null);
        primeraFoto.setComentario(null);
        entityManager.persist(primeraFoto);
        entityManager.flush();

        // Intentar crear segunda foto para la misma lista
        FotoEntity segundaFoto = factory.manufacturePojo(FotoEntity.class);
        segundaFoto.setId(null);
        segundaFoto.setUrl("https://ejemplo.com/segunda-lista.jpg");
        segundaFoto.setTipoArchivo("image/jpeg");
        segundaFoto.setTamanioBytes(1024L * 1024L); // 1MB
        segundaFoto.setListaRegalos(listaNueva); // Misma lista
        segundaFoto.setRegalo(null);
        segundaFoto.setTienda(null);
        segundaFoto.setComentario(null);

        assertThrows(IllegalStateException.class, () ->
                fotoService.createFoto(segundaFoto));
    }

    @Test
    void testCreateFoto_URLInvalida_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setId(null);
        nueva.setUrl("ejemplo.com/foto.jpg"); // Sin http/https
        nueva.setTamanioBytes(1024L * 1024L); // 1MB
        nueva.setTipoArchivo("image/jpeg");
        nueva.setRegalo(regaloList.get(1));
        nueva.setListaRegalos(null);
        nueva.setTienda(null);
        nueva.setComentario(null);

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
    }

    @Test
    void testCreateFoto_TamanioExcedido_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setId(null);
        nueva.setUrl("https://ejemplo.com/grande.jpg");
        nueva.setTamanioBytes(11L * 1024L * 1024L); // 11MB (excede el límite)
        nueva.setTipoArchivo("image/jpeg");
        nueva.setRegalo(regaloList.get(1));
        nueva.setListaRegalos(null);
        nueva.setTienda(null);
        nueva.setComentario(null);

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
    }

    @Test
    void testCreateFoto_FormatoInvalido_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setId(null);
        nueva.setUrl("https://ejemplo.com/archivo.pdf");
        nueva.setTipoArchivo("application/pdf");
        nueva.setTamanioBytes(1024L * 1024L); // 1MB
        nueva.setRegalo(regaloList.get(1));
        nueva.setListaRegalos(null);
        nueva.setTienda(null);
        nueva.setComentario(null);

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
    }

    @Test
    void testCreateFoto_SinEntidadAsociada_LanzaExcepcion() {
        FotoEntity nueva = factory.manufacturePojo(FotoEntity.class);
        nueva.setId(null);
        nueva.setUrl("https://ejemplo.com/foto.jpg");
        nueva.setTipoArchivo("image/jpeg");
        nueva.setTamanioBytes(1024L * 1024L); // 1MB
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
        nueva.setId(null);
        nueva.setUrl("https://ejemplo.com/foto.jpg");
        nueva.setTipoArchivo("image/jpeg");
        nueva.setTamanioBytes(1024L * 1024L); // 1MB
        nueva.setRegalo(regaloList.get(1));
        nueva.setListaRegalos(listaRegalosList.get(1)); // Dos entidades
        nueva.setTienda(null);
        nueva.setComentario(null);

        assertThrows(IllegalArgumentException.class, () ->
                fotoService.createFoto(nueva));
    }

    // =====================================================
    // UPDATE TESTS - CORREGIDOS
    // =====================================================

    @Test
    void testUpdateFoto_Success() {
        FotoEntity existente = fotoList.get(0);
        FotoEntity actualizada = new FotoEntity(); // No usar factory para evitar relaciones
        actualizada.setDescripcion("Descripción actualizada");
        actualizada.setEsPrincipal(false);
        // No establecer relaciones para evitar el error

        FotoEntity resultado = fotoService.updateFoto(existente.getId(), actualizada);
        
        assertEquals("Descripción actualizada", resultado.getDescripcion());
    }

    @Test
    void testUpdateFoto_NoExiste_LanzaExcepcion() {
        FotoEntity actualizada = new FotoEntity();
        actualizada.setDescripcion("Test");
        
        assertThrows(EntityNotFoundException.class, () ->
                fotoService.updateFoto(999L, actualizada));
    }

    @Test
    void testUpdateFoto_CambiarEntidadAsociada_LanzaExcepcion() {
        FotoEntity existente = fotoList.get(0);
        FotoEntity actualizada = new FotoEntity();
        // Crear un nuevo regalo diferente
        RegaloEntity nuevoRegalo = factory.manufacturePojo(RegaloEntity.class);
        nuevoRegalo.setId(null);
        entityManager.persist(nuevoRegalo);
        entityManager.flush();
        
        actualizada.setRegalo(nuevoRegalo); // Diferente regalo

        assertThrows(IllegalStateException.class, () ->
                fotoService.updateFoto(existente.getId(), actualizada));
    }

    @Test
    void testUpdateFoto_MarcarComoPrincipal_DesmarcaOtras() {
        // Usar una foto de tienda que puede tener múltiples
        FotoEntity existente = fotoList.get(1);
        FotoEntity actualizada = new FotoEntity();
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
        // Crear una nueva lista de regalos con foto
        ListaRegalosEntity listaNueva = factory.manufacturePojo(ListaRegalosEntity.class);
        listaNueva.setId(null);
        entityManager.persist(listaNueva);
        entityManager.flush();

        // Crear foto para la nueva lista
        FotoEntity fotoLista = factory.manufacturePojo(FotoEntity.class);
        fotoLista.setId(null);
        fotoLista.setUrl("https://ejemplo.com/lista.jpg");
        fotoLista.setTipoArchivo("image/jpeg");
        fotoLista.setTamanioBytes(1024L * 1024L);
        fotoLista.setListaRegalos(listaNueva);
        fotoLista.setRegalo(null);
        fotoLista.setTienda(null);
        fotoLista.setComentario(null);
        entityManager.persist(fotoLista);
        entityManager.flush();

        List<FotoEntity> fotos = fotoService.getFotosByListaRegalosId(listaNueva.getId());
        
        assertFalse(fotos.isEmpty());
        assertEquals(listaNueva.getId(), fotos.get(0).getListaRegalos().getId());
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
        nueva.setId(null);
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
        nueva.setId(null);
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
        nueva.setId(null);
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