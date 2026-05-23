package dao;

import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultasDAO {

    private Connection conexion;

    public ConsultasDAO() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    // Consulta 1: cartas por tipo y color (JOIN múltiple)
    public List<String[]> cartasPorTipoYColor() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT c.nombre AS carta, t.nombre AS tipo, col.nombre AS color, c.rareza " +
                     "FROM carta c " +
                     "JOIN tipo_carta t ON t.id_tipo = c.id_tipo_carta " +
                     "JOIN carta_color cc ON cc.id_carta = c.id_carta " +
                     "JOIN color col ON col.id_color = cc.id_color " +
                     "ORDER BY t.nombre, col.nombre, c.nombre";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString("carta"),
                    rs.getString("tipo"),
                    rs.getString("color"),
                    rs.getString("rareza")
                });
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Consulta 2: mazos con más cartas por jugador (GROUP BY + JOIN)
    public List<String[]> mazosConMasCartasPorJugador() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT j.nombre AS jugador, m.nombre AS mazo, " +
                     "SUM(cm.cantidad) AS total_cartas " +
                     "FROM jugador j " +
                     "JOIN mazo m ON m.id_jugador = j.id_jugador " +
                     "JOIN carta_mazo cm ON cm.id_mazo = m.id_mazo " +
                     "GROUP BY j.id_jugador, m.id_mazo " +
                     "ORDER BY total_cartas DESC";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString("jugador"),
                    rs.getString("mazo"),
                    rs.getString("total_cartas")
                });
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Consulta 3: jugadores con más victorias (GROUP BY)
    public List<String[]> jugadoresConMasVictorias() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT j.nombre AS jugador, COUNT(p.id_partida) AS victorias " +
                     "FROM jugador j " +
                     "LEFT JOIN partida p ON p.id_ganador = j.id_jugador " +
                     "GROUP BY j.id_jugador " +
                     "ORDER BY victorias DESC";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString("jugador"),
                    rs.getString("victorias")
                });
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Consulta 4: cartas de una edición con coste superior a la media (subconsulta)
    public List<String[]> cartasSobreMediaPorEdicion(int idEdicion) {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT c.nombre, c.coste_mana, t.nombre AS tipo, c.rareza " +
                     "FROM carta c " +
                     "JOIN tipo_carta t ON t.id_tipo = c.id_tipo_carta " +
                     "WHERE c.id_edicion = ? " +
                     "AND c.coste_mana > (SELECT AVG(coste_mana) FROM carta WHERE id_edicion = ?) " +
                     "ORDER BY c.coste_mana DESC";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idEdicion);
            ps.setInt(2, idEdicion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString("nombre"),
                    rs.getString("coste_mana"),
                    rs.getString("tipo"),
                    rs.getString("rareza")
                });
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Función SQL: coste total de maná de un mazo
    public int costeTotalMazo(int idMazo) {
        int total = 0;
        String sql = "SELECT coste_total_mazo(?) AS total";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idMazo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // Procedimiento almacenado: registrar partida
    public int registrarPartidaProcedimiento(int idJugador1, int idJugador2, int idGanador) {
        int idPartida = -1;
        String sql = "{CALL registrar_partida(?, ?, ?, ?)}";
        try {
            CallableStatement cs = conexion.prepareCall(sql);
            cs.setInt(1, idJugador1);
            cs.setInt(2, idJugador2);
            if (idGanador != -1) cs.setInt(3, idGanador);
            else cs.setNull(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.execute();
            idPartida = cs.getInt(4);
            cs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idPartida;
    }
}
