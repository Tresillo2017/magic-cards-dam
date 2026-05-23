package dao;

import model.TipoCarta;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoCartaDAO {

    private Connection conexion;

    public TipoCartaDAO() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    public List<TipoCarta> listarTodos() {
        List<TipoCarta> lista = new ArrayList<>();
        String sql = "SELECT id_tipo, nombre FROM tipo_carta ORDER BY nombre";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TipoCarta t = new TipoCarta();
                t.setIdTipo(rs.getInt("id_tipo"));
                t.setNombre(rs.getString("nombre"));
                lista.add(t);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
