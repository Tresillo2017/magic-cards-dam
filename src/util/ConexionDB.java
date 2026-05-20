package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/magic_cards";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "root";

    private static ConexionDB instancia;
    private Connection conexion;

    private ConexionDB() throws SQLException {
        conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    public static ConexionDB getInstancia() throws SQLException {
        if (instancia == null || instancia.conexion.isClosed()) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void cerrar() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
    }
}
