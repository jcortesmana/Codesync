package dao;

import java.util.List;
import models.Cliente;

public interface ClienteDAO {
    void insertar(Cliente cliente) throws Exception;
    void actualizar(Cliente cliente) throws Exception;
    void eliminar(String email) throws Exception;
    Cliente buscarPorEmail(String email) throws Exception;
    List<Cliente> listarTodos() throws Exception;
}