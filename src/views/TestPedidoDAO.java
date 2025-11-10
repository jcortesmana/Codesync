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

            // Buscar cliente existente
            Cliente cliente = clienteDAO.buscarPorEmail("juan@ej.com");
            if (cliente == null) {
                System.err.println("Crea primero un cliente con TestClienteDAO");
                return;
            }

            // Crear artículo de prueba (si no existe, añádelo manualmente o con ArticuloDAO)
            Articulo articulo = new Articulo("A01", "Mancuerna 5kg", 15.0, 3.0, 2);

            // Crear Pedido (sin número; se generará en BD)
            Pedido pedido = new Pedido(LocalDate.now(), 2, false, cliente);

            // Crear líneas del pedido
            List<PedidoLinea> lineas = new ArrayList<>();
            lineas.add(new PedidoLinea("A01", 2));

            int numeroGenerado = pedidoDAO.insertarPedidoConLineas(pedido, lineas);
            System.out.println("Pedido insertado con número = " + numeroGenerado);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}