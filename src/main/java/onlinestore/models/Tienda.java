package onlinestore.models;

import onlinestore.dao.*;
import onlinestore.exceptions.*;
import onlinestore.factory.JPADAOFactory;

import java.util.List;
import java.util.Collection;

public class Tienda {

    private ClienteDAO clienteDAO;
    private ArticuloDAO articuloDAO;
    private PedidoDAO pedidoDAO;

    public Tienda() {
        // JPA DAOs
        clienteDAO = JPADAOFactory.getClienteDAO();
        articuloDAO = JPADAOFactory.getArticuloDAO();
        pedidoDAO = JPADAOFactory.getPedidoDAO();
    }

    // ================================================================
    // CLIENTES
    // ================================================================
    public void agregarCliente(Cliente c) throws ClienteDuplicadoException {
        try {
            clienteDAO.insertar(c);   // JPA gestiona el id
        } catch (Exception e) {
            throw new ClienteDuplicadoException("El cliente ya existe.");
        }
    }

    public Cliente buscarCliente(String email) {
        try {
            return clienteDAO.buscarPorEmail(email);
        } catch (Exception e) {
            return null;
        }
    }

    public Collection<Cliente> obtenerClientes() {
        try {
            return clienteDAO.listarTodos();
        } catch (Exception e) {
            return List.of();
        }
    }

    public void eliminarCliente(String email) {
        try {
            Cliente c = clienteDAO.buscarPorEmail(email);
            if (c != null) clienteDAO.eliminar(c.getId());
        } catch (Exception ignored) {}
    }

    public void mostrarClientes() {
        obtenerClientes().forEach(System.out::println);
    }

    public void mostrarClientesEstandar() {
        obtenerClientes().stream()
                .filter(c -> c instanceof ClienteEstandar)
                .forEach(System.out::println);
    }

    public void mostrarClientesPremium() {
        obtenerClientes().stream()
                .filter(c -> c instanceof ClientePremium)
                .forEach(System.out::println);
    }

    // ================================================================
    // ARTICULOS
    // ================================================================
    public void agregarArticulo(Articulo a) throws ArticuloDuplicadoException {
        try {
            articuloDAO.insertar(a);
        } catch (Exception e) {
            throw new ArticuloDuplicadoException("El artículo ya existe.");
        }
    }

    public Articulo buscarArticulo(String codigo) {
        try {
            return articuloDAO.buscarPorCodigo(codigo);
        } catch (Exception e) {
            return null;
        }
    }

    public Collection<Articulo> obtenerArticulos() {
        try {
            return articuloDAO.obtenerTodos();
        } catch (Exception e) {
            return List.of();
        }
    }

    public void mostrarArticulos() {
        obtenerArticulos().forEach(System.out::println);
    }

    public void eliminarArticulo(String codigo) {
        try {
            articuloDAO.eliminar(codigo);
        } catch (Exception ignored) {}
    }

    // ================================================================
    // PEDIDOS
    // ================================================================
    public void agregarPedido(Pedido p) {
        try {
            List<PedidoLinea> lineas = p.getLineas() == null ? List.of() : p.getLineas();
            String numero = pedidoDAO.insertarPedidoConLineas(p, lineas);

            // asignar número generado (si quieres mantener compatibilidad)
            if (numero != null) {
                // si quieres guardar el número:
                // p.setNumero(Integer.parseInt(numero));
            }

        } catch (Exception e) {
            System.out.println("Error al agregar pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Pedido buscarPedido(String numero) {
        try {
            Pedido pedido = pedidoDAO.buscarPorNumero(numero);
            if (pedido != null) pedido.setTienda(this);
            return pedido;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Pedido> obtenerPedidos() {
        try {
            List<Pedido> lista = pedidoDAO.listarTodos();
            lista.forEach(p -> p.setTienda(this));
            return lista;
        } catch (Exception e) {
            return List.of();
        }
    }

    public void eliminarPedido(String numero) throws PedidoNoCancelableException {
        try {
            Pedido p = pedidoDAO.buscarPorNumero(numero);
            if (p == null) return;

            p.setTienda(this); // necesario para esCancelable()

            if (!p.esCancelable())
                throw new PedidoNoCancelableException("El pedido no puede cancelarse.");

            pedidoDAO.eliminar(numero);

        } catch (PedidoNoCancelableException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Error eliminando pedido: " + e.getMessage());
        }
    }

    public void listarPedidosPendientes(String email) {
        obtenerPedidos().stream()
                .filter(p -> !p.isEnviado())
                .filter(p -> email == null || p.getCliente().getEmail().equalsIgnoreCase(email))
                .forEach(System.out::println);
    }

    public void listarPedidosEnviados(String email) {
        obtenerPedidos().stream()
                .filter(Pedido::isEnviado)
                .filter(p -> email == null || p.getCliente().getEmail().equalsIgnoreCase(email))
                .forEach(System.out::println);
    }

    public void mostrarPedidos() {
        obtenerPedidos().forEach(System.out::println);
    }

    public void mostrarPedidosCliente(String email) {
        obtenerPedidos().stream()
                .filter(p -> p.getCliente().getEmail().equalsIgnoreCase(email))
                .forEach(System.out::println);
    }
}
