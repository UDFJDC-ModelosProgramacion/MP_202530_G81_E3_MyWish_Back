package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;
import co.edu.udistrital.mdp.back.repositories.CelebracionRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(UsuarioService.class)
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ListaRegalosRepository listaRegalosRepository;

    @Autowired
    private CelebracionRepository celebracionRepository;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<UsuarioEntity> usuarios = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM ListaRegalosEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM CelebracionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM UsuarioEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            UsuarioEntity usuario = factory.manufacturePojo(UsuarioEntity.class);
            usuario.setFechaNacimiento(new Date(System.currentTimeMillis() - 1000000));
            entityManager.persist(usuario);
            usuarios.add(usuario);
        }
    }

    @Test
    void testCrearUsuario_Valido() {
        UsuarioEntity nuevo = factory.manufacturePojo(UsuarioEntity.class);
        nuevo.setCorreo("test@example.com");
        nuevo.setFechaNacimiento(new Date());

        UsuarioEntity creado = usuarioService.crearUsuario(nuevo);

        assertNotNull(creado.getId());
        assertEquals("test@example.com", creado.getCorreo());
    }

    @Test
    void testCrearUsuario_CorreoNulo() {
        UsuarioEntity nuevo = factory.manufacturePojo(UsuarioEntity.class);
        nuevo.setCorreo(null);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.crearUsuario(nuevo));
    }

    @Test
    void testActualizarCorreoUsuario_Exitoso() {
        UsuarioEntity usuario = usuarios.get(0);
        String nuevoCorreo = "nuevo@mail.com";

        UsuarioEntity actualizado = usuarioService.actualizarCorreoUsuario(usuario.getId(), nuevoCorreo);

        assertEquals(nuevoCorreo, actualizado.getCorreo());
    }

    @Test
    void testEliminarUsuario_SinRelaciones_EliminaCorrectamente() {
        UsuarioEntity usuario = usuarios.get(1);

        usuarioService.eliminarUsuario(usuario.getId());

        assertFalse(usuarioRepository.findById(usuario.getId()).isPresent());
    }

    @Test
    void testEliminarUsuario_ConListas_LanzaExcepcion() {
        UsuarioEntity usuario = usuarios.get(0);

        ListaRegalosEntity lista = factory.manufacturePojo(ListaRegalosEntity.class);
        lista.setCreador(usuario);
        entityManager.persist(lista);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                usuarioService.eliminarUsuario(usuario.getId())
        );

        assertEquals("No se puede eliminar un usuario con listas creadas activas.", ex.getMessage());
    }

    @Test
    void testEliminarUsuario_ConCelebraciones_LanzaExcepcion() {
        UsuarioEntity usuario = usuarios.get(0);

        CelebracionEntity celebracion = factory.manufacturePojo(CelebracionEntity.class);
        celebracion.setOrganizador(usuario);
        entityManager.persist(celebracion);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                usuarioService.eliminarUsuario(usuario.getId())
        );

        assertEquals("No se puede eliminar un usuario con celebraciones organizadas pendientes.", ex.getMessage());
    }
}
