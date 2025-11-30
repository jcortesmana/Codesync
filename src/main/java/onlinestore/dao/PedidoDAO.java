package onlinestore.dao;

import onlinestore.models.Pedido;
import onlinestore.models.PedidoLinea;
import java.util.List;

public interface PedidoDAO {
    String insertarPedidoConLineas(Pedido pedido, List<PedidoLinea> lineas) throws Exception; 
    Pedido buscarPorNumero(String numero) throws Exception;
    List<Pedido> listarTodos() throws Exception;
    void actualizar(Pedido pedido) throws Exception;
    void eliminar(String numero) throws Exception;
    List<String[]> listarPedidosDetallados() throws Exception;
    List<String[]> listarPedidosPorEmail(String email) throws Exception;
}
