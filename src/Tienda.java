import java.util.ArrayList;
import java.util.List;

public class Tienda {
    private List<Cliente> clientes = new ArrayList<>();
    private List<Articulo> articulos = new ArrayList<>();
    private List<Pedido> pedidos = new ArrayList<>();

    public void agregarCliente(Cliente c) {
        clientes.add(c);
    }

    public Cliente buscarCliente(String email) {
        for (Cliente c : clientes) {
            if (c.getEmail().equalsIgnoreCase(email)) return c;
        }
        return null;
    }

    public void mostrarClientes() {
        clientes.forEach(System.out::println);
    }

    public void mostrarClientesEstandar() {
        clientes.stream()
                .filter(c -> c instanceof ClienteEstandar)
                .forEach(System.out::println);
    }

    public void mostrarClientesPremium() {
        clientes.stream()
                .filter(c -> c instanceof ClientePremium)
                .forEach(System.out::println);
    }

    public void agregarArticulo(Articulo a) {
        articulos.add(a);
    }

    public Articulo buscarArticulo(String codigo) {
        for (Articulo a : articulos) {
            if (a.getCodigo().equalsIgnoreCase(codigo)) return a;
        }
        return null;
    }

    public void mostrarArticulos() {
        articulos.forEach(System.out::println);
    }

    public void agregarPedido(Pedido p) {
        pedidos.add(p);
    }

    public void eliminarPedido(int numeroPedido) {
        pedidos.removeIf(p -> p.getNumeroPedido() == numeroPedido && p.esCancelable());
    }

    public void listarPedidosPendientes(String clienteEmail) {
        pedidos.stream()
                .filter(p -> !p.isEnviado() && (clienteEmail == null || p.getCliente().getEmail().equals(clienteEmail)))
                .forEach(System.out::println);
    }

    public void listarPedidosEnviados(String clienteEmail) {
        pedidos.stream()
                .filter(p -> p.isEnviado() && (clienteEmail == null || p.getCliente().getEmail().equals(clienteEmail)))
                .forEach(System.out::println);
    }
}