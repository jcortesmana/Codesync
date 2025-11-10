package dao;

import models.Cliente;
import models.ClienteEstandar;
import models.ClientePremium;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {
    private Connection conexion;

    public ClienteDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void insertar(Cliente cliente) throws Exception {
        String sql = "INSERT INTO clientes (nombre, direccion, nif, email, tipo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDomicilio());
            ps.setString(3, cliente.getNif());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, (cliente instanceof ClientePremium) ? "premium" : "estandar");
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(Cliente cliente) throws Exception {
        String sql = "UPDATE clientes SET nombre=?, direccion=?, nif=?, tipo=? WHERE email=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDomicilio());
            ps.setString(3, cliente.getNif());
            ps.setString(4, (cliente instanceof ClientePremium) ? "premium" : "estandar");
            ps.setString(5, cliente.getEmail());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(String email) throws Exception {
        String sql = "DELETE FROM clientes WHERE email=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }

    @Override
    public Cliente buscarPorEmail(String email) throws Exception {
        String sql = "SELECT * FROM clientes WHERE email=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String tipo = rs.getString("tipo");
                if ("premium".equalsIgnoreCase(tipo)) {
                    return new ClientePremium(
                            rs.getString("nombre"),
                            rs.getString("direccion"),
                            rs.getString("nif"),
                            rs.getString("email")
                    );
                } else {
                    return new ClienteEstandar(
                            rs.getString("nombre"),
                            rs.getString("direccion"),
                            rs.getString("nif"),
                            rs.getString("email")
                    );
                }
            }
            return null;
        }
    }

    @Override
    public List<Cliente> listarTodos() throws Exception {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                Cliente c;
                if ("premium".equalsIgnoreCase(tipo)) {
                    c = new ClientePremium(
                            rs.getString("nombre"),
                            rs.getString("direccion"),
                            rs.getString("nif"),
                            rs.getString("email")
                    );
                } else {
                    c = new ClienteEstandar(
                            rs.getString("nombre"),
                            rs.getString("direccion"),
                            rs.getString("nif"),
                            rs.getString("email")
                    );
                }
                lista.add(c);
            }
        }
        return lista;
    }
}