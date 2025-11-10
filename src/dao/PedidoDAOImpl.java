package dao;

import models.Pedido;
import models.PedidoLinea;
import models.Cliente;
import models.ClienteEstandar;
import models.Articulo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PedidoDAOImpl adaptado para que la columna 'fecha' en BD sea de tipo DATE.
 * Usa pedido.getFechaHora().toLocalDate() para persistir solo la parte de fecha.
 * Todas las referencias a getNumero() han sido reemplazadas por getNumeroPedido().
 */
public class PedidoDAOImpl implements PedidoDAO {

    private final Connection conexion;

    public PedidoDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Inserta un pedido y sus líneas (pedido_articulo) usando procedimientos almacenados.
     * Retorna el número de pedido generado (AUTO_INCREMENT).
     */
    @Override
    public int insertarPedidoConLineas(Pedido pedido, List<PedidoLinea> lineas) throws Exception {
        String callInsertPedido = "{CALL sp_insert_pedido(?, ?, ?, ?, ?)}"; // OUT p_numero es param 5
        String callInsertLinea = "{CALL sp_insert_pedido_articulo(?, ?, ?)}";
        CallableStatement csPedido = null;
        CallableStatement csLinea = null;

        try {
            conexion.setAutoCommit(false);

            csPedido = conexion.prepareCall(callInsertPedido);
            // La BD tiene columna 'fecha' tipo DATE -> usamos sólo la parte LocalDate
            csPedido.setDate(1, java.sql.Date.valueOf(pedido.getFechaHora().toLocalDate()));
            csPedido.setInt(2, pedido.getCantidad());
            csPedido.setBoolean(3, pedido.isEnviado());
            // Cliente debe tener id establecido (id_cliente en BD)
            csPedido.setInt(4, pedido.getCliente().getId());
            csPedido.registerOutParameter(5, java.sql.Types.INTEGER);

            csPedido.execute();
            int numeroGenerado = csPedido.getInt(5);

            csLinea = conexion.prepareCall(callInsertLinea);
            for (PedidoLinea linea : lineas) {
                csLinea.setInt(1, numeroGenerado);
                csLinea.setString(2, linea.getCodigoArticulo());
                csLinea.setInt(3, linea.getCantidad());
                csLinea.execute();
            }

            conexion.commit();
            return numeroGenerado;
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            if (csLinea != null) try { csLinea.close(); } catch (SQLException ignored) {}
            if (csPedido != null) try { csPedido.close(); } catch (SQLException ignored) {}
            conexion.setAutoCommit(true);
        }
    }

    /**
     * Buscar pedido por número. Recupera datos del pedido y del cliente asociado.
     */
    @Override
    public Pedido buscarPorNumero(int numero) throws Exception {
        String sql = "SELECT p.numero, p.fecha, p.cantidad, p.enviado, p.id_cliente, " +
                "c.nombre, c.domicilio, c.nif, c.email, c.tipo_cliente " +
                "FROM pedido p JOIN cliente c ON p.id_cliente = c.id_cliente WHERE p.numero = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Construimos cliente (simple). Si necesitas datos premium, ajustar.
                    Cliente cliente;
                    String tipo = rs.getString("tipo_cliente");
                    if ("PREMIUM".equalsIgnoreCase(tipo)) {
                        // Si tu app necesita atributos premium completos, deberías leer la tabla cliente_premium
                        cliente = new ClienteEstandar(rs.getString("nombre"),
                                rs.getString("domicilio"),
                                rs.getString("nif"),
                                rs.getString("email"));
                    } else {
                        cliente = new ClienteEstandar(rs.getString("nombre"),
                                rs.getString("domicilio"),
                                rs.getString("nif"),
                                rs.getString("email"));
                    }

                    Pedido pedido = new Pedido(
                            rs.getInt("numero"),
                            cliente,
                            // Creamos un Articulo placeholder con solo el código (detalles pueden venir de articulo DAO)
                            new Articulo("", "", 0.0, 0.0, 0),
                            rs.getInt("cantidad"),
                            rs.getDate("fecha").toLocalDate().atStartOfDay()
                    );
                    pedido.setEnviado(rs.getBoolean("enviado"));
                    return pedido;
                }
            }
        }
        return null;
    }

    /**
     * Lista todos los pedidos (con cliente básico).
     */
    @Override
    public List<Pedido> listarTodos() throws Exception {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.numero, p.fecha, p.cantidad, p.enviado, c.nombre, c.domicilio, c.nif, c.email, c.tipo_cliente " +
                "FROM pedido p JOIN cliente c ON p.id_cliente = c.id_cliente";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new ClienteEstandar(
                        rs.getString("nombre"),
                        rs.getString("domicilio"),
                        rs.getString("nif"),
                        rs.getString("email")
                );

                Pedido pedido = new Pedido(
                        rs.getInt("numero"),
                        cliente,
                        new Articulo("", "", 0.0, 0.0, 0),
                        rs.getInt("cantidad"),
                        rs.getDate("fecha").toLocalDate().atStartOfDay()
                );
                pedido.setEnviado(rs.getBoolean("enviado"));
                lista.add(pedido);
            }
        }
        return lista;
    }

    /**
     * Actualiza fila pedido (columna fecha es DATE, por eso usamos toLocalDate()).
     */
    @Override
    public void actualizar(Pedido pedido) throws Exception {
        String sql = "UPDATE pedido SET fecha=?, cantidad=?, enviado=?, id_cliente=? WHERE numero=?";
        try {
            conexion.setAutoCommit(false);
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setDate(1, java.sql.Date.valueOf(pedido.getFechaHora().toLocalDate()));
                ps.setInt(2, pedido.getCantidad());
                ps.setBoolean(3, pedido.isEnviado());
                ps.setInt(4, pedido.getCliente().getId());
                // Corrección: usar getNumeroPedido()
                ps.setInt(5, pedido.getNumeroPedido());
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

    /**
     * Elimina pedido por número.
     */
    @Override
    public void eliminar(int numero) throws Exception {
        String sql = "DELETE FROM pedido WHERE numero=?";
        try {
            conexion.setAutoCommit(false);
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, numero);
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