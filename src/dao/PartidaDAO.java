package dao;

import model.Jugador;
import model.Partida;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la entidad Partida.
 * Proporciona operaciones CRUD sobre la tabla {@code partida}.
 */
public class PartidaDAO {

    private Connection conexion;

    public PartidaDAO() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    /** Inserta una nueva partida con la fecha actual y actualiza su {@code idPartida}. */
    public boolean insertar(Partida p) {
        String sql = "INSERT INTO partida (fecha, id_jugador1, id_jugador2, id_ganador) VALUES (NOW(), ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, p.getJugador1().getIdJugador());
            ps.setInt(2, p.getJugador2().getIdJugador());
            if (p.getGanador() != null) ps.setInt(3, p.getGanador().getIdJugador());
            else ps.setNull(3, Types.INTEGER);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                p.setIdPartida(rs.getInt(1));
            }
            rs.close();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Partida obtenerPorId(int id) {
        Partida partida = null;
        String sql = "SELECT p.id_partida, p.fecha, " +
                     "j1.id_jugador AS id_j1, j1.nombre AS nombre_j1, j1.email AS email_j1, " +
                     "j2.id_jugador AS id_j2, j2.nombre AS nombre_j2, j2.email AS email_j2, " +
                     "g.id_jugador AS id_g, g.nombre AS nombre_g " +
                     "FROM partida p " +
                     "JOIN jugador j1 ON j1.id_jugador = p.id_jugador1 " +
                     "JOIN jugador j2 ON j2.id_jugador = p.id_jugador2 " +
                     "LEFT JOIN jugador g ON g.id_jugador = p.id_ganador " +
                     "WHERE p.id_partida = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                partida = new Partida();
                partida.setIdPartida(rs.getInt("id_partida"));
                partida.setFecha(rs.getString("fecha"));
                Jugador j1 = new Jugador();
                j1.setIdJugador(rs.getInt("id_j1"));
                j1.setNombre(rs.getString("nombre_j1"));
                j1.setEmail(rs.getString("email_j1"));
                partida.setJugador1(j1);
                Jugador j2 = new Jugador();
                j2.setIdJugador(rs.getInt("id_j2"));
                j2.setNombre(rs.getString("nombre_j2"));
                j2.setEmail(rs.getString("email_j2"));
                partida.setJugador2(j2);
                int idG = rs.getInt("id_g");
                if (!rs.wasNull()) {
                    Jugador ganador = new Jugador();
                    ganador.setIdJugador(idG);
                    ganador.setNombre(rs.getString("nombre_g"));
                    partida.setGanador(ganador);
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partida;
    }

    public List<Partida> listarTodas() {
        List<Partida> lista = new ArrayList<>();
        String sql = "SELECT p.id_partida, p.fecha, " +
                     "j1.id_jugador AS id_j1, j1.nombre AS nombre_j1, j1.email AS email_j1, " +
                     "j2.id_jugador AS id_j2, j2.nombre AS nombre_j2, j2.email AS email_j2, " +
                     "g.id_jugador AS id_g, g.nombre AS nombre_g " +
                     "FROM partida p " +
                     "JOIN jugador j1 ON j1.id_jugador = p.id_jugador1 " +
                     "JOIN jugador j2 ON j2.id_jugador = p.id_jugador2 " +
                     "LEFT JOIN jugador g ON g.id_jugador = p.id_ganador " +
                     "ORDER BY p.fecha DESC";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Partida partida = new Partida();
                partida.setIdPartida(rs.getInt("id_partida"));
                partida.setFecha(rs.getString("fecha"));
                Jugador j1 = new Jugador();
                j1.setIdJugador(rs.getInt("id_j1"));
                j1.setNombre(rs.getString("nombre_j1"));
                j1.setEmail(rs.getString("email_j1"));
                partida.setJugador1(j1);
                Jugador j2 = new Jugador();
                j2.setIdJugador(rs.getInt("id_j2"));
                j2.setNombre(rs.getString("nombre_j2"));
                j2.setEmail(rs.getString("email_j2"));
                partida.setJugador2(j2);
                int idG = rs.getInt("id_g");
                if (!rs.wasNull()) {
                    Jugador ganador = new Jugador();
                    ganador.setIdJugador(idG);
                    ganador.setNombre(rs.getString("nombre_g"));
                    partida.setGanador(ganador);
                }
                lista.add(partida);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Partida> listarPorJugador(int idJugador) {
        List<Partida> lista = new ArrayList<>();
        String sql = "SELECT p.id_partida, p.fecha, " +
                     "j1.id_jugador AS id_j1, j1.nombre AS nombre_j1, j1.email AS email_j1, " +
                     "j2.id_jugador AS id_j2, j2.nombre AS nombre_j2, j2.email AS email_j2, " +
                     "g.id_jugador AS id_g, g.nombre AS nombre_g " +
                     "FROM partida p " +
                     "JOIN jugador j1 ON j1.id_jugador = p.id_jugador1 " +
                     "JOIN jugador j2 ON j2.id_jugador = p.id_jugador2 " +
                     "LEFT JOIN jugador g ON g.id_jugador = p.id_ganador " +
                     "WHERE p.id_jugador1 = ? OR p.id_jugador2 = ? ORDER BY p.fecha DESC";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idJugador);
            ps.setInt(2, idJugador);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Partida partida = new Partida();
                partida.setIdPartida(rs.getInt("id_partida"));
                partida.setFecha(rs.getString("fecha"));
                Jugador j1 = new Jugador();
                j1.setIdJugador(rs.getInt("id_j1"));
                j1.setNombre(rs.getString("nombre_j1"));
                j1.setEmail(rs.getString("email_j1"));
                partida.setJugador1(j1);
                Jugador j2 = new Jugador();
                j2.setIdJugador(rs.getInt("id_j2"));
                j2.setNombre(rs.getString("nombre_j2"));
                j2.setEmail(rs.getString("email_j2"));
                partida.setJugador2(j2);
                int idG = rs.getInt("id_g");
                if (!rs.wasNull()) {
                    Jugador ganador = new Jugador();
                    ganador.setIdJugador(idG);
                    ganador.setNombre(rs.getString("nombre_g"));
                    partida.setGanador(ganador);
                }
                lista.add(partida);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Asigna un ganador a una partida existente.
     * @param idPartida  identificador de la partida
     * @param idGanador  identificador del jugador ganador
     */
    public boolean registrarGanador(int idPartida, int idGanador) {
        String sql = "UPDATE partida SET id_ganador = ? WHERE id_partida = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idGanador);
            ps.setInt(2, idPartida);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Pone el ganador de una partida a NULL (marca la partida como "en curso").
     * @param idPartida identificador de la partida
     */
    public boolean quitarGanador(int idPartida) {
        String sql = "UPDATE partida SET id_ganador = NULL WHERE id_partida = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPartida);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM partida WHERE id_partida = ?";
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
