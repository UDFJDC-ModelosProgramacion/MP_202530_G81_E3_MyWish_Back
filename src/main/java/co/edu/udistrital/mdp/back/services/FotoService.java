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

    // =====================================================
    // REGLAS DE VALIDACIÓN
    // =====================================================

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

    // =====================================================
    // VALIDACIONES DE RELACIONES
    // =====================================================

    // Regla 4: Una foto puede estar asociada a una entidad (Regalo, ListaRegalos, Tienda o Comentario),
    // pero no a más de una al mismo tiempo.
    int entidadesAsociadas = 0;
    if (fotoEntity.getRegalo() != null) entidadesAsociadas++;
    if (fotoEntity.getListaRegalos() != null) entidadesAsociadas++;
    if (fotoEntity.getTienda() != null) entidadesAsociadas++;
    if (fotoEntity.getComentario() != null) entidadesAsociadas++;

    if (entidadesAsociadas > 1) {
        throw new IllegalArgumentException("Una foto no puede estar asociada a más de una entidad al mismo tiempo.");
    }

    // Regla 5: Si la foto está asociada a un Regalo o una ListaRegalos (relación OneToOne),
    // verificar que esa entidad no tenga ya una foto asignada.
    if (fotoEntity.getRegalo() != null) {
        List<FotoEntity> fotosExistentes = fotoRepository.findByRegaloId(fotoEntity.getRegalo().getId());
        if (!fotosExistentes.isEmpty()) {
            throw new IllegalStateException("El regalo ya tiene una foto asignada.");
        }
    }

    if (fotoEntity.getListaRegalos() != null) {
        List<FotoEntity> fotosExistentes = fotoRepository.findByListaRegalosId(fotoEntity.getListaRegalos().getId());
        if (!fotosExistentes.isEmpty()) {
            throw new IllegalStateException("La lista de regalos ya tiene una foto asignada.");
        }
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
    public List<FotoEntity> getFotosByListaRegalosId(Long listaRegalosId) {
        log.info("Inicia proceso de consulta de fotos por lista de regalos id: {}", listaRegalosId);
        return fotoRepository.findByListaRegalosId(listaRegalosId);
    }

    @Transactional(readOnly = true)
    public List<FotoEntity> getFotosByTiendaId(Long tiendaId) {
        log.info("Inicia proceso de consulta de fotos por tienda id: {}", tiendaId);
        return fotoRepository.findByTiendaId(tiendaId);
    }

    @Transactional(readOnly = true)
    public List<FotoEntity> getFotosByComentarioId(Long comentarioId) {
        log.info("Inicia proceso de consulta de fotos por comentario id: {}", comentarioId);
        return fotoRepository.findByComentarioId(comentarioId);
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
        // Para relaciones OneToOne, solo debería haber una foto por entidad
        // Pero manejamos el caso de múltiples fotos para consistencia
        
        if (fotoPrincipal.getRegalo() != null) {
            List<FotoEntity> fotosRegalo = fotoRepository.findByRegaloId(fotoPrincipal.getRegalo().getId());
            desmarcarOtrasEnLista(fotosRegalo, fotoPrincipal);
        } else if (fotoPrincipal.getListaRegalos() != null) {
            List<FotoEntity> fotosLista = fotoRepository.findByListaRegalosId(fotoPrincipal.getListaRegalos().getId());
            desmarcarOtrasEnLista(fotosLista, fotoPrincipal);
        }
        // Para Tienda y Comentario (ManyToOne) pueden tener múltiples fotos
        else if (fotoPrincipal.getTienda() != null) {
            List<FotoEntity> fotosTienda = fotoRepository.findByTiendaId(fotoPrincipal.getTienda().getId());
            desmarcarOtrasEnLista(fotosTienda, fotoPrincipal);
        } else if (fotoPrincipal.getComentario() != null) {
            List<FotoEntity> fotosComentario = fotoRepository.findByComentarioId(fotoPrincipal.getComentario().getId());
            desmarcarOtrasEnLista(fotosComentario, fotoPrincipal);
        }
    }

    private void desmarcarOtrasEnLista(List<FotoEntity> fotos, FotoEntity fotoPrincipal) {
        for (FotoEntity foto : fotos) {
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