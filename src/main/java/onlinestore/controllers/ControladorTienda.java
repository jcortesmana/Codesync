package onlinestore.controllers;

import java.time.LocalDateTime;
import java.util.Scanner;

import onlinestore.models.*;
import onlinestore.exceptions.*;

import onlinestore.dao.ClienteDAO;
import onlinestore.dao.jpa.ClienteDAOImpl;

public class ControladorTienda {

    private Tienda tienda;
    private Scanner sc;
    private ClienteDAO clienteDAO;

    public ControladorTienda(Tienda tienda) {
        this.tienda = tienda;
        sc = new Scanner(System.in);

        try {
            // INICIALIZACIÓN JPA — SIN CONNECTION
            clienteDAO = new ClienteDAOImpl();

            System.out.println("✅ JPA inicializado correctamente.");

            clienteDAO.listarTodos().forEach(c -> {
                try { tienda.agregarCliente(c); }
                catch (ClienteDuplicadoException ignored) {}
            });

            System.out.println("✅ Clientes cargados desde BD.");

        } catch (Exception e) {
            System.err.println("❌ Error al iniciar JPA: " + e.getMessage());
        }
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
                    case 0 -> System.out.println("Saliendo...");
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
        System.out.print("Opción: ");
    }

    // =============================================================
    // ARTÍCULOS
    // =============================================================
    private void gestionarArticulos() {
        System.out.println("\n--- Gestión de Artículos ---");
        System.out.println("1. Añadir Artículo");
        System.out.println("2. Mostrar Artículos");
        System.out.println("3. Eliminar Artículo");
        System.out.print("Opción: ");

        switch (sc.nextLine()) {
            case "1" -> agregarArticulo();
            case "2" -> tienda.mostrarArticulos();
            case "3" -> eliminarArticulo();
            default -> System.out.println("Opción inválida.");
        }
    }

    private void agregarArticulo() {
        try {
            System.out.print("Código: ");
            String codigo = sc.nextLine();

            System.out.print("Descripción: ");
            String desc = sc.nextLine();

            System.out.print("Precio: ");
            double precio = Double.parseDouble(sc.nextLine());

            System.out.print("Gastos envío: ");
            double envio = Double.parseDouble(sc.nextLine());

            System.out.print("Tiempo preparación: ");
            int tiempo = Integer.parseInt(sc.nextLine());

            tienda.agregarArticulo(new Articulo(codigo, desc, precio, envio, tiempo));

            System.out.println("✔ Artículo agregado.");

        } catch (Exception e) {
            System.out.println("⚠ Error: " + e.getMessage());
        }
    }

    private void eliminarArticulo() {
        try {
            System.out.print("Código del artículo a eliminar: ");
            String codigo = sc.nextLine().trim();

            if (tienda.buscarArticulo(codigo) == null) {
                System.out.println("⚠️ El artículo no existe.");
                return;
            }

            tienda.eliminarArticulo(codigo);
            System.out.println("✔ Artículo eliminado.");

        } catch (Exception e) {
            System.out.println("⚠ Error eliminando artículo: " + e.getMessage());
        }
    }

    // =============================================================
    // CLIENTES
    // =============================================================
    private void gestionarClientes() {
        System.out.println("\n--- Gestión de Clientes ---");
        System.out.println("1. Añadir Cliente");
        System.out.println("2. Mostrar Clientes");
        System.out.println("3. Mostrar Estándar");
        System.out.println("4. Mostrar Premium");
        System.out.print("Opción: ");

        switch (sc.nextLine()) {
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
            String email = sc.nextLine().trim().toLowerCase();

            System.out.print("Tipo (1 = Estándar, 2 = Premium): ");
            int tipo = Integer.parseInt(sc.nextLine());

            if (tienda.buscarCliente(email) != null) {
                System.out.println("⚠ Ese email ya existe.");
                return;
            }

            Cliente cliente;

            if (tipo == 1) {
                cliente = new ClienteEstandar(nombre, dom, nif, email);
            } else {
                System.out.print("Cuota anual: ");
                double cuota = Double.parseDouble(sc.nextLine());

                System.out.print("Descuento: ");
                double desc = Double.parseDouble(sc.nextLine());

                ClientePremium cp = new ClientePremium(nombre, dom, nif, email);
                cp.setCuotaAnual(cuota);
                cp.setDescuento(desc);

                cliente = cp;
            }

            clienteDAO.insertar(cliente); 
            tienda.agregarCliente(cliente);

        } catch (Exception e) {
            System.out.println("⚠ Error añadiendo cliente: " + e.getMessage());
        }
    }

    // =============================================================
    // PEDIDOS
    // =============================================================
    private void gestionarPedidos() {
        System.out.println("\n--- Gestión de Pedidos ---");
        System.out.println("1. Añadir Pedido");
        System.out.println("2. Eliminar Pedido");
        System.out.println("3. Mostrar Todos");
        System.out.println("4. Mostrar por Cliente");
        System.out.print("Opción: ");

        switch (sc.nextLine()) {
            case "1" -> agregarPedido();
            case "2" -> eliminarPedido();
            case "3" -> tienda.mostrarPedidos();
            case "4" -> mostrarPedidosCliente();
            default -> System.out.println("Opción inválida.");
        }
    }

    private void agregarPedido() {
        try {
            System.out.print("Email cliente: ");
            String email = sc.nextLine().trim();

            Cliente cliente = tienda.buscarCliente(email);

            if (cliente == null) {
                System.out.println("⚠ Cliente no encontrado.");
                return;
            }

            LocalDateTime fechaHora = LocalDateTime.now();
            Pedido pedido = new Pedido(email, cliente, fechaHora);
            pedido.setTienda(tienda);

            while (true) {
                System.out.print("Código de artículo: ");
                String codigo = sc.nextLine().trim();

                Articulo art = tienda.buscarArticulo(codigo);
                if (art == null) {
                    System.out.println("⚠ El artículo no existe.");
                    continue;
                }

                System.out.print("Cantidad: ");
                int cantidad = Integer.parseInt(sc.nextLine());

                pedido.addLinea(art, cantidad);
                System.out.println("✔ Línea añadida.");

                System.out.print("¿Añadir otra? (s/n): ");
                if (!sc.nextLine().trim().equalsIgnoreCase("s"))
                    break;
            }

            tienda.agregarPedido(pedido);

            System.out.println("✔ Pedido creado.");
            System.out.println("TOTAL: " + pedido.calcularTotal() + " €");

        } catch (Exception e) {
            System.out.println("❌ Error creando pedido: " + e.getMessage());
        }
    }

    private void eliminarPedido() {
        try {
            System.out.print("Número pedido: ");
            String numero = sc.nextLine().trim();

            tienda.eliminarPedido(numero);

            System.out.println("✔ Pedido eliminado.");

        } catch (Exception e) {
            System.out.println("⚠ Error eliminando pedido: " + e.getMessage());
        }
    }

    private void mostrarPedidosCliente() {
        System.out.print("Email del cliente: ");
        String email = sc.nextLine().trim();

        Cliente c = tienda.buscarCliente(email);
        if (c == null) {
            System.out.println("⚠ Cliente no encontrado.");
            return;
        }

        tienda.mostrarPedidosCliente(email);
    }
}
