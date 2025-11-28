package factory;

import dao.ClienteDAO;
import dao.PedidoDAO;
import dao.ArticuloDAO;
import dao.jpa.ClienteDAOImpl;
import dao.jpa.PedidoDAOImpl;
import dao.jpa.ArticuloDAOImpl;

public class JPADAOFactory {
    public static ClienteDAO getClienteDAO() { return new ClienteDAOImpl(); }
    public static PedidoDAO getPedidoDAO() { return new PedidoDAOImpl(); }
    public static ArticuloDAO getArticuloDAO() { return new ArticuloDAOImpl(); }
}