package co.edu.udistrital.mdp.back.controllers;


import co.edu.udistrital.mdp.back.dto.FiltroRegaloDTO;
import co.edu.udistrital.mdp.back.entities.FiltroRegaloEntity;
import co.edu.udistrital.mdp.back.services.FiltroRegaloService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de filtros de regalos.
 * Se encarga de manejar las peticiones HTTP relacionadas con los filtros
 * y delegarlas al servicio correspondiente.
 * 
 * @author 
 */
@RestController
@RequestMapping("/filtrosRegalos")
public class FiltroRegaloController {

    @Autowired
    private FiltroRegaloService filtroRegaloService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Crea un nuevo filtro de regalo para una lista de regalos específica.
     * 
     * @param listaId ID de la lista de regalos a la que pertenece el filtro.
     * @param filtroDTO Objeto que contiene los datos del nuevo filtro.
     * @return El filtro creado en formato DTO.
     * @throws IllegalArgumentException si los datos son inválidos o la lista no existe.
     */
    @PostMapping("/lista/{listaId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public FiltroRegaloDTO create(@PathVariable Long listaId, @RequestBody FiltroRegaloDTO filtroDTO) {
        FiltroRegaloEntity filtroEntity = filtroRegaloService.crearFiltro(
                listaId,
                filtroDTO.getCriterio(),
                filtroDTO.getValor()
        );
        return modelMapper.map(filtroEntity, FiltroRegaloDTO.class);
    }

    /**
     * Actualiza el valor de un filtro existente.
     * 
     * @param filtroId ID del filtro a actualizar.
     * @param nuevoFiltroDTO Objeto que contiene el nuevo valor del filtro.
     * @return El filtro actualizado en formato DTO.
     * @throws IllegalArgumentException si el filtro no existe o los datos son inválidos.
     */
    @PutMapping("/{filtroId}")
    @ResponseStatus(code = HttpStatus.OK)
    public FiltroRegaloDTO update(@PathVariable Long filtroId, @RequestBody FiltroRegaloDTO nuevoFiltroDTO) {
        FiltroRegaloEntity filtroEntity = filtroRegaloService.actualizarFiltro(
                filtroId,
                nuevoFiltroDTO.getValor()
        );
        return modelMapper.map(filtroEntity, FiltroRegaloDTO.class);
    }

    /**
     * Elimina un filtro de regalo por su ID.
     * 
     * @param filtroId ID del filtro a eliminar.
     * @throws IllegalArgumentException si el filtro no existe.
     */
    @DeleteMapping("/{filtroId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long filtroId) {
        filtroRegaloService.eliminarFiltro(filtroId);
    }

}
