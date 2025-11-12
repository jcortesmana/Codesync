package dao.Impl;

import models.Cliente;
import models.ClienteEstandar;
import models.ClientePremium;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dao.ClienteDAO;

import java.math.BigDecimal;

public class ClienteDAOImpl implements ClienteDAO {

    private final Connection conexion;

    public ClienteDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public int insertar(Cliente cliente) throws Exception {
        String sql = "INSERT INTO cliente (nombre, domicilio, nif, email, tipo_cliente) VALUES (?, ?, ?, ?, ?)";
        try {
            conexion.setAutoCommit(false);
            try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, cliente.getNombre());
                ps.setString(2, cliente.getDomicilio());
                ps.setString(3, cliente.getNif());
                ps.setString(4, cliente.getEmail());
                ps.setString(5, (cliente instanceof ClientePremium) ? "PREMIUM" : "ESTANDAR");
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int newId = keys.getInt(1);
                        conexion.commit();
                        return newId;
                    } else {
                        conexion.commit();
                        return -1;
                    }
                }
            }
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public int insertarConSP(Cliente cliente, BigDecimal cuotaAnual, BigDecimal descuento) throws Exception {
        String callCliente = "{CALL sp_insert_cliente(?, ?, ?, ?, ?, ?)}"; // last is OUT id
        String callPremium = "{CALL sp_insert_cliente_premium(?, ?, ?)}";
        CallableStatement csCliente = null;
        CallableStatement csPremium = null;
        try {
            conexion.setAutoCommit(false);
            csCliente = conexion.prepareCall(callCliente);
            csCliente.setString(1, cliente.getNombre());
            csCliente.setString(2, cliente.getDomicilio());
            csCliente.setString(3, cliente.getNif());
            csCliente.setString(4, cliente.getEmail());
            csCliente.setString(5, (cliente instanceof ClientePremium) ? "PREMIUM" : "ESTANDAR");
            csCliente.registerOutParameter(6, java.sql.Types.INTEGER);
            csCliente.execute();
            int newId = csCliente.getInt(6);

            if (cliente instanceof ClientePremium) {
                csPremium = conexion.prepareCall(callPremium);
                csPremium.setInt(1, newId);
                csPremium.setBigDecimal(2, cuotaAnual);
                csPremium.setBigDecimal(3, descuento);
                csPremium.execute();
            }
            conexion.commit();
            return newId;
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            if (csPremium != null) csPremium.close();
            if (csCliente != null) csCliente.close();
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public Cliente buscarPorEmail(String email) throws Exception {
        String sql = "SELECT c.id_cliente, c.nombre, c.domicilio, c.nif, c.email, c.tipo_cliente, cp.cuota_anual, cp.descuento " +
                "FROM cliente c LEFT JOIN cliente_premium cp ON c.id_cliente = cp.id_cliente WHERE c.email = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo_cliente");
                    if ("PREMIUM".equalsIgnoreCase(tipo)) {
                        ClientePremium cp = new ClientePremium(rs.getInt("id_cliente"),
                                rs.getString("nombre"),
                                rs.getString("domicilio"),
                                rs.getString("nif"),
                                rs.getString("email"),
                                rs.getDouble("cuota_anual"),
                                rs.getDouble("descuento"));
                        return cp;
                    } else {
                        ClienteEstandar ce = new ClienteEstandar(rs.getInt("id_cliente"),
                                rs.getString("nombre"),
                                rs.getString("domicilio"),
                                rs.getString("nif"),
                                rs.getString("email"));
                        return ce;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<Cliente> listarTodos() throws Exception {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT c.id_cliente, c.nombre, c.domicilio, c.nif, c.email, c.tipo_cliente, cp.cuota_anual, cp.descuento " +
                "FROM cliente c LEFT JOIN cliente_premium cp ON c.id_cliente = cp.id_cliente";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String tipo = rs.getString("tipo_cliente");
                if ("PREMIUM".equalsIgnoreCase(tipo)) {
                    ClientePremium cp = new ClientePremium(rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getString("email"),
                            rs.getDouble("cuota_anual"),
                            rs.getDouble("descuento"));
                    lista.add(cp);
                } else {
                    ClienteEstandar ce = new ClienteEstandar(rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getString("email"));
                    lista.add(ce);
                }
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Cliente cliente) throws Exception {
        String sql = "UPDATE cliente SET nombre=?, domicilio=?, nif=?, email=?, tipo_cliente=? WHERE id_cliente=?";
        try {
            conexion.setAutoCommit(false);
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, cliente.getNombre());
                ps.setString(2, cliente.getDomicilio());
                ps.setString(3, cliente.getNif());
                ps.setString(4, cliente.getEmail());
                ps.setString(5, (cliente instanceof ClientePremium) ? "PREMIUM" : "ESTANDAR");
                ps.setInt(6, cliente.getId());
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
        String sql = "DELETE FROM cliente WHERE id_cliente=?";
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