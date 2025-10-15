package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.FotoEntity;
import co.edu.udistrital.mdp.back.repositories.FotoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FotoService {

    @Autowired
    private FotoRepository fotoRepository;

    // =====================================================
    // CREATE
    // =====================================================

    @Transactional
    public FotoEntity createFoto(FotoEntity fotoEntity) {

        log.info("Inicia proceso de creación de la foto");

        // Regla 1: La URL de la foto debe ser una dirección válida (http/https)
        if (fotoEntity.getUrl() == null || 
            (!fotoEntity.getUrl().startsWith("http://") && !fotoEntity.getUrl().startsWith("https://"))) {
            throw new IllegalArgumentException("La URL de la foto debe ser una dirección válida (http/https).");
        }

        // Regla 2: El tamaño del archivo no puede exceder los 10MB
        if (fotoEntity.getTamanioBytes() != null && fotoEntity.getTamanioBytes() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("El tamaño del archivo no puede exceder los 10MB.");
        }

        // Regla 3: Solo se permiten formatos de imagen: JPG, PNG, GIF, WEBP
        if (fotoEntity.getTipoArchivo() != null) {
            String tipoArchivo = fotoEntity.getTipoArchivo().toLowerCase();
            if (!tipoArchivo.equals("image/jpeg") && 
                !tipoArchivo.equals("image/png") && 
                !tipoArchivo.equals("image/gif") && 
                !tipoArchivo.equals("image/webp")) {
                throw new IllegalArgumentException("Solo se permiten formatos de imagen: JPG, PNG, GIF, WEBP.");
            }
        }

        // Regla 4: Una foto debe estar asociada a una y solo una entidad
        int entidadesAsociadas = 0;
        if (fotoEntity.getRegalo() != null) entidadesAsociadas++;
        if (fotoEntity.getListaRegalos() != null) entidadesAsociadas++;
        if (fotoEntity.getTienda() != null) entidadesAsociadas++;
        if (fotoEntity.getComentario() != null) entidadesAsociadas++;
        
        if (entidadesAsociadas != 1) {
            throw new IllegalArgumentException("Una foto debe estar asociada a una y solo una entidad.");
        }

        log.info("Termina proceso de creación de la foto");
        return fotoRepository.save(fotoEntity);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public FotoEntity updateFoto(Long fotoId, FotoEntity fotoEntity) {

        log.info("Inicia proceso de actualización de la foto con id: {}", fotoId);

        Optional<FotoEntity> fotoOpt = fotoRepository.findById(fotoId);
        if (fotoOpt.isEmpty()) {
            throw new EntityNotFoundException("La foto con id " + fotoId + " no existe.");
        }

        FotoEntity existente = fotoOpt.get();

        // Regla 5: No se puede cambiar la entidad asociada una vez creada la foto
        if ((fotoEntity.getRegalo() != null && !fotoEntity.getRegalo().equals(existente.getRegalo())) ||
            (fotoEntity.getListaRegalos() != null && !fotoEntity.getListaRegalos().equals(existente.getListaRegalos())) ||
            (fotoEntity.getTienda() != null && !fotoEntity.getTienda().equals(existente.getTienda())) ||
            (fotoEntity.getComentario() != null && !fotoEntity.getComentario().equals(existente.getComentario()))) {
            throw new IllegalStateException("No se puede cambiar la entidad asociada una vez creada la foto.");
        }

        // Regla 7: Solo puede haber una foto marcada como principal por entidad asociada
        if (fotoEntity.getEsPrincipal() != null && fotoEntity.getEsPrincipal()) {
            // Desmarcar otras fotos principales de la misma entidad
            desmarcarOtrasFotosPrincipales(existente);
        }

        // Actualización de datos válidos
        if (fotoEntity.getDescripcion() != null) {
            existente.setDescripcion(fotoEntity.getDescripcion());
        }
        if (fotoEntity.getEsPrincipal() != null) {
            existente.setEsPrincipal(fotoEntity.getEsPrincipal());
        }

        log.info("Termina proceso de actualización de la foto con id: {}", fotoId);
        return fotoRepository.save(existente);
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Transactional
    public void deleteFoto(Long fotoId) {

        log.info("Inicia proceso de eliminación de la foto con id: {}", fotoId);

        Optional<FotoEntity> fotoOpt = fotoRepository.findById(fotoId);
        if (fotoOpt.isEmpty()) {
            throw new EntityNotFoundException("La foto con id " + fotoId + " no existe.");
        }

        FotoEntity foto = fotoOpt.get();

        // Regla 6: Al eliminar una foto se debe eliminar el archivo físico del servidor
        eliminarArchivoFisico(foto.getUrl());

        fotoRepository.delete(foto);

        log.info("Termina proceso de eliminación de la foto con id: {}", fotoId);
    }

    // =====================================================
    // GET
    // =====================================================

    @Transactional(readOnly = true)
    public List<FotoEntity> getAllFotos() {
        log.info("Inicia proceso de consulta de todas las fotos");
        return fotoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public FotoEntity getFotoById(Long fotoId) {
        log.info("Inicia proceso de consulta de la foto con id: {}", fotoId);
        return fotoRepository.findById(fotoId)
                .orElseThrow(() -> new EntityNotFoundException("La foto con id " + fotoId + " no existe."));
    }

    @Transactional(readOnly = true)
    public List<FotoEntity> getFotosByRegaloId(Long regaloId) {
        log.info("Inicia proceso de consulta de fotos por regalo id: {}", regaloId);
        return fotoRepository.findByRegaloId(regaloId);
    }

    @Transactional(readOnly = true)
    public List<FotoEntity> getFotosPrincipales() {
        log.info("Inicia proceso de consulta de fotos principales");
        return fotoRepository.findByEsPrincipalTrue();
    }

    // =====================================================
    // MÉTODOS PRIVADOS
    // =====================================================
    private void desmarcarOtrasFotosPrincipales(FotoEntity fotoPrincipal) {
        List<FotoEntity> fotosPrincipales;
        
        if (fotoPrincipal.getRegalo() != null) {
            fotosPrincipales = fotoRepository.findByRegaloId(fotoPrincipal.getRegalo().getId());
        } else if (fotoPrincipal.getListaRegalos() != null) {
            fotosPrincipales = fotoRepository.findByListaRegalosId(fotoPrincipal.getListaRegalos().getId());
        } else if (fotoPrincipal.getTienda() != null) {
            // Implementar método similar para tiendas si es necesario
            fotosPrincipales = List.of();
        } else if (fotoPrincipal.getComentario() != null) {
            // Implementar método similar para comentarios si es necesario
            fotosPrincipales = List.of();
        } else {
            fotosPrincipales = List.of();
        }
        
        for (FotoEntity foto : fotosPrincipales) {
            if (!foto.getId().equals(fotoPrincipal.getId()) && foto.getEsPrincipal()) {
                foto.setEsPrincipal(false);
                fotoRepository.save(foto);
            }
        }
    }
    

    private void eliminarArchivoFisico(String url) {
        // Implementación para eliminar el archivo físico del servidor
        // Esto dependerá de tu implementación específica de almacenamiento
        log.info("Eliminando archivo físico: {}", url);
        // Código para eliminar el archivo del sistema de archivos o servicio cloud
    }

}