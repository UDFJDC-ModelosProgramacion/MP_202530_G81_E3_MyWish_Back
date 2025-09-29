package co.edu.udistrital.mdp.back.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.back.entities.CelebracionEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;

/**
 * Repositorio para la entidad Celebracion.
 * Permite realizar operaciones CRUD y consultas personalizadas
 * sobre las celebraciones en la base de datos.
 */
@Repository
public interface CelebracionRepository extends JpaRepository<CelebracionEntity, Long> {

    // Buscar celebraciones por nombre
    List<CelebracionEntity> findByNombre(String nombre);

    // Buscar celebraciones por lugar
    List<CelebracionEntity> findByLugar(String lugar);

    // Buscar celebraciones por fecha
    List<CelebracionEntity> findByFecha(Date fecha);

    // Buscar celebraciones por organizador (antes era CreadorEntity)
    List<CelebracionEntity> findByOrganizador(UsuarioEntity organizador);

    // Buscar celebraciones por nombre y lugar
    List<CelebracionEntity> findByNombreAndLugar(String nombre, String lugar);

    // Buscar celebraciones por rango de fechas
    List<CelebracionEntity> findByFechaBetween(Date fechaInicio, Date fechaFin);

    // Buscar celebraciones por cantidad de invitados mayor a
    List<CelebracionEntity> findByCantidadInvitadosGreaterThan(int cantidad);
}
