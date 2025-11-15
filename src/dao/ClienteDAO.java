package dao;

import models.Cliente;
import java.math.BigDecimal;
import java.util.List;

public interface ClienteDAO {
    int insertar(Cliente cliente) throws Exception; // devuelve id generado
    int insertarConSP(Cliente cliente, BigDecimal cuotaAnual, BigDecimal descuento) throws Exception;
    Cliente buscarPorEmail(String email) throws Exception;
    List<Cliente> listarTodos() throws Exception;
    void actualizar(Cliente cliente) throws Exception;
    void eliminar(int id) throws Exception;
    List<Cliente> listarEstandar() throws Exception;
    List<Cliente> listarPremium() throws Exception;

}