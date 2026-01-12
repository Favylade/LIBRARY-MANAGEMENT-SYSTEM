package libraryborrowingsystem;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;

public class SignUpPage {

    Stage stage;
    Connection conn;

    public SignUpPage(Stage stage, Connection conn) {  // ✅ same constructor style
        this.stage = stage;
        this.conn = conn;
    }

    public void show() {
        Label header = new Label(" Library Sign-Up📘");
        header.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");

        Label nameLabel = new Label("Full Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.setMaxWidth(300);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setMaxWidth(300);

        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter your phone number");
        phoneField.setMaxWidth(300);

        Button signUpButton = new Button("Sign Up");
        Button backButton = new Button("Back to Login");

        signUpButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please fill in all fields.");
                return;
            }

            UserManager userManager = new UserManager(conn);
            User newUser = userManager.signUp(name, email, phone);

            if (newUser != null) {

                // Send user ID to email
                EmailService.sendUserIdEmail(email, newUser.getUserID());

                showAlert(Alert.AlertType.INFORMATION,
                        "Sign-up successful! Your Library User ID is:" + newUser.getUserID() + "\nYour User ID has also been sent "
                                + "to your email incase you forget. Keep it safe." );
                new LoginPage(stage, conn).show();

            } else {
                showAlert(Alert.AlertType.ERROR, "Sign-up failed. User may already exist.");
            }
        });

        backButton.setOnAction(e -> {
            new LoginPage(stage, conn).show();
        });

        VBox layout = new VBox(10,
                header,
                nameLabel, nameField,
                emailLabel, emailField,
                phoneLabel, phoneField,
                signUpButton,
                backButton
        );
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 40; -fx-background-color: #cc9966;");

        Scene scene = new Scene(layout, 1380, 750);
        stage.setScene(scene);
        stage.setTitle("Library Sign-Up");
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
