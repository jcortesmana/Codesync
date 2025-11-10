package views;

import factory.DAOFactory;
import dao.ClienteDAO;
import models.Cliente;
import models.ClienteEstandar;
import models.ClientePremium;

import java.util.List;

public class TestClienteDAO {
    public static void main(String[] args) {
        try {
            ClienteDAO clienteDAO = DAOFactory.getClienteDAO();

            System.out.println("=== 🔹 TEST ClienteDAO ===");

            // Insertar clientes
            Cliente c1 = new ClienteEstandar("Juan Pérez", "Calle Mayor 123", "12345678A", "juan@email.com");
            Cliente c2 = new ClientePremium("María López", "Av. Central 45", "87654321B", "maria@email.com");

            clienteDAO.insertar(c1);
            clienteDAO.insertar(c2);
            System.out.println("✅ Clientes insertados.");

            // Listar todos
            List<Cliente> clientes = clienteDAO.listarTodos();
            System.out.println("\n📋 Clientes en BD:");
            clientes.forEach(System.out::println);

            // Buscar cliente por email
            Cliente encontrado = clienteDAO.buscarPorEmail("juan@email.com");
            if (encontrado != null) {
                System.out.println("\n🔍 Cliente encontrado: " + encontrado);
            }

            // Actualizar cliente
            if (encontrado != null) {
                encontrado.setDomicilio("Calle Nueva 99");
                clienteDAO.actualizar(encontrado);
                System.out.println("\n✏️ Cliente actualizado.");
            }

            // Eliminar cliente
            clienteDAO.eliminar("maria@email.com");
            System.out.println("\n🗑️ Cliente eliminado.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}