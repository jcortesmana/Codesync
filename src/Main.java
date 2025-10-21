public class Main {
    public static void main(String[] args) {
        // 1️⃣ Crear la tienda (modelo)
        Tienda tienda = new Tienda();

        // 2️⃣ Crear el controlador (gestiona la lógica y la vista)
        ControladorTienda controlador = new ControladorTienda(tienda);

        // 3️⃣ Iniciar la aplicación (mostrará el menú en consola)
        controlador.iniciar();
    }
}