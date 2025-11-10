package factory;

import dao.*;
import utils.ConexionBD;
import java.sql.Connection;

public class DAOFactory {
    private static Connection conexion;

    static {
        try {
            conexion = ConexionBD.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOImpl(conexion);
    }

    public static ClienteDAO getClienteDAO() {
        return new ClienteDAOImpl(conexion);
    }

    public static PedidoDAO getPedidoDAO() {
        return new PedidoDAOImpl(conexion);
    }
}