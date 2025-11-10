package models;
import exceptions.*;
import java.util.*;
import java.util.stream.Collectors;
public class Tienda {
    // Elegimos Map para clientes (clave = email) -> búsqueda rápida y unicidad por clave
    private Map<String, Cliente> clientes = new HashMap<>();

    // Map para artículos por código -> búsqueda O(1) por código único
    private Map<String, Articulo> articulos = new HashMap<>();

    // Lista para pedidos (orden de inserción, recorrido, etc.)
    private List<Pedido> pedidos = new ArrayList<>();

    // ========================
    // Gestión de clientes
    // ========================
    public void agregarCliente(Cliente c) throws ClienteDuplicadoException {
        String email = c.getEmail().toLowerCase();
        if (clientes.containsKey(email)) {
            throw new ClienteDuplicadoException("El cliente con email '" + c.getEmail() + "' ya existe.");
        }
        clientes.put(email, c);
    }

    public Cliente buscarCliente(String email) {
        if (email == null) return null;
        return clientes.get(email.toLowerCase());
    }

    public void mostrarClientes() {
        clientes.values().forEach(System.out::println);
    }

    public void mostrarClientesEstandar() {
        clientes.values().stream()
                .filter(c -> c instanceof ClienteEstandar)
                .forEach(System.out::println);
    }

    public void mostrarClientesPremium() {
        clientes.values().stream()
                .filter(c -> c instanceof ClientePremium)
                .forEach(System.out::println);
    }

    // ========================
    // Gestión de artículos
    // ========================
    public void agregarArticulo(Articulo a) throws ArticuloDuplicadoException {
        String codigo = a.getCodigo();
        if (articulos.containsKey(codigo)) {
            throw new ArticuloDuplicadoException("El artículo con código '" + codigo + "' ya existe.");
        }
        articulos.put(codigo, a);
    }

    public Articulo buscarArticulo(String codigo) {
        return articulos.get(codigo);
    }

    public void mostrarArticulos() {
        articulos.values().forEach(System.out::println);
    }

    // ========================
    // Gestión de pedidos
    // ========================
    public void agregarPedido(Pedido p) {
        pedidos.add(p);
    }

    public void eliminarPedido(int numeroPedido) throws PedidoNoCancelableException {
        for (Pedido p : pedidos) {
            if (p.getNumeroPedido() == numeroPedido) {  // ✅ ahora funcionará
                if (p.esCancelable()) {                 // ✅ también funciona
                    pedidos.remove(p);
                    System.out.println("✅ Pedido eliminado correctamente.");
                    return;
                } else {
                    throw new PedidoNoCancelableException(
                            "El pedido " + numeroPedido + " no puede cancelarse (ya preparado/enviado o fuera de plazo)."
                    );
                }
            }
        }
        System.out.println("⚠️ Pedido no encontrado.");
    }

    public void listarPedidosPendientes(String clienteEmail) {
        List<Pedido> resultado = pedidos.stream()
                .filter(p -> !p.isEnviado() && (clienteEmail == null || p.getCliente().getEmail().equalsIgnoreCase(clienteEmail)))
                .collect(Collectors.toList());
        resultado.forEach(System.out::println);
    }

    public void listarPedidosEnviados(String clienteEmail) {
        List<Pedido> resultado = pedidos.stream()
                .filter(p -> p.isEnviado() && (clienteEmail == null || p.getCliente().getEmail().equalsIgnoreCase(clienteEmail)))
                .collect(Collectors.toList());
        resultado.forEach(System.out::println);
    }
}