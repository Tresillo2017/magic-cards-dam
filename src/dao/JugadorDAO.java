package dao;

import model.Jugador;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JugadorDAO {

    private Connection conexion;

    public JugadorDAO() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    public void insertar(Jugador j) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public void actualizar(Jugador j) {
        String sql = "UPDATE jugador SET nombre = ?, email = ? WHERE id_jugador = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, j.getNombre());
            ps.setString(2, j.getEmail());
            ps.setInt(3, j.getIdJugador());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM jugador WHERE id_jugador = ?";
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
