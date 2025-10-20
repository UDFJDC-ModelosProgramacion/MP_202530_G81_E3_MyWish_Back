package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.*;
import co.edu.udistrital.mdp.back.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(MensajeInvitacionService.class)
class MensajeInvitacionServiceTest {

    @Autowired
    private MensajeInvitacionService service;

    @Autowired
    private MensajeInvitacionRepository mensajeRepo;

    @Autowired
    private CelebracionRepository celebracionRepo;

    @Autowired
    private ListaRegalosRepository listaRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

        @Autowired
    private TestEntityManager entityManager;

    @MockBean
    private JavaMailSender mailSender; // Evita enviar correos reales

    private PodamFactory factory = new PodamFactoryImpl();
    private List<UsuarioEntity> usuarios = new ArrayList<>();
    private CelebracionEntity celebracion;
    private ListaRegalosEntity lista;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MensajeInvitacionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from CelebracionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ListaRegalosEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
    }

    private void insertData() {
        // Crear usuarios
        for (int i = 0; i < 3; i++) {
            UsuarioEntity u = factory.manufacturePojo(UsuarioEntity.class);
            u.setCorreo("usuario" + i + "@correo.com");
            entityManager.persist(u);
            usuarios.add(u);
        }

        // Crear celebración
        celebracion = factory.manufacturePojo(CelebracionEntity.class);
        celebracion.setOrganizador(usuarios.get(0));
        entityManager.persist(celebracion);

        // Crear lista de regalos
        lista = factory.manufacturePojo(ListaRegalosEntity.class);
        lista.setCreador(usuarios.get(1));
        entityManager.persist(lista);

        entityManager.flush();
    }

    // =====================================================
    // PRUEBAS DEL MÉTODO enviarInvitacion()
    // =====================================================

    @Test
    void testEnviarInvitacionCelebracionSuccess() {
        Mockito.when(mailSender.createMimeMessage()).thenReturn(Mockito.mock(jakarta.mail.internet.MimeMessage.class));

        MensajeInvitacionEntity mensaje = service.enviarInvitacion(
                "celebracion",
                celebracion.getId(),
                usuarios.get(2).getCorreo(),
                "Te invito a mi fiesta"
        );

        assertNotNull(mensaje.getId());
        assertEquals(usuarios.get(2).getId(), mensaje.getDestinatario().getId());
        assertEquals(celebracion.getId(), mensaje.getCelebracion().getId());
        assertEquals(usuarios.get(0).getId(), mensaje.getRemitente().getId());
    }

    @Test
    void testEnviarInvitacionListaSuccess() {
        Mockito.when(mailSender.createMimeMessage()).thenReturn(Mockito.mock(jakarta.mail.internet.MimeMessage.class));

        MensajeInvitacionEntity mensaje = service.enviarInvitacion(
                "lista",
                lista.getId(),
                usuarios.get(2).getCorreo(),
                "Te invito a ver mi lista de regalos"
        );

        assertNotNull(mensaje.getId());
        assertEquals(lista.getId(), mensaje.getListaRegalos().getId());
        assertEquals(usuarios.get(1).getId(), mensaje.getRemitente().getId());
    }

    @Test
    void testEnviarInvitacionUsuarioNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            service.enviarInvitacion("lista", lista.getId(), "inexistente@correo.com", "Hola");
        });
    }

    @Test
    void testEnviarInvitacionCelebracionNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            service.enviarInvitacion("celebracion", 999L, usuarios.get(1).getCorreo(), "Hola");
        });
    }

    @Test
    void testEnviarInvitacionTipoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.enviarInvitacion("evento", celebracion.getId(), usuarios.get(1).getCorreo(), "Mensaje");
        });
    }

    // =====================================================
    // CRUD
    // =====================================================

    @Test
    void testGetAll() {
        Mockito.when(mailSender.createMimeMessage()).thenReturn(Mockito.mock(jakarta.mail.internet.MimeMessage.class));
        service.enviarInvitacion("celebracion", celebracion.getId(), usuarios.get(2).getCorreo(), "msg");
        service.enviarInvitacion("lista", lista.getId(), usuarios.get(2).getCorreo(), "msg2");

        List<MensajeInvitacionEntity> all = service.getAll();
        assertEquals(2, all.size());
    }

    @Test
    void testGetByIdSuccess() {
        Mockito.when(mailSender.createMimeMessage()).thenReturn(Mockito.mock(jakarta.mail.internet.MimeMessage.class));
        MensajeInvitacionEntity enviado = service.enviarInvitacion("celebracion", celebracion.getId(), usuarios.get(2).getCorreo(), "msg");

        MensajeInvitacionEntity encontrado = service.getById(enviado.getId());
        assertEquals(enviado.getId(), encontrado.getId());
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.getById(999L));
    }

    @Test
    void testUpdateSuccess() {
        Mockito.when(mailSender.createMimeMessage()).thenReturn(Mockito.mock(jakarta.mail.internet.MimeMessage.class));
        MensajeInvitacionEntity enviado = service.enviarInvitacion("celebracion", celebracion.getId(), usuarios.get(2).getCorreo(), "msg");

        MensajeInvitacionEntity nuevo = new MensajeInvitacionEntity();
        nuevo.setMensaje("Mensaje actualizado");
        nuevo.setFechaEnvio(new Date());

        MensajeInvitacionEntity actualizado = service.update(enviado.getId(), nuevo);

        assertEquals("Mensaje actualizado", actualizado.getMensaje());
    }

    @Test
    void testDeleteSuccess() {
        Mockito.when(mailSender.createMimeMessage()).thenReturn(Mockito.mock(jakarta.mail.internet.MimeMessage.class));
        MensajeInvitacionEntity enviado = service.enviarInvitacion("celebracion", celebracion.getId(), usuarios.get(2).getCorreo(), "msg");

        service.delete(enviado.getId());

        assertFalse(mensajeRepo.findById(enviado.getId()).isPresent());
    }

    @Test
    void testDeleteNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(999L));
    }
}
