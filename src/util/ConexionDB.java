package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class ConexionDB {

    private static final String DEFAULT_URL      = "jdbc:mysql://localhost:3306/magic_cards";
    private static final String DEFAULT_USUARIO  = "magic_user";
    private static final String DEFAULT_CONTRASENA = "EbHuYuWuA5EKvr21tEjuXZq";

    private static volatile ConexionDB instancia;
    private Connection conexion;

    private ConexionDB() throws SQLException {
        Properties props = cargarPropiedades();
        String url       = props.getProperty("db.url",      DEFAULT_URL);
        String usuario   = props.getProperty("db.usuario",  DEFAULT_USUARIO);
        String contrasena = props.getProperty("db.contrasena", DEFAULT_CONTRASENA);
        conexion = DriverManager.getConnection(url, usuario, contrasena);
    }

    private static Properties cargarPropiedades() {
        Properties props = new Properties();
        try (InputStream is = ConexionDB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (Exception ignored) {}
        // Variables de entorno tienen prioridad sobre el fichero
        if (System.getenv("DB_URL")       != null) props.setProperty("db.url",        System.getenv("DB_URL"));
        if (System.getenv("DB_USUARIO")   != null) props.setProperty("db.usuario",    System.getenv("DB_USUARIO"));
        if (System.getenv("DB_CONTRASENA")!= null) props.setProperty("db.contrasena", System.getenv("DB_CONTRASENA"));
        return props;
    }

    public static ConexionDB getInstancia() throws SQLException {
        if (instancia == null) {
            synchronized (ConexionDB.class) {
                if (instancia == null) {
                    instancia = new ConexionDB();
                }
            }
        } else if (instancia.conexion.isClosed()) {
            synchronized (ConexionDB.class) {
                if (instancia.conexion.isClosed()) {
                    instancia = new ConexionDB();
                }
            }
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
