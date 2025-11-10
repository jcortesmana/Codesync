package views;

import factory.DAOFactory;
import dao.ClienteDAO;
import models.Cliente;
import models.ClienteEstandar;
import models.ClientePremium;

import java.math.BigDecimal;
import java.util.List;

public class TestClienteDAO {
    public static void main(String[] args) {
        try {
            ClienteDAO dao = DAOFactory.getClienteDAO();

            // Insertar cliente estándar (usando SP)
            Cliente c1 = new ClienteEstandar("Juan Perez", "C/ Mayor 1", "12345678A", "juan@ej.com");
            int id1 = dao.insertarConSP(c1, BigDecimal.ZERO, BigDecimal.ZERO);
            System.out.println("Insertado cliente ESTANDAR id=" + id1);

            // Insertar cliente premium (SP + premium)
            ClientePremium cp = new ClientePremium("Maria Premium", "Av. Central 5", "87654321B", "maria@ej.com");
            int id2 = dao.insertarConSP(cp, new BigDecimal("30.00"), new BigDecimal("0.20"));
            System.out.println("Insertado cliente PREMIUM id=" + id2);

            // Listar
            List<Cliente> lista = dao.listarTodos();
            lista.forEach(System.out::println);

            // Buscar
            Cliente encontrado = dao.buscarPorEmail("juan@ej.com");
            System.out.println("Encontrado: " + encontrado);

            // Actualizar
            if (encontrado != null) {
                encontrado.setDomicilio("C/ Nueva 99");
                dao.actualizar(encontrado);
                System.out.println("Actualizado");
            }

            // Eliminar (ejemplo)
            // dao.eliminar(id1);
            // System.out.println("Eliminado id=" + id1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}