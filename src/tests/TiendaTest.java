package tests;

import exceptions.*;
import models.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TiendaTest {

    private Tienda tienda;

    @BeforeEach
    void setUp() {
        tienda = new Tienda();
    }

    // =========================
    // Prueba 1: agregarCliente
    // =========================
    @Test
    void testAgregarClienteExitoso() throws ClienteDuplicadoException {
        Cliente cliente = new ClienteEstandar("Juan Pérez", "Calle Falsa 123", "12345678A", "juan@test.com");
        tienda.agregarCliente(cliente);

        Cliente resultado = tienda.buscarCliente("juan@test.com");
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("juan@test.com", resultado.getEmail());
    }

    @Test
    void testAgregarClienteDuplicadoLanzaExcepcion() throws ClienteDuplicadoException {
        Cliente cliente = new ClienteEstandar("Ana López", "Av. Siempre Viva 742", "87654321B", "ana@test.com");
        tienda.agregarCliente(cliente);

        ClienteDuplicadoException ex = assertThrows(
                ClienteDuplicadoException.class,
                () -> tienda.agregarCliente(
                        new ClienteEstandar("Ana L", "Otra calle", "99999999C", "ana@test.com")
                )
        );

        assertTrue(ex.getMessage().contains("ya existe"));
    }

    // =========================
    // Prueba 2: agregarArticulo
    // =========================
    @Test
    void testAgregarArticuloExitoso() throws ArticuloDuplicadoException {
        Articulo articulo = new Articulo("A1", "Teclado mecánico", 25.0, 3.5, 2);
        tienda.agregarArticulo(articulo);

        Articulo resultado = tienda.buscarArticulo("A1");
        assertNotNull(resultado);
        assertEquals("Teclado mecánico", resultado.getDescripcion());
        assertEquals(25.0, resultado.getPrecio());
        assertEquals(3.5, resultado.getGastosEnvio());
        assertEquals(2, resultado.getTiempoPreparacion());
    }

    @Test
    void testAgregarArticuloDuplicadoLanzaExcepcion() throws ArticuloDuplicadoException {
        Articulo articulo = new Articulo("A2", "Ratón inalámbrico", 15.0, 2.0, 1);
        tienda.agregarArticulo(articulo);

        ArticuloDuplicadoException ex = assertThrows(
                ArticuloDuplicadoException.class,
                () -> tienda.agregarArticulo(
                        new Articulo("A2", "Mouse óptico", 20.0, 2.0, 1)
                )
        );

        assertTrue(ex.getMessage().contains("ya existe"));
    }
}
    