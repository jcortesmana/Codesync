package factory;

import dao.*;
import dao.Impl.ArticuloDAOImpl;
import dao.Impl.ClienteDAOImpl;
import dao.Impl.PedidoDAOImpl;
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

    public static ClienteDAO getClienteDAO() {
        return new ClienteDAOImpl(conexion);
    }

    public static PedidoDAO getPedidoDAO() {
        return new PedidoDAOImpl(conexion);
    }

    public static ArticuloDAO getArticuloDAO() {
        return new ArticuloDAOImpl(conexion);
    }

}