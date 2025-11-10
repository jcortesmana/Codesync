package views;

import factory.DAOFactory;
import dao.UsuarioDAO;
import models.Usuario;

import java.util.List;

public class TestUsuarioDAO {
    public static void main(String[] args) {
        try {
            UsuarioDAO dao = DAOFactory.getUsuarioDAO();
            Usuario u = new Usuario("ricard", "ricard@ej.com", "1234");
            dao.insertarConSP(u);

            List<Usuario> lista = dao.listarTodos();
            lista.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}