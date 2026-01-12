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

public class MainMenuPage {

    private final Stage stage;
    private final Connection conn;
    private final User user;

    public MainMenuPage(Stage stage, Connection conn, User user) {
        this.stage = stage;
        this.conn = conn;
        this.user = user;
    }

    public void show() {
        Label greeting = new Label(" Welcome, " + user.getName() + "!👋");
        greeting.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button viewBooksButton = new Button("View Available Books");
        viewBooksButton.setPrefWidth(300);
        Button borrowBookButton = new Button("Borrow a Book");
        borrowBookButton.setPrefWidth(300);
        Button returnBookButton = new Button("Return a Book");
       returnBookButton.setPrefWidth(300);
        Button exitButton = new Button("Exit");
        exitButton.setPrefWidth(60);

       // viewBooksButton.setMaxWidth(Double.MAX_VALUE);
       // borrowBookButton.setMaxWidth(Double.MAX_VALUE);
       // returnBookButton.setMaxWidth(Double.MAX_VALUE);
       // exitButton.setMaxWidth(Double.MAX_VALUE);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 40; -fx-background-color: #cc9966;");
        layout.getChildren().addAll(greeting, viewBooksButton, borrowBookButton, returnBookButton, exitButton);

        Scene scene = new Scene(layout,1380, 750);
        stage.setScene(scene);
        stage.setTitle("Library Main Menu");
        stage.show();

        // 🟢 Button Actions
        viewBooksButton.setOnAction(e -> new ViewAvailableBooksPage(stage, conn, user).show());
        borrowBookButton.setOnAction(e -> new BorrowBookPage(stage, conn, user).show());
        returnBookButton.setOnAction(e -> new ReturnBookPage(stage, conn, user).show());
        exitButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(" Goodbye, " + user.getName() + "! Thank you for using the system.👋");
            alert.showAndWait();
            stage.close();
        });
    }
}
