package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.edu.udistrital.mdp.back.entities.FiltroRegaloEntity;
import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.repositories.FiltroRegaloRepository;
import co.edu.udistrital.mdp.back.repositories.ListaRegalosRepository;


public class FiltroRegaloServiceTest {

    @Mock
    private FiltroRegaloRepository filtroRegaloRepository;

    @Mock
    private ListaRegalosRepository listaRegalosRepository;

    @InjectMocks
    private FiltroRegaloService filtroRegaloService;

    private ListaRegalosEntity lista;
    private FiltroRegaloEntity filtro;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        lista = new ListaRegalosEntity();
        lista.setId(1L);
        lista.setFiltrosRegalos(new ArrayList<>());

        filtro = new FiltroRegaloEntity();
        filtro.setId(100L);
        filtro.setCriterio("Categoria");
        filtro.setValor("Deporte");
        filtro.setListaRegalos(lista);
    }

    @Test
    public void testCrearFiltro_Success() {
        when(listaRegalosRepository.findById(1L)).thenReturn(Optional.of(lista));

        FiltroRegaloEntity nuevoFiltro = new FiltroRegaloEntity();
        nuevoFiltro.setId(101L);
        nuevoFiltro.setCriterio("Color");
        nuevoFiltro.setValor("Rojo");
        nuevoFiltro.setListaRegalos(lista);

        when(filtroRegaloRepository.save(any(FiltroRegaloEntity.class))).thenReturn(nuevoFiltro);

        FiltroRegaloEntity resultado = filtroRegaloService.crearFiltro(1L, "Color", "Rojo");

        assertEquals("Color", resultado.getCriterio());
        assertEquals("Rojo", resultado.getValor());
        verify(filtroRegaloRepository, times(1)).save(any(FiltroRegaloEntity.class));
    }

    @Test
    public void testCrearFiltro_Duplicado() {
        lista.getFiltrosRegalos().add(filtro);
        when(listaRegalosRepository.findById(1L)).thenReturn(Optional.of(lista));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> filtroRegaloService.crearFiltro(1L, "Categoria", "Deporte"));
        assertEquals("Ya existe un filtro con el mismo criterio y valor en esta lista de regalos.", ex.getMessage());
    }

    @Test
    public void testCrearFiltro_CriterioOValorNulos() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> filtroRegaloService.crearFiltro(1L, null, "Rojo"));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> filtroRegaloService.crearFiltro(1L, "Color", ""));

        assertEquals("El criterio y el valor no pueden ser nulos ni vacíos.", ex1.getMessage());
        assertEquals("El criterio y el valor no pueden ser nulos ni vacíos.", ex2.getMessage());
    }

    @Test
    public void testActualizarFiltro_Success() {
        lista.getFiltrosRegalos().add(filtro);
        when(filtroRegaloRepository.findById(100L)).thenReturn(Optional.of(filtro));
        when(filtroRegaloRepository.save(filtro)).thenReturn(filtro);

        FiltroRegaloEntity resultado = filtroRegaloService.actualizarFiltro(100L, "Musica");

        assertEquals("Musica", resultado.getValor());
    }

    @Test
    public void testActualizarFiltro_ValorVacio() {
        when(filtroRegaloRepository.findById(100L)).thenReturn(Optional.of(filtro));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> filtroRegaloService.actualizarFiltro(100L, ""));
        assertEquals("El valor del filtro no puede estar vacío.", ex.getMessage());
    }

    @Test
    public void testActualizarFiltro_Duplicado() {
        FiltroRegaloEntity otroFiltro = new FiltroRegaloEntity();
        otroFiltro.setId(101L);
        otroFiltro.setCriterio("Categoria");
        otroFiltro.setValor("Musica");

        lista.getFiltrosRegalos().add(filtro);
        lista.getFiltrosRegalos().add(otroFiltro);

        when(filtroRegaloRepository.findById(100L)).thenReturn(Optional.of(filtro));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> filtroRegaloService.actualizarFiltro(100L, "Musica"));
        assertEquals("No se puede actualizar el filtro: duplicaría criterio y valor en la misma lista.", ex.getMessage());
    }

    @Test
    public void testEliminarFiltro_Success() {
        filtro.setListaRegalos(lista);

        when(filtroRegaloRepository.findById(100L)).thenReturn(Optional.of(filtro));

        filtroRegaloService.eliminarFiltro(100L);

        verify(filtroRegaloRepository, times(1)).delete(filtro);
    }

    @Test
    public void testEliminarFiltro_SinLista() {
        filtro.setListaRegalos(null);

        when(filtroRegaloRepository.findById(100L)).thenReturn(Optional.of(filtro));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> filtroRegaloService.eliminarFiltro(100L));
        assertEquals("El filtro no pertenece a una lista de regalos existente.", ex.getMessage());
    }
}
