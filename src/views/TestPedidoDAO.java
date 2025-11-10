package views;

import factory.DAOFactory;
import dao.PedidoDAO;
import dao.ClienteDAO;
import models.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestPedidoDAO {
    public static void main(String[] args) {
        try {
            PedidoDAO pedidoDAO = DAOFactory.getPedidoDAO();
            ClienteDAO clienteDAO = DAOFactory.getClienteDAO();

            // Buscar cliente existente (asegúrate de tenerlo con TestClienteDAO)
            Cliente cliente = clienteDAO.buscarPorEmail("juan@ej.com");
            if (cliente == null) {
                System.err.println("Crea primero un cliente con TestClienteDAO");
                return;
            }

            // Crear artículo de prueba (si no existe en BD puedes crearlo con ArticuloDAO o insertar manualmente)
            Articulo articulo = new Articulo("A01", "Mancuerna 5kg", 15.0, 3.0, 2);

            // Crear Pedido: usar constructor sin número (se generará en BD)
            Pedido pedido = new Pedido(cliente, articulo, 2, LocalDateTime.now());

            // Crear líneas del pedido
            List<PedidoLinea> lineas = new ArrayList<>();
            lineas.add(new PedidoLinea("A01", 1));
            // si quieres añadir otra línea:
            // lineas.add(new PedidoLinea("A02", 1));

            // Insertar pedido con líneas (devuelve el número generado)
            int numeroGenerado = pedidoDAO.insertarPedidoConLineas(pedido, lineas);
            System.out.println("Pedido insertado con número = " + numeroGenerado);

            // Mostrar todos los pedidos para verificar
            List<Pedido> pedidos = pedidoDAO.listarTodos();
            pedidos.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}