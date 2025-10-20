package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.FiltroRegaloEntity;
import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.repositories.FiltroRegaloRepository;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(FiltroRegaloService.class)
class FiltroRegaloServiceTest {

    @Autowired
    private FiltroRegaloService filtroRegaloService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ListaRegalosRepository listaRegalosRepository;

    @Autowired
    private FiltroRegaloRepository filtroRegaloRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private ListaRegalosEntity lista;
    private List<FiltroRegaloEntity> filtrosList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from FiltroRegaloEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ListaRegalosEntity").executeUpdate();
    }

    private void insertData() {
        // Crear lista de regalos
        lista = factory.manufacturePojo(ListaRegalosEntity.class);
        lista.setId(null);
        lista.setFiltrosRegalos(new ArrayList<>());
        entityManager.persist(lista);

        // Crear filtros controlando el criterio para probar duplicados
        for (int i = 0; i < 3; i++) {
            FiltroRegaloEntity filtro = new FiltroRegaloEntity();
            filtro.setListaRegalos(lista);
            filtro.setCriterio("Categoria"); // mismo criterio en todos
            filtro.setValor("Valor" + i);   // valores diferentes
            entityManager.persist(filtro);
            filtrosList.add(filtro);
            lista.getFiltrosRegalos().add(filtro);
        }
    }

    // ----------------- Tests de creación -----------------
    @Test
    void testCrearFiltro_Success() {
        FiltroRegaloEntity nuevoFiltro = filtroRegaloService.crearFiltro(
                lista.getId(),
                "Color",
                "Rojo"
        );

        assertNotNull(nuevoFiltro);
        assertEquals("Color", nuevoFiltro.getCriterio());
        assertEquals("Rojo", nuevoFiltro.getValor());

        FiltroRegaloEntity filtroEnBD = entityManager.find(FiltroRegaloEntity.class, nuevoFiltro.getId());
        assertNotNull(filtroEnBD);
    }

    @Test
    void testCrearFiltro_Duplicado() {
        FiltroRegaloEntity filtroExistente = filtrosList.get(0);
        Long listaId = lista.getId();
        String criterio = filtroExistente.getCriterio();
        String valor = filtroExistente.getValor();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> filtroRegaloService.crearFiltro(listaId, criterio, valor)
        );

        assertEquals("Ya existe un filtro con el mismo criterio y valor en esta lista de regalos.", ex.getMessage());
    }

    @Test
    void testCrearFiltro_CriterioOValorNulos() {
        Long listaId = lista.getId();

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
            () -> filtroRegaloService.crearFiltro(listaId, null, "Rojo")
        );

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
            () -> filtroRegaloService.crearFiltro(listaId, "Color", "")
        );

        assertEquals("El criterio y el valor no pueden ser nulos ni vacíos.", ex1.getMessage());
        assertEquals("El criterio y el valor no pueden ser nulos ni vacíos.", ex2.getMessage());
    }

    // ----------------- Tests de actualización -----------------
    @Test
    void testActualizarFiltro_Success() {
        FiltroRegaloEntity filtro = filtrosList.get(0);
        FiltroRegaloEntity actualizado = filtroRegaloService.actualizarFiltro(filtro.getId(), "NuevoValor");

        assertNotNull(actualizado);
        assertEquals("NuevoValor", actualizado.getValor());
    }

    @Test
    void testActualizarFiltro_ValorVacio() {
        FiltroRegaloEntity filtro = filtrosList.get(0);
        Long filtroId = filtro.getId();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> filtroRegaloService.actualizarFiltro(filtroId, "")
        );

        assertEquals("El valor del filtro no puede estar vacío.", ex.getMessage());
    }

    @Test
    void testActualizarFiltro_Duplicado() {
        FiltroRegaloEntity filtro1 = filtrosList.get(0);
        FiltroRegaloEntity filtro2 = filtrosList.get(1);
        Long filtro1Id = filtro1.getId();
        String valorDuplicado = filtro2.getValor();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> filtroRegaloService.actualizarFiltro(filtro1Id, valorDuplicado)
        );

        assertEquals("No se puede actualizar el filtro: duplicaría criterio y valor en la misma lista.", ex.getMessage());
    }

    // ----------------- Tests de eliminación -----------------
    @Test
    void testEliminarFiltro_Success() {
        FiltroRegaloEntity filtro = filtrosList.get(0);
        Long filtroId = filtro.getId();

        filtroRegaloService.eliminarFiltro(filtroId);

        FiltroRegaloEntity eliminado = entityManager.find(FiltroRegaloEntity.class, filtroId);
        assertNull(eliminado);
    }

    @Test
    void testEliminarFiltro_SinLista() {
        FiltroRegaloEntity filtroSinLista = factory.manufacturePojo(FiltroRegaloEntity.class);
        filtroSinLista.setListaRegalos(null);
        entityManager.persist(filtroSinLista);
        Long filtroId = filtroSinLista.getId();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> filtroRegaloService.eliminarFiltro(filtroId)
        );

        assertEquals("El filtro no pertenece a una lista de regalos existente.", ex.getMessage());
    }
}
