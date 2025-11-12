package views;

import models.Tienda;

public class TiendaView {

    public void mostrarResumen(Tienda tienda) {
        if (tienda == null) {
            System.out.println("No hay datos de tienda disponibles.");
            return;
        }
        System.out.println("=== Resumen Tienda ===");
        System.out.println("Clientes registrados: " + tienda.obtenerClientes().size());
        System.out.println("Artículos cargados: " + tienda.obtenerArticulos().size());
        System.out.println("Pedidos activos: " + tienda.obtenerPedidos().size());
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.err.println("⚠️ Tienda - " + mensaje);
    }
}