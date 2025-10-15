package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import co.edu.udistrital.mdp.back.repositories.OcasionRepository;
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
@Import(OcasionService.class)
class OcasionServiceTest {

    @Autowired
    private OcasionService ocasionService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OcasionRepository ocasionRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<OcasionEntity> ocasionList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from OcasionEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            OcasionEntity entity = factory.manufacturePojo(OcasionEntity.class);
            entityManager.persist(entity);
            ocasionList.add(entity);
        }
    }

    @Test
    void createOcasion_valida_ok() {
        OcasionEntity nueva = new OcasionEntity();
        nueva.setNombre("Aniversario");

        OcasionEntity result = ocasionService.createOcasion(nueva);

        assertNotNull(result);
        assertEquals("Aniversario", result.getNombre());
    }

    @Test
    void createOcasion_nombreVacio_error() {
        OcasionEntity nueva = new OcasionEntity();
        nueva.setNombre("");

        assertThrows(IllegalArgumentException.class, () -> ocasionService.createOcasion(nueva));
    }

    @Test
    void updateOcasion_valida_ok() {
        OcasionEntity original = ocasionList.get(0);
        OcasionEntity actualizada = ocasionService.updateOcasion(original.getId(), "Cumpleaños");

        assertEquals("Cumpleaños", actualizada.getNombre());
    }

    @Test
    void updateOcasion_nombreDuplicado_error() {
        OcasionEntity original = ocasionList.get(0);
        OcasionEntity otra = ocasionList.get(1);

        assertThrows(IllegalArgumentException.class, () ->
                ocasionService.updateOcasion(original.getId(), otra.getNombre()));
    }
}
