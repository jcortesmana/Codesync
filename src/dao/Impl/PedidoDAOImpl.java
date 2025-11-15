package dao.Impl;

import models.Pedido;
import models.PedidoLinea;
import models.Cliente;
import models.ClienteEstandar;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dao.PedidoDAO;

public class PedidoDAOImpl implements PedidoDAO {

    private final Connection conexion;

    public PedidoDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public int insertarPedidoConLineas(Pedido pedido, List<PedidoLinea> lineas) throws Exception {

        String callInsertPedido = "{CALL sp_insert_pedido(?, ?, ?, ?, ?)}";
        String callInsertLinea  = "{CALL sp_insert_pedido_articulo(?, ?, ?)}";

        CallableStatement csPedido = null;
        CallableStatement csLinea  = null;

        try {
            conexion.setAutoCommit(false);

            // 1️⃣ cantidad total = suma de cantidades de las líneas
            int cantidadTotal = lineas.stream()
                    .mapToInt(PedidoLinea::getCantidad)
                    .sum();

            // 2️⃣ insertar la cabecera del pedido
            csPedido = conexion.prepareCall(callInsertPedido);
            csPedido.setDate(1, Date.valueOf(pedido.getFechaHora().toLocalDate()));
            csPedido.setInt(2, cantidadTotal);   // ← CORREGIDO
            csPedido.setBoolean(3, pedido.isEnviado());
            csPedido.setInt(4, pedido.getCliente().getId());
            csPedido.registerOutParameter(5, Types.INTEGER);

            csPedido.execute();
            int numeroGenerado = csPedido.getInt(5);

            // 3️⃣ insertar líneas
            csLinea = conexion.prepareCall(callInsertLinea);
            for (PedidoLinea linea : lineas) {
                csLinea.setInt(1, numeroGenerado);
                csLinea.setString(2, linea.getCodigoArticulo());
                csLinea.setInt(3, linea.getCantidad());
                csLinea.execute();
            }

            conexion.commit();
            return numeroGenerado;

        } catch (Exception e) {
            conexion.rollback();
            throw e;

        } finally {
            if (csPedido != null) csPedido.close();
            if (csLinea != null) csLinea.close();
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public Pedido buscarPorNumero(int numero) throws Exception {

        // 1️⃣ primero obtenemos la cabecera del pedido
        String sqlPedido =
                "SELECT p.numero, p.fecha, p.cantidad, p.enviado, c.nombre, c.domicilio, " +
                "c.nif, c.email, c.tipo_cliente " +
                "FROM pedido p JOIN cliente c ON p.id_cliente = c.id_cliente " +
                "WHERE p.numero = ?";

        // 2️⃣ luego obtenemos sus líneas reales
        String sqlLineas =
                "SELECT codigo_articulo, cantidad " +
                "FROM pedido_articulo WHERE id_pedido = ?";

        Pedido pedido = null;

        try {
            // → recuperar cabecera
            try (PreparedStatement ps = conexion.prepareStatement(sqlPedido)) {
                ps.setInt(1, numero);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {

                        Cliente cliente = new ClienteEstandar(
                                rs.getString("nombre"),
                                rs.getString("domicilio"),
                                rs.getString("nif"),
                                rs.getString("email")
                        );

                        pedido = new Pedido(
                                rs.getInt("numero"),
                                cliente,
                                null,    // no usamos este constructor para línea aquí
                                0,
                                rs.getDate("fecha").toLocalDate().atStartOfDay()
                        );

                        pedido.setEnviado(rs.getBoolean("enviado"));
                    }
                }
            }

            if (pedido == null) return null;

            // → recuperar líneas
            try (PreparedStatement ps2 = conexion.prepareStatement(sqlLineas)) {
                ps2.setInt(1, numero);

                try (ResultSet rsLineas = ps2.executeQuery()) {
                    while (rsLineas.next()) {
                        pedido.getLineas().add(
                                new PedidoLinea(
                                        rsLineas.getString("codigo_articulo"),
                                        rsLineas.getInt("cantidad")
                                )
                        );
                    }
                }
            }

            return pedido;

        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public List<Pedido> listarTodos() throws Exception {
        List<Pedido> lista = new ArrayList<>();

        String sql = "SELECT numero FROM pedido";

        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int num = rs.getInt("numero");
                lista.add(buscarPorNumero(num)); // ← reutilizamos método correcto
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Pedido pedido) throws Exception {

        String sql = "UPDATE pedido SET fecha=?, cantidad=?, enviado=?, id_cliente=? WHERE numero=?";

        // cantidad total = suma de cantidades de líneas
        int cantidadTotal = pedido.getLineas().stream()
                .mapToInt(PedidoLinea::getCantidad)
                .sum();

        try {
            conexion.setAutoCommit(false);

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(pedido.getFechaHora().toLocalDate()));
                ps.setInt(2, cantidadTotal);  // CORREGIDO
                ps.setBoolean(3, pedido.isEnviado());
                ps.setInt(4, pedido.getCliente().getId());
                ps.setInt(5, pedido.getNumeroPedido());
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
    public void eliminar(int numero) throws Exception {

        try {
            conexion.setAutoCommit(false);

            // 1️⃣ borrar líneas primero
            try (PreparedStatement ps1 = conexion.prepareStatement(
                    "DELETE FROM pedido_articulo WHERE id_pedido=?")) {
                ps1.setInt(1, numero);
                ps1.executeUpdate();
            }

            // 2️⃣ borrar cabecera
            try (PreparedStatement ps2 = conexion.prepareStatement(
                    "DELETE FROM pedido WHERE numero=?")) {
                ps2.setInt(1, numero);
                ps2.executeUpdate();
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
