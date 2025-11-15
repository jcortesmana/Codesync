import controllers.ControladorTienda;
import models.Tienda;

public class Main {
    public static void main(String[] args) {
   
        Tienda tienda = new Tienda();

        ControladorTienda controlador = new ControladorTienda(tienda);

        controlador.iniciar();
    }
}
