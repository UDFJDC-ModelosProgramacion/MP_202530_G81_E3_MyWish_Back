package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.*;
import co.edu.udistrital.mdp.back.repositories.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(MensajeInvitacionService.class)
class MensajeInvitacionServiceTest {

    @Autowired
    private MensajeInvitacionService mensajeService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private CelebracionRepository celebracionRepo;

    @Autowired
    private ListaRegalosRepository listaRepo;

    @Autowired
    private MensajeInvitacionRepository mensajeRepo;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<UsuarioEntity> usuarioList = new ArrayList<>();
    private List<CelebracionEntity> celebracionList = new ArrayList<>();
    private List<ListaRegalosEntity> listaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // Configurar un JavaMailSender “fake” que no envía correos
        mensajeService.setMailSender(new org.springframework.mail.javamail.JavaMailSender() {
            @Override
            public MimeMessage createMimeMessage() {
                return new MimeMessage((javax.mail.Session) null);
            }

            @Override
            public MimeMessage createMimeMessage(java.io.InputStream contentStream) {
                return new MimeMessage((javax.mail.Session) null);
            }

            @Override
            public void send(MimeMessage mimeMessage) { /* No hace nada */ }

            @Override
            public void send(MimeMessage... mimeMessages) { /* No hace nada */ }

            @Override
            public void send(org.springframework.mail.SimpleMailMessage simpleMessage) { /* No hace nada */ }

            @Override
            public void send(org.springframework.mail.SimpleMailMessage... simpleMessages) { /* No hace nada */ }
        });

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
            UsuarioEntity usuario = factory.manufacturePojo(UsuarioEntity.class);
            entityManager.persist(usuario);
            usuarioList.add(usuario);
        }

        // Crear celebraciones
        for (int i = 0; i < 2; i++) {
            CelebracionEntity celebracion = factory.manufacturePojo(CelebracionEntity.class);
            celebracion.setOrganizador(usuarioList.get(0));
            entityManager.persist(celebracion);
            celebracionList.add(celebracion);
        }

        // Crear listas de regalos
        for (int i = 0; i < 2; i++) {
            ListaRegalosEntity lista = factory.manufacturePojo(ListaRegalosEntity.class);
            lista.setCreador(usuarioList.get(1));
            entityManager.persist(lista);
            listaList.add(lista);
        }
    }

    @Test
    void testEnviarInvitacionCelebracion() throws MessagingException {
        MensajeInvitacionEntity mensaje = mensajeService.enviarInvitacion(
                "celebracion",
                celebracionList.get(0).getId(),
                usuarioList.get(2).getCorreo(),
                "¡Ven a la fiesta!"
        );

        assertNotNull(mensaje.getId());
        assertEquals("¡Ven a la fiesta!", mensaje.getMensaje());
        assertEquals(celebracionList.get(0).getId(), mensaje.getCelebracion().getId());
        assertEquals(usuarioList.get(2).getId(), mensaje.getDestinatario().getId());
    }

    @Test
    void testEnviarInvitacionListaRegalos() throws MessagingException {
        MensajeInvitacionEntity mensaje = mensajeService.enviarInvitacion(
                "lista",
                listaList.get(0).getId(),
                usuarioList.get(2).getCorreo(),
                "¡Participa en mi lista!"
        );

        assertNotNull(mensaje.getId());
        assertEquals("¡Participa en mi lista!", mensaje.getMensaje());
        assertEquals(listaList.get(0).getId(), mensaje.getListaRegalos().getId());
        assertEquals(usuarioList.get(2).getId(), mensaje.getDestinatario().getId());
    }

    @Test
    void testGetAll() {
        List<MensajeInvitacionEntity> all = mensajeService.getAll();
        assertNotNull(all);
        assertEquals(0, all.size());
    }

    @Test
    void testUpdateAndDelete() {
        MensajeInvitacionEntity mensaje = new MensajeInvitacionEntity();
        mensaje.setDestinatario(usuarioList.get(2));
        mensaje.setRemitente(usuarioList.get(0));
        mensaje.setMensaje("Original");
        mensaje.setFechaEnvio(new Date());
        mensajeRepo.save(mensaje);

        // Actualizar
        MensajeInvitacionEntity nuevo = new MensajeInvitacionEntity();
        nuevo.setMensaje("Actualizado");
        nuevo.setFechaEnvio(new Date());
        MensajeInvitacionEntity actualizado = mensajeService.update(mensaje.getId(), nuevo);

        assertEquals("Actualizado", actualizado.getMensaje());

        // Eliminar
        mensajeService.delete(mensaje.getId());
        assertThrows(EntityNotFoundException.class, () -> mensajeService.getById(mensaje.getId()));
    }

    @Test
    void testEnviarInvitacionUsuarioNoExiste() {
        assertThrows(EntityNotFoundException.class,
                () -> mensajeService.enviarInvitacion("celebracion", 1L, "correo@noexiste.com", "Mensaje"));
    }

    @Test
    void testEnviarInvitacionTipoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> mensajeService.enviarInvitacion("invalido", 1L, usuarioList.get(0).getCorreo(), "Mensaje"));
    }
}
