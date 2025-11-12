package tests.controllers;

import controllers.ControladorUsuario;
import exceptions.ArticuloDuplicadoException;
import exceptions.ClienteDuplicadoException;
import models.Articulo;
import models.Cliente;
import models.ClienteEstandar;
import models.Pedido;
import models.Tienda;
import views.ClienteView;
import views.PedidoView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioControllerTest {

    private Tienda tienda;
    private RecordingClienteView clienteView;
    private RecordingPedidoView pedidoView;
    private ControladorUsuario controller;

    @BeforeEach
    void setUp() throws ArticuloDuplicadoException {
        tienda = new Tienda();
        clienteView = new RecordingClienteView();
        pedidoView = new RecordingPedidoView();
        controller = new ControladorUsuario(tienda, clienteView, pedidoView);

        tienda.agregarArticulo(new Articulo("A-100", "Banda elástica", 12.0, 2.0, 5));
    }

    @Test
    void registrarUsuarioValidoPersisteCliente() {
        boolean registrado = controller.registrarUsuario(
                "Ana Pérez", "Calle Luna 45", "87654321B", "ana@test.com", false);

        assertTrue(registrado);
        assertNotNull(tienda.buscarCliente("ana@test.com"));
        assertNotNull(clienteView.getUltimoClienteRegistrado());
        assertEquals("ana@test.com", clienteView.getUltimoClienteRegistrado().getEmail());
    }

    @Test
    void registrarUsuarioConEmailInvalidoLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.registrarUsuario(
                        "Luis", "Calle Sol 12", "12345678Z", "correo-invalido", true)
        );

        assertTrue(ex.getMessage().contains("email"));
        assertTrue(clienteView.getUltimoError().contains("email"));
    }

    @Test
    void registrarPedidoSinArticuloNotificaError() throws ClienteDuplicadoException {
        tienda.agregarCliente(new ClienteEstandar("Mario", "Dir 1", "11111111H", "mario@test.com"));

        boolean creado = controller.registrarPedido(5, "mario@test.com", "COD-NO-EXISTE", 2);

        assertFalse(creado);
        assertTrue(pedidoView.getUltimoError().contains("No existe un artículo"));
    }

    @Test
    void registrarUsuarioConPedidoDuplicadoRevierteCliente() throws ClienteDuplicadoException {
        Cliente clienteExistente = new ClienteEstandar("Laura", "Dir 2", "22222222J", "laura@test.com");
        tienda.agregarCliente(clienteExistente);
        Pedido pedidoExistente = new Pedido(99, clienteExistente,
                tienda.buscarArticulo("A-100"), 1, LocalDateTime.now());
        tienda.agregarPedido(pedidoExistente);

        boolean resultado = controller.registrarUsuarioConPedido(
                "Nuevo", "Dir nueva", "33333333K", "nuevo@test.com", true,
                99, "A-100", 3);

        assertFalse(resultado);
        assertNull(tienda.buscarCliente("nuevo@test.com"));
        assertTrue(pedidoView.getUltimoError().contains("ya existe"));
    }

    private static class RecordingClienteView extends ClienteView {
        private Cliente ultimoClienteRegistrado;
        private String ultimoError = "";

        @Override
        public void mostrarRegistroExitoso(Cliente cliente) {
            this.ultimoClienteRegistrado = cliente;
        }

        @Override
        public void mostrarError(String mensaje) {
            this.ultimoError = mensaje;
        }

        Cliente getUltimoClienteRegistrado() {
            return ultimoClienteRegistrado;
        }

        String getUltimoError() {
            return ultimoError;
        }
    }

    private static class RecordingPedidoView extends PedidoView {
        private String ultimoError = "";

        @Override
        public void mostrarPedidoCreado(Pedido pedido) {
            // no-op para pruebas
        }

        @Override
        public void mostrarError(String mensaje) {
            this.ultimoError = mensaje;
        }

        String getUltimoError() {
            return ultimoError;
        }
    }
}
