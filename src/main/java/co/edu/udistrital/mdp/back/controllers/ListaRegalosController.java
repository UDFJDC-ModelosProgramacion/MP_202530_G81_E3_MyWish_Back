package co.edu.udistrital.mdp.back.controllers;


import co.edu.udistrital.mdp.back.dto.ListaRegalosDTO;
import co.edu.udistrital.mdp.back.dto.ListaRegalosDetailDTO;
import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;
import co.edu.udistrital.mdp.back.services.ListaRegalosService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listas-regalos")
public class ListaRegalosController {

    @Autowired
    private ListaRegalosService listaRegalosService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obtener todas las listas de regalos
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ListaRegalosDetailDTO> findAll() {
        List<ListaRegalosEntity> listas = listaRegalosService.getAllListas();
        return modelMapper.map(listas, new TypeToken<List<ListaRegalosDetailDTO>>() {}.getType());
    }

    /**
     * Obtener una lista de regalos por id
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ListaRegalosDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        ListaRegalosEntity lista = listaRegalosService.getListaById(id);
        return modelMapper.map(lista, ListaRegalosDetailDTO.class);
    }

    /**
     * Crear una nueva lista de regalos
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ListaRegalosDTO create(@RequestBody ListaRegalosDTO listaDTO)
            throws IllegalArgumentException, EntityNotFoundException {
        ListaRegalosEntity listaEntity =
                listaRegalosService.createListaRegalos(modelMapper.map(listaDTO, ListaRegalosEntity.class));
        return modelMapper.map(listaEntity, ListaRegalosDTO.class);
    }

    /**
     * Actualizar una lista de regalos existente
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ListaRegalosDTO update(@PathVariable Long id, @RequestBody ListaRegalosDTO listaDTO)
            throws EntityNotFoundException, IllegalArgumentException {
        ListaRegalosEntity listaEntity =
                listaRegalosService.updateListaRegalos(id, modelMapper.map(listaDTO, ListaRegalosEntity.class));
        return modelMapper.map(listaEntity, ListaRegalosDTO.class);
    }

    /**
     * Eliminar una lista de regalos por id
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        listaRegalosService.deleteListaRegalos(id);
    }

    /**
     * Consultar todas las listas de regalos creadas por un usuario específico
     */
    @GetMapping(value = "/creador/{creadorId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ListaRegalosDetailDTO> findByCreador(@PathVariable Long creadorId) {
        List<ListaRegalosEntity> listas = listaRegalosService.getListasByCreador(creadorId);
        return modelMapper.map(listas, new TypeToken<List<ListaRegalosDetailDTO>>() {}.getType());
    }
}
