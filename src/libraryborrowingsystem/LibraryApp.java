
package libraryborrowingsystem;


import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LibraryApp extends Application {

    private Connection conn;

    @Override
    public void start(Stage primaryStage) {
        // 1️⃣ Connect to MySQL database
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/booklibrary", 
                "root",                                      
                "Favylade7*"                                           
            );
            System.out.println("✅ Database connection successful!");
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            return; // Stop app if no DB
        }

        // 2️⃣ Show the Login Page first
        LoginPage loginPage = new LoginPage(primaryStage, conn);
        loginPage.show();
    }

    @Override
    public void stop() {
        // 3️⃣ Close DB connection when app exits
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("🔒 Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error closing database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
