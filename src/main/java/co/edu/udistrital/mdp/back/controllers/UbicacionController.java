package co.edu.udistrital.mdp.back.controllers;


import co.edu.udistrital.mdp.back.dto.UbicacionDTO;
import co.edu.udistrital.mdp.back.entities.UbicacionEntity;
import co.edu.udistrital.mdp.back.services.UbicacionService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con las ubicaciones.
 * Permite crear, consultar, actualizar y eliminar ubicaciones.
 * 
 * Ruta base: /ubicaciones
 */
@RestController
@RequestMapping("/ubicaciones")
public class UbicacionController {

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obtiene todas las ubicaciones registradas.
     * 
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<UbicacionDTO> findAll() {
        List<UbicacionEntity> ubicaciones = ubicacionService.getAll();
        return modelMapper.map(ubicaciones, new TypeToken<List<UbicacionDTO>>() {}.getType());
    }

    /**
     * Obtiene una ubicación por su ID.
     * 
  
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UbicacionDTO findOne(@PathVariable Long id) {
        UbicacionEntity ubicacion = ubicacionService.getById(id);
        return modelMapper.map(ubicacion, UbicacionDTO.class);
    }

    /**
     * Crea una nueva ubicación.
     * 
 
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UbicacionDTO create(@RequestBody UbicacionDTO ubicacionDTO) {
        UbicacionEntity ubicacionEntity = modelMapper.map(ubicacionDTO, UbicacionEntity.class);
        UbicacionEntity creada = ubicacionService.create(ubicacionEntity);
        return modelMapper.map(creada, UbicacionDTO.class);
    }

    /**
     * Actualiza una ubicación existente.
     * 
  
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UbicacionDTO update(@PathVariable Long id, @RequestBody UbicacionDTO ubicacionDTO) {
        UbicacionEntity ubicacionEntity = modelMapper.map(ubicacionDTO, UbicacionEntity.class);
        UbicacionEntity actualizada = ubicacionService.update(id, ubicacionEntity);
        return modelMapper.map(actualizada, UbicacionDTO.class);
    }

    /**
     * Elimina una ubicación por su ID.
     * 
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ubicacionService.delete(id);
    }

    /**
     * Busca ubicaciones por ciudad.
     * 
     */
    @GetMapping("/ciudad/{ciudad}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<UbicacionDTO> findByCiudad(@PathVariable String ciudad) {
        List<UbicacionEntity> ubicaciones = ubicacionService.findByCiudad(ciudad);
        return modelMapper.map(ubicaciones, new TypeToken<List<UbicacionDTO>>() {}.getType());
    }

    /**
     * Busca ubicaciones por país.
     * 
     */
    @GetMapping("/pais/{pais}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<UbicacionDTO> findByPais(@PathVariable String pais) {
        List<UbicacionEntity> ubicaciones = ubicacionService.findByPais(pais);
        return modelMapper.map(ubicaciones, new TypeToken<List<UbicacionDTO>>() {}.getType());
    }

    /**
     * Busca ubicaciones por dirección exacta.
     * 

     */
    @GetMapping("/direccion/{direccion}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<UbicacionDTO> findByDireccion(@PathVariable String direccion) {
        List<UbicacionEntity> ubicaciones = ubicacionService.findByDireccion(direccion);
        return modelMapper.map(ubicaciones, new TypeToken<List<UbicacionDTO>>() {}.getType());
    }

    /**
     * Busca ubicaciones asociadas a una tienda por su ID.
     * 

     */
    @GetMapping("/tienda/{tiendaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<UbicacionDTO> findByTiendaId(@PathVariable Long tiendaId) {
        List<UbicacionEntity> ubicaciones = ubicacionService.findByTiendaId(tiendaId);
        return modelMapper.map(ubicaciones, new TypeToken<List<UbicacionDTO>>() {}.getType());
    }
}
