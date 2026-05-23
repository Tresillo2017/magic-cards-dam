package dao;

import model.*;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartaDAO {

    private Connection conexion;

    public CartaDAO() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    public void insertar(Carta c) {
        String sql = "INSERT INTO carta (nombre, coste_mana, fuerza, resistencia, texto_habilidad, " +
                     "rareza, legendario, id_tipo_carta, id_tipo_secundario, id_edicion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getCosteMana());
            if (c.getFuerza() != null) ps.setInt(3, c.getFuerza());
            else ps.setNull(3, Types.INTEGER);
            if (c.getResistencia() != null) ps.setInt(4, c.getResistencia());
            else ps.setNull(4, Types.INTEGER);
            ps.setString(5, c.getTextoHabilidad());
            ps.setString(6, c.getRareza());
            ps.setBoolean(7, c.isLegendario());
            ps.setInt(8, c.getTipoCarta().getIdTipo());
            if (c.getTipoSecundario() != null) ps.setInt(9, c.getTipoSecundario().getIdTipo());
            else ps.setNull(9, Types.INTEGER);
            ps.setInt(10, c.getEdicion().getIdEdicion());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                c.setIdCarta(rs.getInt(1));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Carta obtenerPorId(int id) {
        Carta carta = null;
        String sql = "SELECT c.id_carta, c.nombre, c.coste_mana, c.fuerza, c.resistencia, " +
                     "c.texto_habilidad, c.rareza, c.legendario, " +
                     "t.id_tipo, t.nombre AS nombre_tipo, " +
                     "ts.id_tipo AS id_tipo_sec, ts.nombre AS nombre_tipo_sec, " +
                     "e.id_edicion, e.nombre AS nombre_edicion, e.fecha_lanzamiento " +
                     "FROM carta c " +
                     "JOIN tipo_carta t ON t.id_tipo = c.id_tipo_carta " +
                     "LEFT JOIN tipo_carta ts ON ts.id_tipo = c.id_tipo_secundario " +
                     "JOIN edicion e ON e.id_edicion = c.id_edicion " +
                     "WHERE c.id_carta = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                carta = new Carta();
                carta.setIdCarta(rs.getInt("id_carta"));
                carta.setNombre(rs.getString("nombre"));
                carta.setCosteMana(rs.getInt("coste_mana"));
                int f = rs.getInt("fuerza");
                carta.setFuerza(rs.wasNull() ? null : f);
                int r = rs.getInt("resistencia");
                carta.setResistencia(rs.wasNull() ? null : r);
                carta.setTextoHabilidad(rs.getString("texto_habilidad"));
                carta.setRareza(rs.getString("rareza"));
                carta.setLegendario(rs.getBoolean("legendario"));
                TipoCarta tipo = new TipoCarta();
                tipo.setIdTipo(rs.getInt("id_tipo"));
                tipo.setNombre(rs.getString("nombre_tipo"));
                carta.setTipoCarta(tipo);
                int idSec = rs.getInt("id_tipo_sec");
                if (!rs.wasNull()) {
                    TipoCarta tipoSec = new TipoCarta();
                    tipoSec.setIdTipo(idSec);
                    tipoSec.setNombre(rs.getString("nombre_tipo_sec"));
                    carta.setTipoSecundario(tipoSec);
                }
                Edicion edicion = new Edicion();
                edicion.setIdEdicion(rs.getInt("id_edicion"));
                edicion.setNombre(rs.getString("nombre_edicion"));
                carta.setEdicion(edicion);
                carta.setColores(obtenerColores(carta.getIdCarta()));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carta;
    }

    public List<Carta> listarTodos() {
        List<Carta> lista = new ArrayList<>();
        String sql = "SELECT c.id_carta, c.nombre, c.coste_mana, c.fuerza, c.resistencia, " +
                     "c.texto_habilidad, c.rareza, c.legendario, " +
                     "t.id_tipo, t.nombre AS nombre_tipo, " +
                     "e.id_edicion, e.nombre AS nombre_edicion " +
                     "FROM carta c " +
                     "JOIN tipo_carta t ON t.id_tipo = c.id_tipo_carta " +
                     "JOIN edicion e ON e.id_edicion = c.id_edicion " +
                     "ORDER BY c.nombre";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Carta carta = new Carta();
                carta.setIdCarta(rs.getInt("id_carta"));
                carta.setNombre(rs.getString("nombre"));
                carta.setCosteMana(rs.getInt("coste_mana"));
                int f = rs.getInt("fuerza");
                carta.setFuerza(rs.wasNull() ? null : f);
                int r = rs.getInt("resistencia");
                carta.setResistencia(rs.wasNull() ? null : r);
                carta.setTextoHabilidad(rs.getString("texto_habilidad"));
                carta.setRareza(rs.getString("rareza"));
                carta.setLegendario(rs.getBoolean("legendario"));
                TipoCarta tipo = new TipoCarta();
                tipo.setIdTipo(rs.getInt("id_tipo"));
                tipo.setNombre(rs.getString("nombre_tipo"));
                carta.setTipoCarta(tipo);
                Edicion edicion = new Edicion();
                edicion.setIdEdicion(rs.getInt("id_edicion"));
                edicion.setNombre(rs.getString("nombre_edicion"));
                carta.setEdicion(edicion);
                lista.add(carta);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Carta> buscarPorNombre(String nombre) {
        List<Carta> lista = new ArrayList<>();
        String sql = "SELECT c.id_carta, c.nombre, c.coste_mana, c.rareza, c.legendario, " +
                     "t.id_tipo, t.nombre AS nombre_tipo, e.id_edicion, e.nombre AS nombre_edicion " +
                     "FROM carta c " +
                     "JOIN tipo_carta t ON t.id_tipo = c.id_tipo_carta " +
                     "JOIN edicion e ON e.id_edicion = c.id_edicion " +
                     "WHERE c.nombre LIKE ? ORDER BY c.nombre";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Carta carta = new Carta();
                carta.setIdCarta(rs.getInt("id_carta"));
                carta.setNombre(rs.getString("nombre"));
                carta.setCosteMana(rs.getInt("coste_mana"));
                carta.setRareza(rs.getString("rareza"));
                carta.setLegendario(rs.getBoolean("legendario"));
                TipoCarta tipo = new TipoCarta();
                tipo.setIdTipo(rs.getInt("id_tipo"));
                tipo.setNombre(rs.getString("nombre_tipo"));
                carta.setTipoCarta(tipo);
                Edicion edicion = new Edicion();
                edicion.setIdEdicion(rs.getInt("id_edicion"));
                edicion.setNombre(rs.getString("nombre_edicion"));
                carta.setEdicion(edicion);
                lista.add(carta);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void actualizar(Carta c) {
        String sql = "UPDATE carta SET nombre = ?, coste_mana = ?, fuerza = ?, resistencia = ?, " +
                     "texto_habilidad = ?, rareza = ?, legendario = ?, " +
                     "id_tipo_carta = ?, id_tipo_secundario = ?, id_edicion = ? WHERE id_carta = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getCosteMana());
            if (c.getFuerza() != null) ps.setInt(3, c.getFuerza());
            else ps.setNull(3, Types.INTEGER);
            if (c.getResistencia() != null) ps.setInt(4, c.getResistencia());
            else ps.setNull(4, Types.INTEGER);
            ps.setString(5, c.getTextoHabilidad());
            ps.setString(6, c.getRareza());
            ps.setBoolean(7, c.isLegendario());
            ps.setInt(8, c.getTipoCarta().getIdTipo());
            if (c.getTipoSecundario() != null) ps.setInt(9, c.getTipoSecundario().getIdTipo());
            else ps.setNull(9, Types.INTEGER);
            ps.setInt(10, c.getEdicion().getIdEdicion());
            ps.setInt(11, c.getIdCarta());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM carta WHERE id_carta = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Color> obtenerColores(int idCarta) {
        List<Color> colores = new ArrayList<>();
        String sql = "SELECT col.id_color, col.nombre FROM color col " +
                     "JOIN carta_color cc ON cc.id_color = col.id_color WHERE cc.id_carta = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idCarta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Color color = new Color();
                color.setIdColor(rs.getInt("id_color"));
                color.setNombre(rs.getString("nombre"));
                colores.add(color);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return colores;
    }
}
