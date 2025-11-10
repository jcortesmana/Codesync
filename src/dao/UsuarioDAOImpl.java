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
            conexion.setAutoCommit(false); // 🔹 Inicia transacción

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getEmail());
                ps.setString(3, usuario.getPassword());
                ps.executeUpdate();
            }

            conexion.commit(); // ✅ Confirma cambios
            System.out.println("✔️ Usuario insertado correctamente: " + usuario.getEmail());
        } catch (SQLException e) {
            conexion.rollback(); // 🔄 Revierte si hay error
            throw new SQLException("❌ Error al insertar usuario: " + e.getMessage());
        } finally {
            conexion.setAutoCommit(true);
        }
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
            System.out.println("✏️ Usuario actualizado correctamente.");
        } catch (SQLException e) {
            conexion.rollback();
            throw new SQLException("❌ Error al actualizar usuario: " + e.getMessage());
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
            System.out.println("🗑️ Usuario eliminado (ID=" + id + ")");
        } catch (SQLException e) {
            conexion.rollback();
            throw new SQLException("❌ Error al eliminar usuario: " + e.getMessage());
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public Usuario buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM usuarios WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password")
                );
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
                Usuario u = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                lista.add(u);
            }
        }
        return lista;
    }
}