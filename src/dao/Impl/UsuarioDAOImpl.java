package dao.Impl;

import models.Usuario;
import dao.UsuarioDAO;

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
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion.setAutoCommit(false);

         
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getPassword());
            ps.executeUpdate();

            // ✅ Recuperamos el ID generado automáticamente
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }

            conexion.commit();
        } catch (SQLException e) {
            System.err.println(" Error al insertar usuario, haciendo rollback: " + e.getMessage());
            conexion.rollback();
            throw e;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public void insertarConSP(Usuario usuario) throws Exception {
        String call = "{CALL sp_insert_usuario(?, ?, ?)}";
        CallableStatement cs = null;

        try {
            conexion.setAutoCommit(false);
            cs = conexion.prepareCall(call);
            cs.setString(1, usuario.getNombre());
            cs.setString(2, usuario.getEmail());
            cs.setString(3, usuario.getPassword());
            cs.execute();

            conexion.commit();
        } catch (SQLException e) {
            System.err.println(" Error al insertar con SP, haciendo rollback: " + e.getMessage());
            conexion.rollback();
            throw e;
        } finally {
            if (cs != null) try { cs.close(); } catch (SQLException ignored) {}
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
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
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
                lista.add(new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("password")
                ));
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Usuario usuario) throws Exception {
        String sql = "UPDATE usuarios SET nombre=?, email=?, password=? WHERE id=?";
        PreparedStatement ps = null;

        try {
            conexion.setAutoCommit(false);
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getPassword());
            ps.setInt(4, usuario.getId());
            ps.executeUpdate();

            conexion.commit();
        } catch (SQLException e) {
            System.err.println(" Error al actualizar usuario, haciendo rollback: " + e.getMessage());
            conexion.rollback();
            throw e;
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM usuarios WHERE id=?";
        PreparedStatement ps = null;

        try {
            conexion.setAutoCommit(false);
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

            conexion.commit();
        } catch (SQLException e) {
            System.err.println(" Error al eliminar usuario, haciendo rollback: " + e.getMessage());
            conexion.rollback();
            throw e;
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            conexion.setAutoCommit(true);
        }
    }
}
