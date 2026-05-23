package dao;

import model.Edicion;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EdicionDAO {

    private Connection conexion;

    public EdicionDAO() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    public List<Edicion> listarTodas() {
        List<Edicion> lista = new ArrayList<>();
        String sql = "SELECT id_edicion, nombre, fecha_lanzamiento FROM edicion ORDER BY nombre";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Edicion e = new Edicion();
                e.setIdEdicion(rs.getInt("id_edicion"));
                e.setNombre(rs.getString("nombre"));
                e.setFechaLanzamiento(rs.getString("fecha_lanzamiento"));
                lista.add(e);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
