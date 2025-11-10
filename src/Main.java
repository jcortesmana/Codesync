import controllers.ControladorTienda;
import models.*;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // 1) Crear el modelo (Tienda)
        Tienda tienda = new Tienda();

        // 2) Añadir datos de ejemplo (si no quieres datos de ejemplo, borra este bloque)
        try {
            // Clientes de ejemplo
            Cliente c1 = new ClienteEstandar("Juan Pérez", "Calle Mayor 123, Madrid", "12345678A", "juanperez@email.com");
            Cliente c2 = new ClientePremium("María López", "Avenida Central 45, Barcelona", "87654321B", "marialopez@email.com");

            tienda.agregarCliente(c1);
            tienda.agregarCliente(c2);

            // Artículos de ejemplo
            Articulo a1 = new Articulo("A01", "Mancuerna de 5kg", 15.0, 3.5, 2);
            Articulo a2 = new Articulo("A02", "Colchoneta antideslizante", 10.5, 2.0, 1);

            tienda.agregarArticulo(a1);
            tienda.agregarArticulo(a2);

            // Pedidos en memoria (opcional, sólo en el modelo en memoria)
            Pedido p1 = new Pedido(LocalDate.now(), 2, false, c1);
            Pedido p2 = new Pedido(LocalDate.now().plusDays(1), 1, false, c2);
            tienda.agregarPedido(p1);
            tienda.agregarPedido(p2);

        } catch (Exception e) {
            // Si tu implementación de Tienda lanza excepciones de duplicado u otras, las atrapamos para demo
            System.err.println("Aviso al añadir datos de ejemplo: " + e.getMessage());
        }

        // 3) Crear el controlador (gestiona la lógica y muestra el menú)
        ControladorTienda controlador = new ControladorTienda(tienda);

        // 4) Iniciar la aplicación (menú en consola)
        controlador.iniciar();
    }
}