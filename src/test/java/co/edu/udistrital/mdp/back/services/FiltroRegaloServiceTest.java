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

        // ⚠️ Crear filtros controlando el criterio para probar duplicados
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

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> filtroRegaloService.crearFiltro(lista.getId(), filtroExistente.getCriterio(), filtroExistente.getValor()));

        assertEquals("Ya existe un filtro con el mismo criterio y valor en esta lista de regalos.", ex.getMessage());
    }

    @Test
    void testCrearFiltro_CriterioOValorNulos() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> filtroRegaloService.crearFiltro(lista.getId(), null, "Rojo"));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> filtroRegaloService.crearFiltro(lista.getId(), "Color", ""));

        assertEquals("El criterio y el valor no pueden ser nulos ni vacíos.", ex1.getMessage());
        assertEquals("El criterio y el valor no pueden ser nulos ni vacíos.", ex2.getMessage());
    }

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

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> filtroRegaloService.actualizarFiltro(filtro.getId(), ""));

        assertEquals("El valor del filtro no puede estar vacío.", ex.getMessage());
    }

    @Test
    void testActualizarFiltro_Duplicado() {
        // Ambos filtros tienen el mismo criterio ("Categoria") pero distinto valor ("Valor0" y "Valor1")
        FiltroRegaloEntity filtro1 = filtrosList.get(0);
        FiltroRegaloEntity filtro2 = filtrosList.get(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> filtroRegaloService.actualizarFiltro(filtro1.getId(), filtro2.getValor()));

        assertEquals("No se puede actualizar el filtro: duplicaría criterio y valor en la misma lista.", ex.getMessage());
    }

    @Test
    void testEliminarFiltro_Success() {
        FiltroRegaloEntity filtro = filtrosList.get(0);

        filtroRegaloService.eliminarFiltro(filtro.getId());

        FiltroRegaloEntity eliminado = entityManager.find(FiltroRegaloEntity.class, filtro.getId());
        assertNull(eliminado);
    }

    @Test
    void testEliminarFiltro_SinLista() {
        FiltroRegaloEntity filtroSinLista = factory.manufacturePojo(FiltroRegaloEntity.class);
        filtroSinLista.setListaRegalos(null);
        entityManager.persist(filtroSinLista);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> filtroRegaloService.eliminarFiltro(filtroSinLista.getId()));

        assertEquals("El filtro no pertenece a una lista de regalos existente.", ex.getMessage());
    }
}
