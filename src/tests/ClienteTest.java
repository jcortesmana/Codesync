package tests;

import dao.Impl.ClienteDAOImpl;

import models.ClienteEstandar;
import models.ClientePremium;
import utils.ConexionBD;

import java.sql.Connection;

public class ClienteTest {

    public static void main(String[] args) {

        try {
            Connection conexion = ConexionBD.getConnection();
            ClienteDAOImpl clienteDAO = new ClienteDAOImpl(conexion);

            System.out.println("=== TEST CLIENTE ===");

            // INSERTAR ESTÁNDAR
            ClienteEstandar c1 = new ClienteEstandar("Juan", "Calle Uno", "111A", "juan@test.com");
            int id1 = clienteDAO.insertar(c1);
            System.out.println("Insertado estándar → ID " + id1);

            // INSERTAR PREMIUM
            ClientePremium c2 = new ClientePremium("Laura", "Calle Dos", "222B", "laura@test.com");
            c2.setCuotaAnual(120);
            c2.setDescuento(15);
            int id2 = clienteDAO.insertar(c2);
            System.out.println("Insertado premium → ID " + id2);

            // BUSCAR
            System.out.println("\nBuscar juan@test.com → " + clienteDAO.buscarPorEmail("juan@test.com"));
            System.out.println("Buscar laura@test.com → " + clienteDAO.buscarPorEmail("laura@test.com"));

            // LISTAR TODOS
            System.out.println("\nLista completa:");
            clienteDAO.listarTodos().forEach(System.out::println);

            // ELIMINAR
            clienteDAO.eliminar(id1);
            clienteDAO.eliminar(id2);

            System.out.println("\nTras eliminar:");
            clienteDAO.listarTodos().forEach(System.out::println);

            System.out.println("\n=== FIN TEST CLIENTE ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
