package co.edu.udistrital.mdp.back.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.back.entities.ListaRegalosEntity;

/**
 * Repositorio para la entidad ListaRegalos.
 * Permite realizar operaciones CRUD y consultas personalizadas
 * sobre las listas de regalos en la base de datos.
 */
@Repository
public interface ListaRegalosRepository extends JpaRepository<ListaRegalosEntity, Long> {

    // Buscar listas por nombre
    List<ListaRegalosEntity> findByNombre(String nombre);

    // Buscar listas por color
    List<ListaRegalosEntity> findByColor(String color);

    // Buscar listas por nombre y color
    List<ListaRegalosEntity> findByNombreAndColor(String nombre, String color);
}