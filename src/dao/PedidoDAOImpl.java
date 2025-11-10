package dao;

import models.Pedido;
import models.Articulo;
import models.Cliente;
import models.ClienteEstandar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    private final Connection conexion;

    public PedidoDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void insertar(Pedido pedido) throws Exception {
        String sql = "INSERT INTO pedidos (numero_pedido, cliente_email, articulo_codigo, cantidad, fecha_hora) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, pedido.getNumeroPedido());
            ps.setString(2, pedido.getCliente().getEmail());
            ps.setString(3, pedido.getArticulo().getCodigo());
            ps.setInt(4, pedido.getCantidad());
            ps.setTimestamp(5, Timestamp.valueOf(pedido.getFechaHora()));
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(Pedido pedido) throws Exception {
        String sql = "UPDATE pedidos SET cliente_email=?, articulo_codigo=?, cantidad=?, fecha_hora=? WHERE numero_pedido=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, pedido.getCliente().getEmail());
            ps.setString(2, pedido.getArticulo().getCodigo());
            ps.setInt(3, pedido.getCantidad());
            ps.setTimestamp(4, Timestamp.valueOf(pedido.getFechaHora()));
            ps.setInt(5, pedido.getNumeroPedido());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int numeroPedido) throws Exception {
        String sql = "DELETE FROM pedidos WHERE numero_pedido=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, numeroPedido);
            ps.executeUpdate();
        }
    }

    @Override
    public Pedido buscarPorNumero(int numeroPedido) throws Exception {
        String sql = "SELECT * FROM pedidos WHERE numero_pedido=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, numeroPedido);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Creamos cliente genérico con datos mínimos (no podemos instanciar Cliente directamente)
                Cliente cliente = new ClienteEstandar("Desconocido", "", "", rs.getString("cliente_email"));

                // Creamos artículo básico con el código
                Articulo articulo = new Articulo(rs.getString("articulo_codigo"), "", 0.0, 0.0, 0);

                // Creamos y devolvemos el pedido
                return new Pedido(
                        rs.getInt("numero_pedido"),
                        cliente,
                        articulo,
                        rs.getInt("cantidad"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime()
                );
            }
        }
        return null;
    }

    @Override
    public List<Pedido> listarTodos() throws Exception {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // Cliente temporal (estándar) con solo el email
                Cliente cliente = new ClienteEstandar("Desconocido", "", "", rs.getString("cliente_email"));

                // Artículo básico con solo el código
                Articulo articulo = new Articulo(rs.getString("articulo_codigo"), "", 0.0, 0.0, 0);

                Pedido pedido = new Pedido(
                        rs.getInt("numero_pedido"),
                        cliente,
                        articulo,
                        rs.getInt("cantidad"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime()
                );

                lista.add(pedido);
            }
        }
        return lista;
    }
}