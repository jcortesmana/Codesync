package dao.Impl;

import dao.ArticuloDAO;
import models.Articulo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDAOImpl implements ArticuloDAO {

    private Connection conexion;

    public ArticuloDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void insertar(Articulo articulo) throws Exception {
        String sql = "INSERT INTO articulo (codigo, descripcion, precio, gastosEnvio, tiempoPreparacion) VALUES (?, ?, ?, ?, ?)";

        try {
            conexion.setAutoCommit(false);

            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, articulo.getCodigo());
                stmt.setString(2, articulo.getDescripcion());
                stmt.setDouble(3, articulo.getPrecio());
                stmt.setDouble(4, articulo.getGastosEnvio());
                stmt.setInt(5, articulo.getTiempoPreparacion());

                stmt.executeUpdate();
            }

            conexion.commit();
        } catch (Exception e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public Articulo buscarPorCodigo(String codigo) throws Exception {
        String sql = "SELECT * FROM articulo WHERE codigo = ?";
        Articulo articulo = null;

        try {
            conexion.setAutoCommit(false);

            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, codigo);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    articulo = new Articulo(
                            rs.getString("codigo"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getDouble("gastosEnvio"),
                            rs.getInt("tiempoPreparacion")
                    );
                }
            }

            conexion.commit();
        } catch (Exception e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }

        return articulo;
    }

    @Override
    public List<Articulo> obtenerTodos() throws Exception {
        List<Articulo> articulos = new ArrayList<>();
        String sql = "SELECT * FROM articulo";

        try {
            conexion.setAutoCommit(false);

            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    articulos.add(new Articulo(
                            rs.getString("codigo"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getDouble("gastosEnvio"),
                            rs.getInt("tiempoPreparacion")
                    ));
                }
            }

            conexion.commit();
        } catch (Exception e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }

        return articulos;
    }

    @Override
    public void eliminar(String codigo) throws Exception {
        String sql = "DELETE FROM articulo WHERE codigo = ?";

        try {
            conexion.setAutoCommit(false);

            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, codigo);
                stmt.executeUpdate();
            }

            conexion.commit();
        } catch (Exception e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }
}
