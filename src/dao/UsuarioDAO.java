package dao;

import java.util.List;
import models.Usuario;

public interface UsuarioDAO {
    void insertar(Usuario usuario) throws Exception;
    void actualizar(Usuario usuario) throws Exception;
    void eliminar(int id) throws Exception;
    Usuario buscarPorId(int id) throws Exception;
    List<Usuario> listarTodos() throws Exception;
}