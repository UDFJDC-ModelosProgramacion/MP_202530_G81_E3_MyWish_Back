package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.MonedaEntity;
import co.edu.udistrital.mdp.back.repositories.MonedaRepository;
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
@Import(MonedaService.class)
class MonedaServiceTest {

    @Autowired 
    private MonedaService monedaService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MonedaRepository monedaRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<MonedaEntity> monedaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MonedaEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            MonedaEntity entity = factory.manufacturePojo(MonedaEntity.class);
            entityManager.persist(entity);
            monedaList.add(entity);
        }
    }

    @Test
    void createMoneda_valida_ok() {
        MonedaEntity nueva = new MonedaEntity();
        nueva.setCodigo("USD");
        nueva.setSimbolo("$");

        MonedaEntity result = monedaService.createMoneda(nueva);

        assertNotNull(result);
        assertEquals("USD", result.getCodigo());
    }

    @Test
    void createMoneda_codigoVacio_error() {
        MonedaEntity nueva = new MonedaEntity();
        nueva.setCodigo("");
        nueva.setSimbolo("$");

        assertThrows(IllegalArgumentException.class, () -> monedaService.createMoneda(nueva));
    }

    @Test
    void updateMoneda_valida_ok() {
        MonedaEntity original = monedaList.get(0);
        MonedaEntity actualizada = monedaService.updateMoneda(original.getId(), "EUR", "€");

        assertEquals("EUR", actualizada.getCodigo());
    }

    @Test
    void updateMoneda_codigoDuplicado_error() {
        MonedaEntity original = monedaList.get(0);
        MonedaEntity otra = monedaList.get(1);

        assertThrows(IllegalArgumentException.class, () ->
                monedaService.updateMoneda(original.getId(), otra.getCodigo(), "₱"));
    }
}
