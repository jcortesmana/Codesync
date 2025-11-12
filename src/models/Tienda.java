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
        Optional<Pedido> opt = pedidos.stream()
                .filter(p -> p.getNumeroPedido() == numeroPedido)
                .findFirst();

        if (opt.isEmpty()) {
            // No existe -> no hacemos nada (alternativa: lanzar excepción PedidoNoEncontrado)
            return;
        }

        Pedido p = opt.get();
        if (!p.esCancelable()) {
            throw new PedidoNoCancelableException("El pedido " + numeroPedido + " no puede cancelarse (ya preparado/enviado o fuera de plazo).");
        }

        pedidos.remove(p);
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

    public Collection<Cliente> obtenerClientes() {
        return Collections.unmodifiableCollection(clientes.values());
    }

    public Collection<Articulo> obtenerArticulos() {
        return Collections.unmodifiableCollection(articulos.values());
    }

    public List<Pedido> obtenerPedidos() {
        return Collections.unmodifiableList(pedidos);
    }

    public Pedido buscarPedido(int numeroPedido) {
        return pedidos.stream()
                .filter(p -> p.getNumeroPedido() == numeroPedido)
                .findFirst()
                .orElse(null);
    }

    public void eliminarCliente(String email) {
        if (email == null) {
            return;
        }
        clientes.remove(email.toLowerCase());
    }
}