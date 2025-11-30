package onlinestore.dao;

import onlinestore.models.Cliente;
import java.util.List;

public interface ClienteDAO {

    void insertar(Cliente cliente) throws Exception;

    Cliente buscarPorEmail(String email) throws Exception;

    List<Cliente> listarTodos() throws Exception;

    void actualizar(Cliente cliente) throws Exception;

    void eliminar(int id) throws Exception;

    List<Cliente> listarEstandar() throws Exception;

    List<Cliente> listarPremium() throws Exception;
}
