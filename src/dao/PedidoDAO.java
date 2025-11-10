package dao;

import java.util.List;
import models.Pedido;

public interface PedidoDAO {
    void insertar(Pedido pedido) throws Exception;
    void actualizar(Pedido pedido) throws Exception;
    void eliminar(int numeroPedido) throws Exception;
    Pedido buscarPorNumero(int numeroPedido) throws Exception;
    List<Pedido> listarTodos() throws Exception;
}