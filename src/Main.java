
import java.time.LocalDateTime;
import controllers.*;
import models.*;
import exceptions.*;
public class Main {
    public static void main(String[] args) {
  
        Tienda tienda = new Tienda();
     ControladorTienda controlador = new ControladorTienda(tienda);

 
        try {
            tienda.agregarCliente(new ClienteEstandar("Juan Pérez","Calle Mayor 123, Madrid","12345678A","juanperez@email.com"));
            tienda.agregarCliente(new ClientePremium("María López","Avenida Central 45, Barcelona","87654321B","marialopez@email.com"));
        } catch (ClienteDuplicadoException e) {
            System.out.println("Error al añadir cliente de ejemplo: " + e.getMessage());
        }

        try {
            tienda.agregarArticulo(new Articulo("A01", "Mancuerna de 5kg", 15.0, 3.5, 2));
            tienda.agregarArticulo(new Articulo("A02", "Colchoneta antideslizante", 10.5, 2.0, 1));
        } catch (ArticuloDuplicadoException e) {
            System.out.println("Error al añadir artículo de ejemplo: " + e.getMessage());
        }


        Gestor<Cliente> gestorClientes = new Gestor<>();
        gestorClientes.agregar(tienda.buscarCliente("juanperez@email.com"));
        gestorClientes.agregar(tienda.buscarCliente("marialopez@email.com"));

        System.out.println("=== Lista de Clientes desde Gestor ===");
        gestorClientes.mostrarTodos();


        try {
            Pedido p1 = new Pedido(1, tienda.buscarCliente("juanperez@email.com"),
                    tienda.buscarArticulo("A01"), 2, LocalDateTime.now());
            tienda.agregarPedido(p1);

            Pedido p2 = new Pedido(2, tienda.buscarCliente("marialopez@email.com"),
                    tienda.buscarArticulo("A02"), 1, LocalDateTime.now().plusHours(2));
            tienda.agregarPedido(p2);
        } catch (NullPointerException npe) {
            System.out.println("Error creando pedidos de ejemplo: faltan cliente o artículo.");
        }

        System.out.println("\n=== Pedidos en la tienda ===");
        tienda.listarPedidosPendientes(null);


        controlador.iniciar();
    }
}