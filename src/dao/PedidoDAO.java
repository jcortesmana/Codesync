package dao;

import models.Pedido;
import models.PedidoLinea;
import java.util.List;

public interface PedidoDAO {
    int insertarPedidoConLineas(Pedido pedido, List<PedidoLinea> lineas) throws Exception; // devuelve numero generado
    Pedido buscarPorNumero(int numero) throws Exception;
    List<Pedido> listarTodos() throws Exception;
    void actualizar(Pedido pedido) throws Exception;
    void eliminar(int numero) throws Exception;
}