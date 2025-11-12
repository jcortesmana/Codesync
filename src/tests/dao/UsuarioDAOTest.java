package tests.dao;

import org.junit.jupiter.api.*;

import dao.UsuarioDAO;
import dao.Impl.UsuarioDAOImpl;
import models.Usuario;
import utils.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioDAOTest {

    private Connection connection;
    private UsuarioDAO usuarioDAO;
    private Savepoint savepoint;

    @BeforeAll
    void initConnection() throws SQLException {
        connection = ConexionBD.getConnection();
        Assumptions.assumeTrue(connection != null, "No se pudo establecer conexión a la base de datos");

        // ✅ Instanciamos la implementación concreta, no la interfaz
        usuarioDAO = new UsuarioDAOImpl(connection);

        // ✅ Crear tabla si no existe (nombre igual al del DAO)
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS usuarios (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) NOT NULL,
                        email VARCHAR(100) UNIQUE NOT NULL,
                        password VARCHAR(100) NOT NULL
                    )
                    """);
        }
    }

    @AfterAll
    void closeConnection() throws SQLException {
        if (connection != null) {
            if (!connection.getAutoCommit()) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
            ConexionBD.closeConnection();
        }
    }

    @BeforeEach
    void startTransaction() throws SQLException {
        Assumptions.assumeTrue(connection != null, "No hay conexión activa");
        connection.setAutoCommit(false);
        savepoint = connection.setSavepoint();
    }

    @AfterEach
    void rollbackTransaction() throws SQLException {
        if (connection != null) {
            try {
                connection.rollback(savepoint);
            } catch (SQLException e) {
                System.err.println("⚠️ No se pudo hacer rollback al savepoint (puede que se haya hecho commit en el DAO)");
            }
            connection.setAutoCommit(true);
        }
    }

    // ==============================
    // TEST: INSERTAR USUARIO
    // ==============================
    @Test
    void testInsertarUsuario() throws Exception {
        Usuario usuario = new Usuario("Carla Insert", "carla110.insert@test.com", "pwd123");
        usuarioDAO.insertar(usuario);

        assertTrue(usuario.getId() > 0, "El ID generado debe ser mayor a cero");

        Usuario desdeBD = usuarioDAO.buscarPorId(usuario.getId());
        assertNotNull(desdeBD, "El usuario insertado debe existir en BD");
        assertEquals("Carla Insert", desdeBD.getNombre());
    }

    // ==============================
    // TEST: ACTUALIZAR USUARIO
    // ==============================
    @Test
    void testActualizarUsuario() throws Exception {
        Usuario usuario = new Usuario("Carlos Update", "carlos10.update@test.com", "pwd");
        usuarioDAO.insertar(usuario);

        usuario.setNombre("Carlos Actualizado");
        usuario.setPassword("nuevoPwd");
        usuarioDAO.actualizar(usuario);

        Usuario desdeBD = usuarioDAO.buscarPorId(usuario.getId());
        assertNotNull(desdeBD);
        assertEquals("Carlos Actualizado", desdeBD.getNombre());
        assertEquals("nuevoPwd", desdeBD.getPassword());
    }

    // ==============================
    // TEST: ELIMINAR USUARIO
    // ==============================
    @Test
    void testEliminarUsuario() throws Exception {
        Usuario usuario = new Usuario("Dario Delete", "dario10.delete@test.com", "pwd");
        usuarioDAO.insertar(usuario);

        usuarioDAO.eliminar(usuario.getId());

        Usuario desdeBD = usuarioDAO.buscarPorId(usuario.getId());
        assertNull(desdeBD, "El usuario eliminado no debe existir en BD");
    }

    // ==============================
    // TEST: BUSCAR POR ID
    // ==============================
    @Test
    void testBuscarPorId() throws Exception {
        Usuario usuario = new Usuario("Fernanda Find", "fer10.find@test.com", "pwd");
        usuarioDAO.insertar(usuario);

        Usuario desdeBD = usuarioDAO.buscarPorId(usuario.getId());
        assertNotNull(desdeBD);
        assertEquals(usuario.getEmail(), desdeBD.getEmail());
    }
}