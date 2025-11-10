package views;

import factory.DAOFactory;
import dao.PedidoDAO;
import dao.ClienteDAO;
import models.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestPedidoDAO {
    public static void main(String[] args) {
        try {
            PedidoDAO pedidoDAO = DAOFactory.getPedidoDAO();
            ClienteDAO clienteDAO = DAOFactory.getClienteDAO();

            // Asegúrate de tener un cliente con id (usa TestClienteDAO)
            // Ejemplo: buscar cliente por email para obtener id
            Cliente cliente = clienteDAO.buscarPorEmail("juan@ej.com");
            if (cliente == null) {
                System.err.println("Crea primero un cliente con TestClienteDAO");
                return;
            }

            Pedido p = new Pedido(LocalDate.now(), 2, false, cliente);

            List<PedidoLinea> lineas = new ArrayList<>();
            lineas.add(new PedidoLinea("A01", 1));
            lineas.add(new PedidoLinea("A02", 1));

            int numero = pedidoDAO.insertarPedidoConLineas(p, lineas);
            System.out.println("Pedido insertado con numero=" + numero);

            List<Pedido> pedidos = pedidoDAO.listarTodos();
            pedidos.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}