import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        // =============================
        // Inicio de la aplicación Codesync
        // =============================

        // 1️⃣ Crear la tienda (modelo)
        Tienda tienda = new Tienda();

        // 2️⃣ Crear el controlador (gestiona la lógica y la vista)
        ControladorTienda controlador = new ControladorTienda(tienda);

        // 3️⃣ Iniciar la aplicación (mostrará el menú en consola)
        controlador.iniciar();

        // =============================
        // Uso de clase genérica Gestor<T>
        // =============================

        Gestor<Cliente> gestorClientes = new Gestor<>();
        Cliente c1 = new ClienteEstandar("Juan Pérez","Calle Mayor 123, Madrid","12345678A","juanperez@email.com");
        Cliente c2 = new ClientePremium("María López","Avenida Central 45, Barcelona","87654321B","marialopez@email.com");
        gestorClientes.agregar(c1);
        gestorClientes.agregar(c2); 
        System.out.println("=== Lista de Clientes ===");
        gestorClientes.mostrarTodos();


        System.out.println("=== Lista de Clientes ===");
        gestorClientes.mostrarTodos();
        Gestor<Articulo> gestorArticulos = new Gestor<>();
        Articulo a1 = new Articulo("A01", "Mancuerna de 5kg", 15.0, 3.5, 2);
        Articulo a2 = new Articulo("A02", "Colchoneta antideslizante", 10.5, 2.0, 1);
        gestorArticulos.agregar(a1);
        gestorArticulos.agregar(a2);
        System.out.println("\n=== Lista de Artículos ===");
        gestorArticulos.mostrarTodos();

        // ---- Pedidos ----
        Gestor<Pedido> gestorPedidos = new Gestor<>();
        Pedido p1 = new Pedido(1,c1,a1,2,LocalDateTime.now());
        Pedido p2 = new Pedido(2,c2,a2, 1,LocalDateTime.now().plusHours(2));
        gestorPedidos.agregar(p1);
        gestorPedidos.agregar(p2);
        System.out.println("\n=== Lista de Pedidos ===");
        gestorPedidos.mostrarTodos();
        System.out.println("\n=== Lista de Pedidos ===");
        gestorPedidos.mostrarTodos();

        // Ejemplo de búsqueda
        System.out.println("\nCliente en posición 1: " + gestorClientes.getPorIndice(1));
        System.out.println("¿El cliente Juan Pérez está en la lista? " + gestorClientes.contiene(c1));
    }
}
