package tests;


import dao.Impl.ArticuloDAOImpl;
import models.Articulo;
import utils.ConexionBD;

import java.sql.Connection;

public class ArticuloTest {

    public static void main(String[] args) {

        try {
            Connection conexion = ConexionBD.getConnection();
            ArticuloDAOImpl articuloDAO = new ArticuloDAOImpl(conexion);

            System.out.println("=== TEST ARTICULO ===");

            // INSERTAR
            Articulo a1 = new Articulo("T100", "Teclado RGB", 45.50, 4.99, 2);
            articuloDAO.insertar(a1);
            System.out.println("Insertado: " + a1);

            // BUSCAR
            System.out.println("Buscar T100 → " + articuloDAO.buscarPorCodigo("T100"));

            // LISTAR
            System.out.println("\nLista completa:");
            articuloDAO.obtenerTodos().forEach(System.out::println);

            // ELIMINAR
            articuloDAO.eliminar("T100");
            System.out.println("\nTras eliminar T100 → " + articuloDAO.buscarPorCodigo("T100"));

            System.out.println("\n=== FIN TEST ARTICULO ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
