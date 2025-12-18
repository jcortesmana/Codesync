package onlinestore.dao.Impl;

import onlinestore.models.Cliente;
import onlinestore.models.ClienteEstandar;
import onlinestore.models.ClientePremium;

import onlinestore.dao.ClienteDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ClienteDAOImpl implements ClienteDAO {

    private final Connection conexion;

    public ClienteDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    // =======================================================
    //   INSERTAR SIN PROCEDURE (NORMAL)
    // =======================================================
    @Override
    public int insertar(Cliente cliente) throws Exception {

        String sqlCliente =
                "INSERT INTO cliente (nombre, domicilio, nif, email, tipo_cliente) " +
                "VALUES (?, ?, ?, ?, ?)";

        String sqlInsertEstandar =
                "INSERT INTO cliente_estandar (id_cliente) VALUES (?)";

        String sqlInsertPremium =
                "INSERT INTO cliente_premium (id_cliente, cuota_anual, descuento) VALUES (?, ?, ?)";

        PreparedStatement psCliente = null;
        PreparedStatement psEstandar = null;
        PreparedStatement psPremium = null;
        ResultSet rs = null;

        try {
            conexion.setAutoCommit(false);

            // 1️⃣ Insertamos en tabla cliente
            psCliente = conexion.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS);
            psCliente.setString(1, cliente.getNombre());
            psCliente.setString(2, cliente.getDomicilio());
            psCliente.setString(3, cliente.getNif());
            psCliente.setString(4, cliente.getEmail());
            psCliente.setString(5, (cliente instanceof ClientePremium) ? "PREMIUM" : "ESTANDAR");

            psCliente.executeUpdate();

            
            rs = psCliente.getGeneratedKeys();
            int newId = -1;

            if (rs.next()) {
                newId = rs.getInt(1);
                cliente.setId(newId);
            }

           
            if (cliente instanceof ClienteEstandar) {
                psEstandar = conexion.prepareStatement(sqlInsertEstandar);
                psEstandar.setInt(1, newId);
                psEstandar.executeUpdate();
            }

         
            if (cliente instanceof ClientePremium cp) {
                psPremium = conexion.prepareStatement(sqlInsertPremium);
                psPremium.setInt(1, newId);
                psPremium.setDouble(2, cp.getCuotaAnual());
                psPremium.setDouble(3, cp.getDescuento());
                psPremium.executeUpdate();
            }

            conexion.commit();
            return newId;

        } catch (SQLException e) {
            conexion.rollback();
            throw e;

        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (psCliente != null) try { psCliente.close(); } catch (SQLException ignored) {}
            if (psEstandar != null) try { psEstandar.close(); } catch (SQLException ignored) {}
            if (psPremium != null) try { psPremium.close(); } catch (SQLException ignored) {}
            conexion.setAutoCommit(true);
        }
    }

    // =======================================================
    //   INSERTAR CON PROCEDURE 
    // =======================================================
    public int insertarConSP(Cliente cliente, BigDecimal cuotaAnual, BigDecimal descuento) throws Exception {

        String callCliente = "{ CALL sp_insert_cliente(?, ?, ?, ?, ?, ?) }";
        String callPremium = "{ CALL sp_insert_cliente_premium(?, ?, ?) }";

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

            csCliente.registerOutParameter(6, Types.INTEGER);
            csCliente.execute();

            int newId = csCliente.getInt(6);
            cliente.setId(newId);

            // Premium extra
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
            if (csPremium != null) try { csPremium.close(); } catch (SQLException ignored) {}
            if (csCliente != null) try { csCliente.close(); } catch (SQLException ignored) {}
            conexion.setAutoCommit(true);
        }
    }

    // =======================================================
    //   BUSCAR POR EMAIL
    // =======================================================
    @Override
    public Cliente buscarPorEmail(String email) throws Exception {

        String sql = """
                SELECT c.id_cliente, c.nombre, c.domicilio, c.nif, c.email, c.tipo_cliente,
                       cp.id_premium, cp.cuota_anual, cp.descuento,
                       ce.id_estandar
                FROM cliente c
                LEFT JOIN cliente_premium cp ON c.id_cliente = cp.id_cliente
                LEFT JOIN cliente_estandar ce ON c.id_cliente = ce.id_cliente
                WHERE c.email = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                if ("PREMIUM".equalsIgnoreCase(rs.getString("tipo_cliente"))) {
                    return new ClientePremium(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getString("email"),
                            rs.getDouble("cuota_anual"),
                            rs.getDouble("descuento")
                    );
                }

                return new ClienteEstandar(
                        rs.getInt("id_cliente"),
                        rs.getInt("id_estandar"),
                        rs.getString("nombre"),
                        rs.getString("domicilio"),
                        rs.getString("nif"),
                        rs.getString("email")
                );
            }
        }
        return null;
    }

    // =======================================================
    //   LISTAR TODOS
    // =======================================================
    @Override
    public List<Cliente> listarTodos() throws Exception {

        List<Cliente> lista = new ArrayList<>();
        String call = "{CALL sp_listar_todos()}";

        try (CallableStatement cs = conexion.prepareCall(call);
            ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {

                String tipo = rs.getString("tipo_cliente");

               if ("PREMIUM".equalsIgnoreCase(tipo)) {

    ClientePremium cp = new ClientePremium(
            rs.getInt("id_cliente"),
            rs.getString("nombre"),
            rs.getString("domicilio"),
            rs.getString("nif"),
            rs.getString("email")
    );

    cp.setCuotaAnual(0.0);
    cp.setDescuento(0.0);

    lista.add(cp);

} else {

    lista.add(new ClienteEstandar(
            rs.getInt("id_cliente"),
            0,
            rs.getString("nombre"),
            rs.getString("domicilio"),
            rs.getString("nif"),
            rs.getString("email")
    ));
}

            }
        }

        return lista;
    }



    // =======================================================
    //   ACTUALIZAR
    // =======================================================
    @Override
    public void actualizar(Cliente cliente) throws Exception {

        String sql = "UPDATE cliente SET nombre=?, domicilio=?, nif=?, email=?, tipo_cliente=? " +
                     "WHERE id_cliente=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDomicilio());
            ps.setString(3, cliente.getNif());
            ps.setString(4, cliente.getEmail());
            ps.setString(5,
                    (cliente instanceof ClientePremium) ? "PREMIUM" : "ESTANDAR");

            ps.setInt(6, cliente.getId());
            ps.executeUpdate();
        }
    }

    // =======================================================
    //   ELIMINAR
    // =======================================================
    @Override
    public void eliminar(int id) throws Exception {

        
        String delPremium = "DELETE FROM cliente_premium WHERE id_cliente=?";
        String delEstandar = "DELETE FROM cliente_estandar WHERE id_cliente=?";
        String delCliente = "DELETE FROM cliente WHERE id_cliente=?";

        try {
            conexion.setAutoCommit(false);

            try (PreparedStatement ps = conexion.prepareStatement(delPremium)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conexion.prepareStatement(delEstandar)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conexion.prepareStatement(delCliente)) {
                ps.setInt(1, id);
                ps.executeUpdate();
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
public List<Cliente> listarEstandar() throws Exception {

    List<Cliente> lista = new ArrayList<>();

    String call = "{CALL sp_listar_estandar()}";

    try (CallableStatement cs = conexion.prepareCall(call);
         ResultSet rs = cs.executeQuery()) {

        while (rs.next()) {
            lista.add(new ClienteEstandar(
                    rs.getInt("id_cliente"),
                    rs.getInt("id_estandar"),
                    rs.getString("nombre"),
                    rs.getString("domicilio"),
                    rs.getString("nif"),
                    rs.getString("email")
            ));
        }
    }

    return lista;
}

@Override
public List<Cliente> listarPremium() throws Exception {

    List<Cliente> lista = new ArrayList<>();

    String call = "{CALL sp_listar_premium()}";

    try (CallableStatement cs = conexion.prepareCall(call);
         ResultSet rs = cs.executeQuery()) {

        while (rs.next()) {
            lista.add(new ClientePremium(
                    rs.getInt("id_cliente"),
                    rs.getString("nombre"),
                    rs.getString("domicilio"),
                    rs.getString("nif"),
                    rs.getString("email"),
                    rs.getDouble("cuota_anual"),
                    rs.getDouble("descuento")
            ));
        }
    }

    return lista;
}




}
