package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión JDBC a la base de datos MySQL.
 * Implementa el patrón Singleton para reutilizar una única conexión.
 */
public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/magic_cards";
    private static final String USUARIO = "magic_user";
    private static final String CONTRASENA = "EbHuYuWuA5EKvr21tEjuXZq";

    private static ConexionDB instancia = null;
    private Connection conexion;
    private String errorConexion = null;

    private ConexionDB() {
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (SQLException e) {
            errorConexion = e.getMessage();
            e.printStackTrace();
        }
    }

    /**
     * Devuelve la única instancia de ConexionDB, creándola si no existe.
     * @return instancia única de ConexionDB
     */
    public static ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    /**
     * Indica si la conexión se estableció correctamente.
     * @return true si hay conexión activa
     */
    public boolean isConectado() {
        return conexion != null;
    }

    /**
     * Devuelve el mensaje de error de conexión, o null si no hubo error.
     * @return mensaje de error o null
     */
    public String getErrorConexion() {
        return errorConexion;
    }

    /**
     * Devuelve el objeto Connection activo.
     * @return conexión JDBC
     */
    public Connection getConexion() {
        return conexion;
    }

    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
