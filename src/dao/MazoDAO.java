package dao;

import model.Jugador;
import model.Mazo;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MazoDAO {

    private Connection conexion;

    public MazoDAO() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    public void insertar(Mazo m) {
        String sql = "INSERT INTO mazo (nombre, id_jugador) VALUES (?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, m.getNombre());
            ps.setInt(2, m.getJugador().getIdJugador());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                m.setIdMazo(rs.getInt(1));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Mazo obtenerPorId(int id) {
        Mazo mazo = null;
        String sql = "SELECT m.id_mazo, m.nombre, j.id_jugador, j.nombre AS nombre_jugador, " +
                     "j.email, j.fecha_registro " +
                     "FROM mazo m JOIN jugador j ON j.id_jugador = m.id_jugador " +
                     "WHERE m.id_mazo = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Jugador jugador = new Jugador();
                jugador.setIdJugador(rs.getInt("id_jugador"));
                jugador.setNombre(rs.getString("nombre_jugador"));
                jugador.setEmail(rs.getString("email"));
                jugador.setFechaRegistro(rs.getString("fecha_registro"));
                mazo = new Mazo();
                mazo.setIdMazo(rs.getInt("id_mazo"));
                mazo.setNombre(rs.getString("nombre"));
                mazo.setJugador(jugador);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mazo;
    }

    public List<Mazo> listarTodos() {
        List<Mazo> lista = new ArrayList<>();
        String sql = "SELECT m.id_mazo, m.nombre, j.id_jugador, j.nombre AS nombre_jugador, " +
                     "j.email, j.fecha_registro " +
                     "FROM mazo m JOIN jugador j ON j.id_jugador = m.id_jugador ORDER BY m.nombre";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Jugador jugador = new Jugador();
                jugador.setIdJugador(rs.getInt("id_jugador"));
                jugador.setNombre(rs.getString("nombre_jugador"));
                jugador.setEmail(rs.getString("email"));
                jugador.setFechaRegistro(rs.getString("fecha_registro"));
                Mazo mazo = new Mazo();
                mazo.setIdMazo(rs.getInt("id_mazo"));
                mazo.setNombre(rs.getString("nombre"));
                mazo.setJugador(jugador);
                lista.add(mazo);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Mazo> listarPorJugador(int idJugador) {
        List<Mazo> lista = new ArrayList<>();
        String sql = "SELECT m.id_mazo, m.nombre, j.id_jugador, j.nombre AS nombre_jugador, " +
                     "j.email, j.fecha_registro " +
                     "FROM mazo m JOIN jugador j ON j.id_jugador = m.id_jugador " +
                     "WHERE m.id_jugador = ? ORDER BY m.nombre";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idJugador);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Jugador jugador = new Jugador();
                jugador.setIdJugador(rs.getInt("id_jugador"));
                jugador.setNombre(rs.getString("nombre_jugador"));
                jugador.setEmail(rs.getString("email"));
                jugador.setFechaRegistro(rs.getString("fecha_registro"));
                Mazo mazo = new Mazo();
                mazo.setIdMazo(rs.getInt("id_mazo"));
                mazo.setNombre(rs.getString("nombre"));
                mazo.setJugador(jugador);
                lista.add(mazo);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void actualizar(Mazo m) {
        String sql = "UPDATE mazo SET nombre = ?, id_jugador = ? WHERE id_mazo = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, m.getNombre());
            ps.setInt(2, m.getJugador().getIdJugador());
            ps.setInt(3, m.getIdMazo());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM mazo WHERE id_mazo = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
