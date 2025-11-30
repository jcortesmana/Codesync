package onlinestore.factory;

import onlinestore.dao.ClienteDAO;
import onlinestore.dao.PedidoDAO;
import onlinestore.dao.ArticuloDAO;
import onlinestore.dao.jpa.ClienteDAOImpl;
import onlinestore.dao.jpa.PedidoDAOImpl;
import onlinestore.dao.jpa.ArticuloDAOImpl;

public class JPADAOFactory {
    public static ClienteDAO getClienteDAO() { return new ClienteDAOImpl(); }
    public static PedidoDAO getPedidoDAO() { return new PedidoDAOImpl(); }
    public static ArticuloDAO getArticuloDAO() { return new ArticuloDAOImpl(); }
}