package tests;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import utils.ConexionBD;

public class ConexionTest {
    public static void main(String[] args) {
     
        Connection conn = ConexionBD.getConnection();

        if (conn != null) {
            System.out.println("✅ Conexión establecida correctamente con la base de datos.");

      
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SELECT 1"); // consulta de prueba
                System.out.println("✅ Consulta de prueba ejecutada correctamente.");
            } catch (SQLException e) {
                System.err.println("⚠️ Error ejecutando consulta de prueba: " + e.getMessage());
            }


            ConexionBD.closeConnection();
        } else {
            System.err.println("❌ No se pudo establecer conexión con la base de datos.");
        }
    }
}
