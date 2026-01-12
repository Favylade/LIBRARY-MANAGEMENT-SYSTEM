/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryborrowingsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Sqlconnector {

    // MySQL connection parameters
    String dbUrl = "jdbc:mysql://localhost:3306/booklibrary";
    String username = "root";
    String password = "Favylade7*";

    // Method to establish and return the connection
    public Connection connect() {
        Connection conn = null;
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            conn = DriverManager.getConnection(dbUrl, "root", "Favylade7*");
            System.out.println("✅ Connected to MySQL!");

        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC driver not found: " + e.getMessage());
        }

        // Return connection (could be null if failed)
        return conn;
    }
}
