package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.entities.UbicacionEntity;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;
import co.edu.udistrital.mdp.back.repositories.UbicacionRepository;
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
@Import(UbicacionService.class)
class UbicacionServiceTest { 

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<UbicacionEntity> ubicaciones = new ArrayList<>();
    private List<TiendaEntity> tiendas = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from UbicacionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TiendaEntity").executeUpdate();
    }

    private void insertData() {
        // Crear tiendas
        for (int i = 0; i < 2; i++) {
            TiendaEntity tienda = factory.manufacturePojo(TiendaEntity.class);
            entityManager.persist(tienda);
            tiendas.add(tienda);
        }

        // Crear ubicaciones
        for (int i = 0; i < 3; i++) {
            UbicacionEntity ubicacion = factory.manufacturePojo(UbicacionEntity.class);
            ubicacion.setTienda(tiendas.get(0));
            entityManager.persist(ubicacion);
            ubicaciones.add(ubicacion);
        }
    }

    @Test
    void testGetAll() {
        List<UbicacionEntity> list = ubicacionService.getAll();
        assertEquals(3, list.size());
    }

    @Test
    void testGetByIdSuccess() {
        UbicacionEntity entity = ubicaciones.get(0);
        UbicacionEntity found = ubicacionService.getById(entity.getId());
        assertNotNull(found);
        assertEquals(entity.getCiudad(), found.getCiudad());
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> ubicacionService.getById(999L));
    }

    @Test
    void testCreateUbicacion() {
        UbicacionEntity nueva = factory.manufacturePojo(UbicacionEntity.class);
        nueva.setTienda(tiendas.get(1));
        UbicacionEntity creada = ubicacionService.create(nueva);
        assertNotNull(creada);
        assertEquals(nueva.getCiudad(), creada.getCiudad());
    }

    @Test
    void testUpdateUbicacion() {
        UbicacionEntity existente = ubicaciones.get(0);
        UbicacionEntity cambios = factory.manufacturePojo(UbicacionEntity.class);

        UbicacionEntity actualizada = ubicacionService.update(existente.getId(), cambios);
        assertEquals(cambios.getCiudad(), actualizada.getCiudad());
        assertEquals(cambios.getPais(), actualizada.getPais());
    }

    @Test
    void testDeleteUbicacion() {
        UbicacionEntity entity = ubicaciones.get(0);
        ubicacionService.delete(entity.getId());
        assertFalse(ubicacionRepository.findById(entity.getId()).isPresent());
    }

    @Test
    void testFindByCiudad() {
        UbicacionEntity u = ubicaciones.get(0);
        List<UbicacionEntity> result = ubicacionService.findByCiudad(u.getCiudad());
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByPais() {
        UbicacionEntity u = ubicaciones.get(0);
        List<UbicacionEntity> result = ubicacionService.findByPais(u.getPais());
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByDireccion() {
        UbicacionEntity u = ubicaciones.get(0);
        List<UbicacionEntity> result = ubicacionService.findByDireccion(u.getDireccion());
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByTienda() {
        TiendaEntity t = tiendas.get(0);
        List<UbicacionEntity> result = ubicacionService.findByTienda(t);
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByTiendaId() {
        TiendaEntity t = tiendas.get(0);
        List<UbicacionEntity> result = ubicacionService.findByTiendaId(t.getId());
        assertFalse(result.isEmpty());
    }
}
