package views;

import factory.DAOFactory;
import dao.PedidoDAO;
import models.*;
import java.time.LocalDateTime;
import java.util.List;

public class TestPedidoDAO {
    public static void main(String[] args) {
        try {
            PedidoDAO pedidoDAO = DAOFactory.getPedidoDAO();

            System.out.println("=== 🔹 TEST PedidoDAO ===");

            // Crear cliente y artículo de prueba
            Cliente cliente = new ClienteEstandar("Prueba", "Calle Falsa 123", "11111111A", "cliente@demo.com");
            Articulo articulo = new Articulo("A01", "Mancuerna 5kg", 15.0, 3.0, 2);

            // Crear pedido
            Pedido pedido = new Pedido(1, cliente, articulo, 2, LocalDateTime.now());
            pedidoDAO.insertar(pedido);
            System.out.println("✅ Pedido insertado con número: " + pedido.getNumeroPedido());

            // Listar todos los pedidos
            List<Pedido> pedidos = pedidoDAO.listarTodos();
            System.out.println("\n📋 Pedidos en BD:");
            pedidos.forEach(System.out::println);

            // Buscar pedido por número
            Pedido encontrado = pedidoDAO.buscarPorNumero(1);
            if (encontrado != null) {
                System.out.println("\n🔍 Pedido encontrado: " + encontrado);
            }

            // Actualizar cantidad
            if (encontrado != null) {
                encontrado = new Pedido(1, cliente, articulo, 5, LocalDateTime.now());
                pedidoDAO.actualizar(encontrado);
                System.out.println("\n✏️ Pedido actualizado.");
            }

            // Eliminar pedido
            pedidoDAO.eliminar(1);
            System.out.println("\n🗑️ Pedido eliminado.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}