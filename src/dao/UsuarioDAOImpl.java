package dao;

import models.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    private final Connection conexion;

    public UsuarioDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void insertar(Usuario usuario) throws Exception {
        String sql = "INSERT INTO usuarios (nombre, email, password) VALUES (?, ?, ?)";
        try {
            conexion.setAutoCommit(false);
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getEmail());
                ps.setString(3, usuario.getPassword());
                ps.executeUpdate();
            }
            conexion.commit();
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public void insertarConSP(Usuario usuario) throws Exception {
        String call = "{CALL sp_insert_usuario(?, ?, ?)}";
        try {
            conexion.setAutoCommit(false);
            try (CallableStatement cs = conexion.prepareCall(call)) {
                cs.setString(1, usuario.getNombre());
                cs.setString(2, usuario.getEmail());
                cs.setString(3, usuario.getPassword());
                cs.execute();
            }
            conexion.commit();
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public Usuario buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("email"), rs.getString("password"));
                }
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() throws Exception {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("email"), rs.getString("password")));
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Usuario usuario) throws Exception {
        String sql = "UPDATE usuarios SET nombre=?, email=?, password=? WHERE id=?";
        try {
            conexion.setAutoCommit(false);
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getEmail());
                ps.setString(3, usuario.getPassword());
                ps.setInt(4, usuario.getId());
                ps.executeUpdate();
            }
            conexion.commit();
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM usuarios WHERE id=?";
        try {
            conexion.setAutoCommit(false);
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            conexion.commit();
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }
}