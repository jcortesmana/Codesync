package dao.Impl;

import dao.ArticuloDAO;
import models.Articulo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDAOImpl implements ArticuloDAO {

    private final Connection conexion;

    public ArticuloDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void insertar(Articulo articulo) throws Exception {
        String sql = "INSERT INTO articulo (codigo, descripcion, precio, gastosEnvio, tiempoPreparacion) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, articulo.getCodigo());
            stmt.setString(2, articulo.getDescripcion());
            stmt.setDouble(3, articulo.getPrecio());
            stmt.setDouble(4, articulo.getGastosEnvio());
            stmt.setInt(5, articulo.getTiempoPreparacion());

            stmt.executeUpdate();
        }
    }

    @Override
    public Articulo buscarPorCodigo(String codigo) throws Exception {
        String sql = "SELECT codigo, descripcion, precio, gastosEnvio, tiempoPreparacion " +
                     "FROM articulo WHERE codigo = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Articulo(
                            rs.getString("codigo"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getDouble("gastosEnvio"),
                            rs.getInt("tiempoPreparacion")
                    );
                }
            }
        }

        return null;
    }

    @Override
    public List<Articulo> obtenerTodos() throws Exception {
        List<Articulo> lista = new ArrayList<>();

        String sql = "SELECT codigo, descripcion, precio, gastosEnvio, tiempoPreparacion FROM articulo";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getDouble("gastosEnvio"),
                        rs.getInt("tiempoPreparacion")
                ));
            }
        }

        return lista;
    }

    @Override
    public void eliminar(String codigo) throws Exception {
        String sql = "DELETE FROM articulo WHERE codigo = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            stmt.executeUpdate();
        }
    }
}
