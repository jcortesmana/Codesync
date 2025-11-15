package tests;

import dao.Impl.ArticuloDAOImpl;
import dao.Impl.ClienteDAOImpl;
import dao.Impl.PedidoDAOImpl;

import models.*;
import utils.ConexionBD;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoTest {

    public static void main(String[] args) {
        try {
            Connection conexion = ConexionBD.getConnection();

            ArticuloDAOImpl articuloDAO = new ArticuloDAOImpl(conexion);
            ClienteDAOImpl clienteDAO = new ClienteDAOImpl(conexion);
            PedidoDAOImpl pedidoDAO = new PedidoDAOImpl(conexion);

            Tienda tienda = new Tienda(); // Solo para buscar artículos al imprimir

            System.out.println("=== TEST PEDIDO ===");

            // ===============================================
            // INSERTAR CLIENTE
            // ===============================================
            ClienteEstandar c1 =
                    new ClienteEstandar("Joaquin", "Calle 3", "335C", "mario_test1@test.com");

            int idC = clienteDAO.insertar(c1);
            c1.setId(idC);

            // ===============================================
            // INSERTAR ARTÍCULOS
            // ===============================================
            Articulo a1 = new Articulo("PX10", "Pantalla 245\"", 120, 7, 3);
            Articulo a2 = new Articulo("PX20", "Altavoces", 35, 5, 1);

            articuloDAO.insertar(a1);
            articuloDAO.insertar(a2);

            // ===============================================
            // CREAR PEDIDO
            // ===============================================
            Pedido pedido = new Pedido("PED_TEST_01", c1, LocalDateTime.now(), tienda);
            pedido.addLinea(a1, 1);
            pedido.addLinea(a2, 2);

            String generado = pedidoDAO.insertarPedidoConLineas(pedido, pedido.getLineas());

            System.out.println("Pedido creado → " + generado);

            // ===============================================
            // BUSCAR PEDIDO
            // ===============================================
            Pedido buscado = pedidoDAO.buscarPorNumero(generado);

            if (buscado == null) {
                System.out.println("ERROR: pedido no encontrado en BD");
                return;
            }

            buscado.setTienda(tienda);
            System.out.println("Encontrado → " + buscado);

            // ===============================================
            // LISTAR TODOS
            // ===============================================
            System.out.println("\nLista pedidos:");

            List<Pedido> pedidos = pedidoDAO.listarTodos();

            for (Pedido p : pedidos) {
                p.setTienda(tienda);
                System.out.println(p);
            }

            System.out.println("\n=== FIN TEST PEDIDO ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
