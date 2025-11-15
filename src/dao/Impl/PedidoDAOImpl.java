package dao.Impl;

import models.Pedido;
import models.PedidoLinea;
import models.Cliente;
import models.ClienteEstandar;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.PedidoDAO;

public class PedidoDAOImpl implements PedidoDAO {

    private final Connection conexion;

    public PedidoDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    // ===============================================================
    // INSERTAR PEDIDO + LÍNEAS
    // ===============================================================
    @Override
    public String insertarPedidoConLineas(Pedido pedido, List<PedidoLinea> lineas) throws Exception {

        String callInsertPedido = "{CALL sp_insert_pedido(?, ?, ?, ?, ?)}";
        String callInsertLinea  = "{CALL sp_insert_pedido_articulo(?, ?, ?)}";

        CallableStatement csPedido = null;
        CallableStatement csLinea  = null;

        try {
            conexion.setAutoCommit(false);

            int cantidadTotal = lineas.stream()
                    .mapToInt(PedidoLinea::getCantidad)
                    .sum();

            csPedido = conexion.prepareCall(callInsertPedido);
            csPedido.setDate(1, Date.valueOf(pedido.getFechaHora().toLocalDate()));
            csPedido.setInt(2, cantidadTotal);
            csPedido.setBoolean(3, pedido.isEnviado());
            csPedido.setInt(4, pedido.getCliente().getId());

            csPedido.registerOutParameter(5, Types.VARCHAR);
            csPedido.execute();

            String numeroGenerado = csPedido.getString(5);

            // insertar líneas
            csLinea = conexion.prepareCall(callInsertLinea);
            for (PedidoLinea linea : lineas) {

                csLinea.setString(1, numeroGenerado);
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

    // ===============================================================
    // BUSCAR POR NÚMERO
    // ===============================================================
    @Override
    public Pedido buscarPorNumero(String numero) throws Exception {

        String sqlPedido =
                "SELECT p.numero, p.fecha, p.cantidad, p.enviado, c.nombre, c.domicilio, " +
                "c.nif, c.email " +
                "FROM pedido p JOIN cliente c ON p.id_cliente = c.id_cliente " +
                "WHERE p.numero = ?";

        String sqlLineas =
                "SELECT codigo_articulo, cantidad FROM pedido_articulo WHERE id_pedido = ?";

        Pedido pedido = null;

        try (PreparedStatement ps = conexion.prepareStatement(sqlPedido)) {

            ps.setString(1, numero);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    Cliente cliente = new ClienteEstandar(
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getString("email")
                    );

                    // CONSTRUCTOR CORRECTO
                    LocalDateTime fecha = rs.getDate("fecha").toLocalDate().atStartOfDay();

                    pedido = new Pedido(
                            rs.getString("numero"),
                            cliente,
                            fecha
                    );

                    pedido.setEnviado(rs.getBoolean("enviado"));
                }
            }
        }

        if (pedido == null) return null;

        // Recuperar líneas
        try (PreparedStatement ps2 = conexion.prepareStatement(sqlLineas)) {

            ps2.setString(1, numero);

            try (ResultSet rs = ps2.executeQuery()) {
                while (rs.next()) {
                    pedido.getLineas().add(
                            new PedidoLinea(
                                    rs.getString("codigo_articulo"),
                                    rs.getInt("cantidad")
                            )
                    );
                }
            }
        }

        return pedido;
    }


    // ===============================================================
    // LISTAR TODOS
    // ===============================================================
    @Override
    public List<Pedido> listarTodos() throws Exception {

        List<Pedido> lista = new ArrayList<>();

        String sql = "SELECT numero FROM pedido";

        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add( buscarPorNumero(rs.getString("numero")) );
            }
        }

        return lista;
    }

    // ===============================================================
    // ACTUALIZAR
    // ===============================================================
    @Override
    public void actualizar(Pedido pedido) throws Exception {

        String sql = "UPDATE pedido SET fecha=?, cantidad=?, enviado=?, id_cliente=? WHERE numero=?";

        int cantidadTotal = pedido.getLineas().stream()
                .mapToInt(PedidoLinea::getCantidad)
                .sum();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(pedido.getFechaHora().toLocalDate()));
            ps.setInt(2, cantidadTotal);
            ps.setBoolean(3, pedido.isEnviado());
            ps.setInt(4, pedido.getCliente().getId());
            ps.setString(5, pedido.getNumeroPedido());

            ps.executeUpdate();
        }
    }

    // ===============================================================
    // ELIMINAR
    // ===============================================================
    @Override
    public void eliminar(String numero) throws Exception {

        try {
            conexion.setAutoCommit(false);

            try (PreparedStatement ps1 =
                         conexion.prepareStatement("DELETE FROM pedido_articulo WHERE id_pedido=?")) {
                ps1.setString(1, numero);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 =
                         conexion.prepareStatement("DELETE FROM pedido WHERE numero=?")) {
                ps2.setString(1, numero);
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

        // ===============================================================
    // LISTAR PEDIDOS DETALLADOS (pedido + cliente + artículo)
    // ===============================================================
    @Override
    public List<String[]> listarPedidosDetallados() throws Exception {

        String sql = """
                SELECT p.numero, c.nombre, a.descripcion
                FROM pedido p
                JOIN cliente c ON p.id_cliente = c.id_cliente
                JOIN pedido_articulo pa ON p.numero = pa.id_pedido
                JOIN articulo a ON pa.codigo_articulo = a.codigo
                ORDER BY p.numero;
                """;

        List<String[]> lista = new ArrayList<>();

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("numero"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                });
            }
        }

        return lista;
    }


    // ===============================================================
    // LISTAR PEDIDOS POR EMAIL (pedido + cliente + artículo)
    // ===============================================================
    @Override
    public List<String[]> listarPedidosPorEmail(String email) throws Exception {

        String sql = """
                SELECT p.numero, c.nombre, a.descripcion
                FROM pedido p
                JOIN cliente c ON p.id_cliente = c.id_cliente
                JOIN pedido_articulo pa ON p.numero = pa.id_pedido
                JOIN articulo a ON pa.codigo_articulo = a.codigo
                WHERE c.email = ?
                ORDER BY p.numero;
                """;

        List<String[]> lista = new ArrayList<>();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new String[]{
                            rs.getString("numero"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    });
                }
            }
        }

        return lista;
    }
}
