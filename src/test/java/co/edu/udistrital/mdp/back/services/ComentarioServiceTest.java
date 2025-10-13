package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.ComentarioEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.ComentarioRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(ComentarioService.class)
class ComentarioServiceTest {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ComentarioRepository comentarioRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ComentarioEntity> comentarioList = new ArrayList<>();

    private UsuarioEntity usuario;
    private TiendaEntity tienda;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ComentarioEntity").executeUpdate();
    }

    private void insertData() {
        usuario = factory.manufacturePojo(UsuarioEntity.class);
        tienda = factory.manufacturePojo(TiendaEntity.class);
        entityManager.persist(usuario);
        entityManager.persist(tienda);

        for (int i = 0; i < 3; i++) {
            ComentarioEntity entity = factory.manufacturePojo(ComentarioEntity.class);
            entity.setUsuario(usuario);
            entity.setTienda(tienda);
            entityManager.persist(entity);
            comentarioList.add(entity);
        }
    }

    @Test
    void createComentario_valido_ok() {
        ComentarioEntity nuevo = new ComentarioEntity();
        nuevo.setTexto("Excelente servicio");
        nuevo.setCalificacion(5);
        nuevo.setUsuario(usuario);
        nuevo.setTienda(tienda);

        ComentarioEntity result = comentarioService.createComentario(nuevo);

        assertNotNull(result);
        assertEquals(5, result.getCalificacion());
    }
 
    @Test
    void createComentario_sinTexto_error() {
        ComentarioEntity nuevo = new ComentarioEntity();
        nuevo.setTexto("");
        nuevo.setCalificacion(3);
        nuevo.setUsuario(usuario);
        nuevo.setTienda(tienda);

        assertThrows(IllegalArgumentException.class, () -> comentarioService.createComentario(nuevo));
    }

    @Test
    void updateComentario_valido_ok() {
        ComentarioEntity original = comentarioList.get(0);
        ComentarioEntity actualizado = comentarioService.updateComentario(original.getId(), "Muy buena atención", 4);
        assertEquals(4, actualizado.getCalificacion());
    }

    @Test
    void updateComentario_calificacionInvalida_error() {
        ComentarioEntity original = comentarioList.get(0);
        assertThrows(IllegalArgumentException.class, () ->
                comentarioService.updateComentario(original.getId(), "Texto", 10));
    }
}

