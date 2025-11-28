package models;

import dao.*;
import exceptions.*;
import factory.JPADAOFactory;

import java.util.List;
import java.util.Collection;

public class Tienda {

    private ClienteDAO clienteDAO;
    private ArticuloDAO articuloDAO;
    private PedidoDAO pedidoDAO;

    public Tienda() {
        // usando JPA DAOs por defecto ahora
        clienteDAO = JPADAOFactory.getClienteDAO();
        articuloDAO = JPADAOFactory.getArticuloDAO();
        pedidoDAO = JPADAOFactory.getPedidoDAO();
    }

    // CLIENTES
    public void agregarCliente(Cliente c) throws ClienteDuplicadoException {
        try {
            int id = clienteDAO.insertar(c);
            c.setId(id);
        } catch (Exception e) {
            throw new ClienteDuplicadoException("El cliente ya existe.");
        }
    }

    public Cliente buscarCliente(String email) {
        try {
            return clienteDAO.buscarPorEmail(email);
        } catch (Exception e) { return null; }
    }

    public Collection<Cliente> obtenerClientes() {
        try {
            return clienteDAO.listarTodos();
        } catch (Exception e) { return List.of(); }
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

    // ARTICULOS
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
        } catch (Exception e) { return null; }
    }

    public Collection<Articulo> obtenerArticulos() {
        try {
            return articuloDAO.obtenerTodos();
        } catch (Exception e) { return List.of(); }
    }

    public void mostrarArticulos() {
        obtenerArticulos().forEach(System.out::println);
    }

    public void eliminarArticulo(String codigo) {
        try {
            articuloDAO.eliminar(codigo);
        } catch (Exception ignored) {}
    }

    // PEDIDOS
    public void agregarPedido(Pedido p) {
        try {
            List<PedidoLinea> lineas = p.getLineas() == null ? List.of() : p.getLineas();
            String num = pedidoDAO.insertarPedidoConLineas(p, lineas);
            // si quieres, puedes setear el numero obtenido en p
            if (num != null) {
                // p.setNumeroPedido(num); // si tienes setter para numero, adaptalo
            }
        } catch (Exception e) {
            System.out.println("Error al agregar pedido: " + e.getMessage());
        }
    }

    public Pedido buscarPedido(String numero) {
        try {
            Pedido p = pedidoDAO.buscarPorNumero(numero);
            if (p != null) p.setTienda(this);
            return p;
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
            if (!p.esCancelable())
                throw new PedidoNoCancelableException("El pedido no puede cancelarse.");
            pedidoDAO.eliminar(numero);
        } catch (PedidoNoCancelableException e) {
            throw e;
        } catch (Exception ignored) {}
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