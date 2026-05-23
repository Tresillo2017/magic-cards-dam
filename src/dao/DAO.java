package dao;

import java.util.List;

/**
 * Interfaz genérica que define las operaciones CRUD básicas para cualquier entidad.
 * Todos los DAOs del sistema implementan esta interfaz con su tipo concreto.
 *
 * @param <T> tipo de la entidad gestionada por el DAO
 */
public interface DAO<T> {

    /**
     * Inserta una nueva entidad en la base de datos.
     * @param entidad objeto a insertar
     * @return true si la operación fue correcta, false si hubo error
     */
    boolean insertar(T entidad);

    /**
     * Obtiene una entidad por su identificador.
     * @param id identificador único
     * @return la entidad encontrada, o null si no existe
     */
    T obtenerPorId(int id);

    /**
     * Devuelve todas las entidades almacenadas.
     * @return lista de entidades
     */
    List<T> listarTodos();

    /**
     * Actualiza los datos de una entidad existente.
     * @param entidad objeto con los nuevos datos
     * @return true si la operación fue correcta, false si hubo error
     */
    boolean actualizar(T entidad);

    /**
     * Elimina la entidad con el identificador indicado.
     * @param id identificador único
     * @return true si la operación fue correcta, false si hubo error
     */
    boolean eliminar(int id);
}
