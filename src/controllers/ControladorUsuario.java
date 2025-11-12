package controllers;

import exceptions.ClienteDuplicadoException;
import models.Articulo;
import models.Cliente;
import models.ClienteEstandar;
import models.ClientePremium;
import models.Pedido;
import models.Tienda;
import views.ClienteView;
import views.PedidoView;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

public class ControladorUsuario {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final Tienda tienda;
    private final ClienteView clienteView;
    private final PedidoView pedidoView;

    public ControladorUsuario(Tienda tienda, ClienteView clienteView, PedidoView pedidoView) {
        this.tienda = Objects.requireNonNull(tienda, "La tienda no puede ser nula");
        this.clienteView = Objects.requireNonNull(clienteView, "La vista de clientes no puede ser nula");
        this.pedidoView = Objects.requireNonNull(pedidoView, "La vista de pedidos no puede ser nula");
    }

    public boolean registrarUsuario(String nombre, String domicilio, String nif, String email, boolean esPremium) {
        try {
            Cliente cliente = construirCliente(nombre, domicilio, nif, email, esPremium);
            tienda.agregarCliente(cliente);
            clienteView.mostrarRegistroExitoso(cliente);
            return true;
        } catch (IllegalArgumentException e) {
            clienteView.mostrarError(e.getMessage());
            throw e;
        } catch (ClienteDuplicadoException e) {
            clienteView.mostrarError(e.getMessage());
            return false;
        }
    }

    public boolean actualizarDomicilio(String email, String nuevoDomicilio) {
        validarEmail(email);
        validarCadena(nuevoDomicilio, "domicilio");

        Cliente cliente = tienda.buscarCliente(email);
        if (cliente == null) {
            clienteView.mostrarError("No existe un cliente con email " + email);
            return false;
        }
        cliente.setDomicilio(nuevoDomicilio);
        clienteView.mostrarActualizacion(cliente, "domicilio");
        return true;
    }

    public void mostrarUsuarios() {
        clienteView.mostrarListado(tienda.obtenerClientes());
    }

    public boolean registrarPedido(int numeroPedido, String emailCliente, String codigoArticulo, int cantidad) {
        try {
            validarNumeroPedido(numeroPedido);
            validarEmail(emailCliente);
            validarCadena(codigoArticulo, "código de artículo");
            validarCantidad(cantidad);

            Cliente cliente = tienda.buscarCliente(emailCliente);
            if (cliente == null) {
                clienteView.mostrarError("El cliente con email " + emailCliente + " no existe.");
                return false;
            }

            Articulo articulo = tienda.buscarArticulo(codigoArticulo);
            if (articulo == null) {
                pedidoView.mostrarError("No existe un artículo con código " + codigoArticulo);
                return false;
            }

            if (tienda.buscarPedido(numeroPedido) != null) {
                pedidoView.mostrarError("El pedido con número " + numeroPedido + " ya existe.");
                return false;
            }

            Pedido pedido = new Pedido(numeroPedido, cliente, articulo, cantidad, LocalDateTime.now());
            tienda.agregarPedido(pedido);
            pedidoView.mostrarPedidoCreado(pedido);
            return true;
        } catch (IllegalArgumentException e) {
            pedidoView.mostrarError(e.getMessage());
            throw e;
        }
    }

    public boolean registrarUsuarioConPedido(
            String nombre, String domicilio, String nif, String email, boolean esPremium,
            int numeroPedido, String codigoArticulo, int cantidad) {

        Cliente clienteCreado = null;
        try {
            validarNumeroPedido(numeroPedido);
            validarCadena(codigoArticulo, "código de artículo");
            validarCantidad(cantidad);

            Articulo articulo = tienda.buscarArticulo(codigoArticulo);
            if (articulo == null) {
                pedidoView.mostrarError("No existe un artículo con código " + codigoArticulo);
                return false;
            }

            if (tienda.buscarCliente(email) != null) {
                clienteView.mostrarError("Ya existe un cliente registrado con el email " + email);
                return false;
            }

            clienteCreado = construirCliente(nombre, domicilio, nif, email, esPremium);
            tienda.agregarCliente(clienteCreado);

            asegurarNumeroPedidoDisponible(numeroPedido);

            Pedido pedido = new Pedido(numeroPedido, clienteCreado, articulo, cantidad, LocalDateTime.now());
            tienda.agregarPedido(pedido);

            clienteView.mostrarRegistroExitoso(clienteCreado);
            pedidoView.mostrarPedidoCreado(pedido);
            return true;
        } catch (IllegalArgumentException e) {
            clienteView.mostrarError(e.getMessage());
            throw e;
        } catch (ClienteDuplicadoException e) {
            clienteView.mostrarError(e.getMessage());
            return false;
        } catch (IllegalStateException e) {
            if (clienteCreado != null) {
                tienda.eliminarCliente(clienteCreado.getEmail());
            }
            pedidoView.mostrarError(e.getMessage());
            return false;
        }
    }

    private Cliente construirCliente(String nombre, String domicilio, String nif, String email, boolean esPremium) {
        validarCadena(nombre, "nombre");
        validarCadena(domicilio, "domicilio");
        validarCadena(nif, "NIF");
        validarEmail(email);
        return esPremium
                ? new ClientePremium(nombre, domicilio, nif, email)
                : new ClienteEstandar(nombre, domicilio, nif, email);
    }

    private void validarCadena(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
    }

    private void validarEmail(String email) {
        validarCadena(email, "email");
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("El email proporcionado no es válido.");
        }
    }

    private void validarNumeroPedido(int numeroPedido) {
        if (numeroPedido <= 0) {
            throw new IllegalArgumentException("El número de pedido debe ser positivo.");
        }
    }

    private void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
    }

    private void asegurarNumeroPedidoDisponible(int numeroPedido) {
        if (tienda.buscarPedido(numeroPedido) != null) {
            throw new IllegalStateException("El pedido con número " + numeroPedido + " ya existe.");
        }
    }
}