package views;

import models.Cliente;

import java.util.Collection;

public class ClienteView {

    public void mostrarListado(Collection<Cliente> clientes) {
        if (clientes == null || clientes.isEmpty()) {
            System.out.println("No hay clientes para mostrar.");
            return;
        }
        clientes.forEach(System.out::println);
    }

    public void mostrarRegistroExitoso(Cliente cliente) {
        System.out.println("✅ Cliente registrado: " + cliente);
    }

    public void mostrarActualizacion(Cliente cliente, String campoActualizado) {
        System.out.println("🔄 Cliente actualizado (" + campoActualizado + "): " + cliente);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.err.println("⚠️ Cliente - " + mensaje);
    }
}