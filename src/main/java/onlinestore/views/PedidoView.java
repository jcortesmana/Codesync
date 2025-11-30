package onlinestore.views;

import onlinestore.models.Pedido;

import java.util.Collection;

public class PedidoView {

    public void mostrarPedidoCreado(Pedido pedido) {
        System.out.println("🧾 Pedido creado: " + pedido);
    }

    public void mostrarPedidos(Collection<Pedido> pedidos) {
        if (pedidos == null || pedidos.isEmpty()) {
            System.out.println("No hay pedidos que mostrar.");
            return;
        }
        pedidos.forEach(System.out::println);
    }

    public void mostrarError(String mensaje) {
        System.err.println("⚠️ Pedido - " + mensaje);
    }
} 