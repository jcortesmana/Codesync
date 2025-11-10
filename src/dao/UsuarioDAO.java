package dao;

import models.Usuario;
import java.util.List;

public interface UsuarioDAO {
    void insertar(Usuario usuario) throws Exception;
    void insertarConSP(Usuario usuario) throws Exception;
    Usuario buscarPorId(int id) throws Exception;
    List<Usuario> listarTodos() throws Exception;
    void actualizar(Usuario usuario) throws Exception;
    void eliminar(int id) throws Exception;
}