package dao;

import model.Jugador;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la entidad Jugador.
 * Proporciona operaciones CRUD sobre la tabla {@code jugador}.
 */
public class JugadorDAO implements DAO<Jugador> {

    private Connection conexion;

    public JugadorDAO() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    /** Inserta un jugador y actualiza su {@code idJugador} con la clave generada. */
    public boolean insertar(Jugador j) {
        String sql = "INSERT INTO jugador (nombre, email, fecha_registro) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, j.getNombre());
            ps.setString(2, j.getEmail());
            ps.setString(3, j.getFechaRegistro());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                j.setIdJugador(rs.getInt(1));
            }
            rs.close();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene un jugador por su ID.
     * @param id identificador del jugador
     * @return objeto Jugador o {@code null} si no existe
     */
    public Jugador obtenerPorId(int id) {
        Jugador jugador = null;
        String sql = "SELECT id_jugador, nombre, email, fecha_registro FROM jugador WHERE id_jugador = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jugador = new Jugador();
                jugador.setIdJugador(rs.getInt("id_jugador"));
                jugador.setNombre(rs.getString("nombre"));
                jugador.setEmail(rs.getString("email"));
                jugador.setFechaRegistro(rs.getString("fecha_registro"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jugador;
    }

    /** Devuelve todos los jugadores ordenados por nombre. */
    public List<Jugador> listarTodos() {
        List<Jugador> lista = new ArrayList<>();
        String sql = "SELECT id_jugador, nombre, email, fecha_registro FROM jugador ORDER BY nombre";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Jugador j = new Jugador();
                j.setIdJugador(rs.getInt("id_jugador"));
                j.setNombre(rs.getString("nombre"));
                j.setEmail(rs.getString("email"));
                j.setFechaRegistro(rs.getString("fecha_registro"));
                lista.add(j);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /** Devuelve todos los jugadores incluyendo el número de mazos que posee cada uno. */
    public List<Jugador> listarTodosConMazos() {
        List<Jugador> lista = new ArrayList<>();
        String sql = "SELECT j.id_jugador, j.nombre, j.email, j.fecha_registro, COUNT(m.id_mazo) AS num_mazos " +
                     "FROM jugador j LEFT JOIN mazo m ON m.id_jugador = j.id_jugador " +
                     "GROUP BY j.id_jugador ORDER BY j.nombre";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Jugador j = new Jugador();
                j.setIdJugador(rs.getInt("id_jugador"));
                j.setNombre(rs.getString("nombre"));
                j.setEmail(rs.getString("email"));
                j.setFechaRegistro(rs.getString("fecha_registro"));
                j.setNumMazos(rs.getInt("num_mazos"));
                lista.add(j);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /** Actualiza nombre y email del jugador. */
    public boolean actualizar(Jugador j) {
        String sql = "UPDATE jugador SET nombre = ?, email = ? WHERE id_jugador = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, j.getNombre());
            ps.setString(2, j.getEmail());
            ps.setInt(3, j.getIdJugador());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina el jugador con el ID indicado.
     * @param id identificador del jugador
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM jugador WHERE id_jugador = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
