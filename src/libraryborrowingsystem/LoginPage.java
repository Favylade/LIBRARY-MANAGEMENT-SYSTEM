/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryborrowingsystem;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;

public class LoginPage {

    Stage stage;
    Connection conn;

    public LoginPage(Stage stage, Connection conn) {
        this.stage = stage;
        this.conn = conn;
    }

    public void show() {
        Label welcomeLabel = new Label(" Welcome to the Library  📚");
        welcomeLabel.setStyle("-fx-font-size: 44px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label userIdLabel = new Label("Enter your Library User ID:");
        TextField userIdField = new TextField();
        userIdField.setPromptText("e.g., LIBU521");
        userIdField.setMaxWidth(300);

        Button loginButton = new Button("Login");
        Button signupButton = new Button("Sign Up");

        Label messageLabel = new Label();

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(welcomeLabel, userIdLabel, userIdField, loginButton, signupButton, messageLabel);
        layout.setStyle("-fx-padding: 40; -fx-background-color: #cc9966;");

        Scene scene = new Scene(layout, 1380, 750);
        stage.setScene(scene);
        stage.setTitle("Library Login");
        stage.show();

        UserManager userManager = new UserManager(conn);

        // 🟢 LOGIN BUTTON ACTION
        loginButton.setOnAction(e -> {
            String userId = userIdField.getText().trim();
            if (userId.isEmpty()) {
                messageLabel.setText("⚠️ Please enter your user ID.");
                return;
            }

            User user = userManager.login(userId);
            if (user != null) {
                messageLabel.setText("✅ Login successful! Welcome, " + user.getName());

                // 🟢 Move to Main Menu Page
                MainMenuPage mainMenuPage = new MainMenuPage(stage, conn, user);
                mainMenuPage.show();

            } else {
                messageLabel.setText("❌ Invalid User ID. Please sign up first.");
            }
        });

        // 🟣 SIGNUP BUTTON ACTION
        signupButton.setOnAction(e -> {
            SignUpPage signupPage = new SignUpPage(stage, conn);
            signupPage.show();
        });
    }
}
