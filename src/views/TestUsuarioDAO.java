package views;

import factory.DAOFactory;
import dao.UsuarioDAO;
import models.Usuario;

import java.util.List;

public class TestUsuarioDAO {
    public static void main(String[] args) {
        try {
            UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();

            System.out.println("=== 🔹 TEST UsuarioDAO ===");

            // Crear usuario nuevo
            Usuario nuevo = new Usuario("Ricard", "ricard@email.com", "1234");
            usuarioDAO.insertar(nuevo);
            System.out.println("✅ Usuario insertado: " + nuevo.getEmail());

            // Listar usuarios
            List<Usuario> lista = usuarioDAO.listarTodos();
            System.out.println("\n📋 Usuarios en BD:");
            lista.forEach(System.out::println);

            // Buscar usuario por ID
            Usuario encontrado = usuarioDAO.buscarPorId(1);
            if (encontrado != null) {
                System.out.println("\n🔍 Usuario encontrado con ID=1: " + encontrado);
            }

            // Actualizar usuario
            if (encontrado != null) {
                encontrado.setNombre("Ricard Modificado");
                usuarioDAO.actualizar(encontrado);
                System.out.println("\n✏️ Usuario actualizado.");
            }

            // Eliminar usuario
            if (encontrado != null) {
                usuarioDAO.eliminar(encontrado.getId());
                System.out.println("\n🗑️ Usuario eliminado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}