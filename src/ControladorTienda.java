import java.time.LocalDateTime;
import java.util.Scanner;

public class ControladorTienda {
    private Tienda tienda;
    private Scanner sc;

    public ControladorTienda(Tienda tienda) {
        this.tienda = tienda;
        sc = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1 -> gestionarArticulos();
                    case 2 -> gestionarClientes();
                    case 3 -> gestionarPedidos();
                    case 0 -> System.out.println("Saliendo de la aplicación...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
                opcion = -1;
            }
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("\n===== TIENDA ONLINE =====");
        System.out.println("1. Gestión de Artículos");
        System.out.println("2. Gestión de Clientes");
        System.out.println("3. Gestión de Pedidos");
        System.out.println("0. Salir");
        System.out.print("Elija una opción: ");
    }

    // =========================
    // Gestión de Artículos
    // =========================
    private void gestionarArticulos() {
        System.out.println("\n--- Gestión de Artículos ---");
        System.out.println("1. Añadir Artículo");
        System.out.println("2. Mostrar Artículos");
        System.out.print("Opción: ");
        String op = sc.nextLine();
        switch (op) {
            case "1" -> agregarArticulo();
            case "2" -> tienda.mostrarArticulos();
            default -> System.out.println("Opción inválida.");
        }
    }

    private void agregarArticulo() {
        try {
            System.out.print("Código: ");
            String codigo = sc.nextLine();
            System.out.print("Descripción: ");
            String desc = sc.nextLine();
            System.out.print("Precio de venta: ");
            double precio = Double.parseDouble(sc.nextLine());
            System.out.print("Gastos de envío: ");
            double envio = Double.parseDouble(sc.nextLine());
            System.out.print("Tiempo preparación (minutos): ");
            int tiempo = Integer.parseInt(sc.nextLine());

            Articulo a = new Articulo(codigo, desc, precio, envio, tiempo);
            tienda.agregarArticulo(a);
            System.out.println("Artículo agregado correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("Datos inválidos, no se pudo agregar el artículo.");
        }
    }

    // =========================
    // Gestión de Clientes
    // =========================
    private void gestionarClientes() {
        System.out.println("\n--- Gestión de Clientes ---");
        System.out.println("1. Añadir Cliente");
        System.out.println("2. Mostrar Clientes");
        System.out.println("3. Mostrar Clientes Estándar");
        System.out.println("4. Mostrar Clientes Premium");
        System.out.print("Opción: ");
        String op = sc.nextLine();
        switch (op) {
            case "1" -> agregarCliente();
            case "2" -> tienda.mostrarClientes();
            case "3" -> tienda.mostrarClientesEstandar();
            case "4" -> tienda.mostrarClientesPremium();
            default -> System.out.println("Opción inválida.");
        }
    }

    private void agregarCliente() {
        try {
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Domicilio: ");
            String dom = sc.nextLine();
            System.out.print("NIF: ");
            String nif = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Tipo (1 = Estándar, 2 = Premium): ");
            int tipo = Integer.parseInt(sc.nextLine());
            Cliente c;
            if (tipo == 1) {
                c = new ClienteEstandar(nombre, dom, nif, email);
            } else {
                c = new ClientePremium(nombre, dom, nif, email);
            }
            tienda.agregarCliente(c);
            System.out.println("Cliente agregado correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("Tipo de cliente inválido.");
        }
    }

    // =========================
    // Gestión de Pedidos
    // =========================
    private void gestionarPedidos() {
        System.out.println("\n--- Gestión de Pedidos ---");
        System.out.println("1. Añadir Pedido");
        System.out.println("2. Eliminar Pedido");
        System.out.println("3. Mostrar Pedidos Pendientes");
        System.out.println("4. Mostrar Pedidos Enviados");
        System.out.print("Opción: ");
        String op = sc.nextLine();
        switch (op) {
            case "1" -> agregarPedido();
            case "2" -> eliminarPedido();
            case "3" -> mostrarPedidosPendientes();
            case "4" -> mostrarPedidosEnviados();
            default -> System.out.println("Opción inválida.");
        }
    }

    private void agregarPedido() {
        try {
            System.out.print("Número de pedido: ");
            int num = Integer.parseInt(sc.nextLine());
            System.out.print("Email del cliente: ");
            String email = sc.nextLine();
            Cliente c = tienda.buscarCliente(email);
            if (c == null) {
                System.out.println("Cliente no existe. Debe registrarlo primero:");
                agregarCliente();
                c = tienda.buscarCliente(email);
            }
            System.out.print("Código del artículo: ");
            String cod = sc.nextLine();
            Articulo a = tienda.buscarArticulo(cod);
            if (a == null) {
                System.out.println("Artículo no encontrado. No se puede crear el pedido.");
                return;
            }
            System.out.print("Cantidad: ");
            int cantidad = Integer.parseInt(sc.nextLine());

            Pedido p = new Pedido(num, c, a, cantidad, LocalDateTime.now());
            tienda.agregarPedido(p);
            System.out.println("Pedido agregado correctamente. Total: " + p.calcularTotal());
        } catch (NumberFormatException e) {
            System.out.println("Datos inválidos para crear el pedido.");
        }
    }

    private void eliminarPedido() {
        try {
            System.out.print("Número de pedido a eliminar: ");
            int num = Integer.parseInt(sc.nextLine());
            tienda.eliminarPedido(num);
            System.out.println("Pedido eliminado si era cancelable.");
        } catch (NumberFormatException e) {
            System.out.println("Número de pedido inválido.");
        }
    }

    private void mostrarPedidosPendientes() {
        System.out.print("Filtrar por cliente (enter = todos): ");
        String email = sc.nextLine();
        if (email.isBlank()) email = null;
        tienda.listarPedidosPendientes(email);
    }

    private void mostrarPedidosEnviados() {
        System.out.print("Filtrar por cliente (enter = todos): ");
        String email = sc.nextLine();
        if (email.isBlank()) email = null;
        tienda.listarPedidosEnviados(email);
    }
}