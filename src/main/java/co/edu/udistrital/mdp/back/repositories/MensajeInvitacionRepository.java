package co.edu.udistrital.mdp.back.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.back.entities.MensajeInvitacionEntity;
import co.edu.udistrital.mdp.back.entities.CelebracionEntity;

//Repositorio para la entidad MensajeInvitacion.

@Repository
public interface MensajeInvitacionRepository extends JpaRepository<MensajeInvitacionEntity, Long> {

    // Buscar mensajes por celebración
    List<MensajeInvitacionEntity> findByCelebracion(CelebracionEntity celebracion);

    // Buscar mensajes por fecha de envío
    List<MensajeInvitacionEntity> findByFechaEnvio(Date fechaEnvio);

    // Buscar mensajes por rango de fechas de envío
    List<MensajeInvitacionEntity> findByFechaEnvioBetween(Date fechaInicio, Date fechaFin);

    // Buscar mensajes que contienen texto específico
    List<MensajeInvitacionEntity> findByMensajeContainingIgnoreCase(String texto);

    // Buscar mensajes por celebración ordenados por fecha de envío
    List<MensajeInvitacionEntity> findByCelebracionOrderByFechaEnvioDesc(CelebracionEntity celebracion);

    // Buscar mensajes enviados después de una fecha específica
    List<MensajeInvitacionEntity> findByFechaEnvioAfter(Date fecha);

    // Buscar mensajes enviados antes de una fecha específica
    List<MensajeInvitacionEntity> findByFechaEnvioBefore(Date fecha);

    // Contar mensajes por celebración
    long countByCelebracion(CelebracionEntity celebracion);

    // Buscar mensajes por el ID de la celebración
    List<MensajeInvitacionEntity> findByCelebracion_Id(Long celebracionId);

}
