package onlinestore.factory;

import onlinestore.dao.*;
import onlinestore.dao.Impl.ArticuloDAOImpl;
import onlinestore.dao.Impl.ClienteDAOImpl;
import onlinestore.dao.Impl.PedidoDAOImpl;
import onlinestore.utils.ConexionBD;
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