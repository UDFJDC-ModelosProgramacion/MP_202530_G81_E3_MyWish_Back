package co.edu.udistrital.mdp.back.controllers;


import co.edu.udistrital.mdp.back.dto.CatalogoTiendasDTO;
import co.edu.udistrital.mdp.back.dto.CatalogoTiendasDetailDTO;
import co.edu.udistrital.mdp.back.entities.CatalogoTiendasEntity;
import co.edu.udistrital.mdp.back.services.CatalogoTiendasService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar los catálogos de tiendas.
 * Proporciona endpoints para crear, consultar, actualizar y eliminar catálogos.
 */
@Slf4j
@RestController
@RequestMapping("/catalogosTiendas")
public class CatalogoTiendasController {

    @Autowired
    private CatalogoTiendasService catalogoTiendasService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obtiene todos los catálogos de tiendas.
     *
     * @return lista de catálogos en formato DTO.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CatalogoTiendasDetailDTO> findAll() {
        log.info("Solicitud GET para obtener todos los catálogos de tiendas.");
        List<CatalogoTiendasEntity> catalogos = catalogoTiendasService
                .getAll(); // <- Si no existe, deberás implementarlo en el service
        return modelMapper.map(catalogos, new TypeToken<List<CatalogoTiendasDetailDTO>>() {}.getType());
    }

    /**
     * Obtiene un catálogo específico por su ID.
     *
     * @param id identificador del catálogo.
     * @return catálogo encontrado en formato DTO.
     * @throws EntityNotFoundException si no existe el catálogo.
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CatalogoTiendasDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        log.info("Solicitud GET para obtener catálogo con id {}", id);
        CatalogoTiendasEntity catalogo = catalogoTiendasService
                .getById(id); // <- Deberás implementarlo si no existe
        return modelMapper.map(catalogo, CatalogoTiendasDetailDTO.class);
    }

    /**
     * Crea un nuevo catálogo de tiendas.
     *
     * @param catalogoDTO objeto con los datos del catálogo.
     * @return catálogo creado en formato DTO.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CatalogoTiendasDTO create(@RequestBody CatalogoTiendasDTO catalogoDTO) {
        log.info("Solicitud POST para crear un nuevo catálogo de tiendas.");
        CatalogoTiendasEntity entity = catalogoTiendasService.crearCatalogo(
                modelMapper.map(catalogoDTO, CatalogoTiendasEntity.class)
        );
        return modelMapper.map(entity, CatalogoTiendasDTO.class);
    }

    /**
     * Actualiza la información de un catálogo existente.
     *
     * @param id               identificador del catálogo.
     * @param catalogoDetailDTO datos nuevos del catálogo.
     * @return catálogo actualizado en formato DTO.
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CatalogoTiendasDTO update(@PathVariable Long id,
                                     @RequestBody CatalogoTiendasDetailDTO catalogoDetailDTO) {
        log.info("Solicitud PUT para actualizar catálogo con id {}", id);

        CatalogoTiendasEntity actualizado = catalogoTiendasService.actualizarCatalogo(
                id,
                catalogoDetailDTO.getNombre(),
                catalogoDetailDTO.getDescripcion(),
                modelMapper.map(catalogoDetailDTO.getTiendas(), new TypeToken<List<co.edu.udistrital.mdp.back.entities.TiendaEntity>>() {}.getType())
        );

        return modelMapper.map(actualizado, CatalogoTiendasDTO.class);
    }

    /**
     * Elimina un catálogo por su ID.
     *
     * @param id identificador del catálogo a eliminar.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Solicitud DELETE para eliminar catálogo con id {}", id);
        catalogoTiendasService.eliminarCatalogo(id);
    }

    /**
     * Elimina una tienda específica de un catálogo.
     *
     * @param catalogoId identificador del catálogo.
     * @param tiendaId   identificador de la tienda.
     */
    @DeleteMapping(value = "/{catalogoId}/tiendas/{tiendaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void eliminarTiendaDeCatalogo(@PathVariable Long catalogoId, @PathVariable Long tiendaId) {
        log.info("Solicitud DELETE para eliminar tienda {} del catálogo {}", tiendaId, catalogoId);
        catalogoTiendasService.eliminarTiendaDeCatalogo(catalogoId, tiendaId);
    }
}
