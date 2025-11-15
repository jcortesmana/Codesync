package dao;

import models.Articulo;
import java.util.List;

public interface ArticuloDAO {

    void insertar(Articulo articulo) throws Exception;

    Articulo buscarPorCodigo(String codigo) throws Exception;

    List<Articulo> obtenerTodos() throws Exception;

    void eliminar(String codigo) throws Exception;
}
