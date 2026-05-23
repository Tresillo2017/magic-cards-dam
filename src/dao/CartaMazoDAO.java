package dao;

import model.*;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartaMazoDAO {

    private Connection conexion;

    public CartaMazoDAO() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    public void insertar(CartaMazo cm) {
        String sql = "INSERT INTO carta_mazo (id_mazo, id_carta, cantidad) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, cm.getMazo().getIdMazo());
            ps.setInt(2, cm.getCarta().getIdCarta());
            ps.setInt(3, cm.getCantidad());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CartaMazo> listarPorMazo(int idMazo) {
        List<CartaMazo> lista = new ArrayList<>();
        String sql = "SELECT cm.cantidad, c.id_carta, c.nombre, c.coste_mana, " +
                     "c.fuerza, c.resistencia, c.rareza, c.legendario, " +
                     "t.id_tipo, t.nombre AS nombre_tipo, " +
                     "e.id_edicion, e.nombre AS nombre_edicion " +
                     "FROM carta_mazo cm " +
                     "JOIN carta c ON c.id_carta = cm.id_carta " +
                     "JOIN tipo_carta t ON t.id_tipo = c.id_tipo_carta " +
                     "JOIN edicion e ON e.id_edicion = c.id_edicion " +
                     "WHERE cm.id_mazo = ? ORDER BY c.nombre";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idMazo);
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
                Mazo mazo = new Mazo();
                mazo.setIdMazo(idMazo);
                CartaMazo cm = new CartaMazo();
                cm.setCarta(carta);
                cm.setMazo(mazo);
                cm.setCantidad(rs.getInt("cantidad"));
                lista.add(cm);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void actualizar(CartaMazo cm) {
        String sql = "UPDATE carta_mazo SET cantidad = ? WHERE id_mazo = ? AND id_carta = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, cm.getCantidad());
            ps.setInt(2, cm.getMazo().getIdMazo());
            ps.setInt(3, cm.getCarta().getIdCarta());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int idMazo, int idCarta) {
        String sql = "DELETE FROM carta_mazo WHERE id_mazo = ? AND id_carta = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idMazo);
            ps.setInt(2, idCarta);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarPorMazo(int idMazo) {
        String sql = "DELETE FROM carta_mazo WHERE id_mazo = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idMazo);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
