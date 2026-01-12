/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryborrowingsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;

public class ViewAvailableBooksPage {

    private final Stage stage;
    private final Connection conn;
    private final User user;  // ✅ store the logged-in user
    private final BookManager bookManager;

    public ViewAvailableBooksPage(Stage stage, Connection conn, User user) { // ✅ accept User
        this.stage = stage;
        this.conn = conn;
        this.user = user;
        this.bookManager = new BookManager(conn);
    }

    public void show() {
        // 🔹 Root layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // 🔹 Page title
        Label titleLabel = new Label(" Available Books 📚");
        titleLabel.setStyle("-fx-font-size: 44px; -fx-font-weight: bold; -fx-text-fill: #2a4d69;");

        // 🔹 TableView setup
        TableView<BookFX> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<BookFX, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cell -> cell.getValue().titleProperty());

        TableColumn<BookFX, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(cell -> cell.getValue().authorProperty());

        TableColumn<BookFX, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(cell -> cell.getValue().genreProperty());

        tableView.getColumns().addAll(titleCol, authorCol, genreCol);
        root.setStyle("-fx-background-color: e1c699;");
        tableView.setStyle("-fx-background-color: #cc9966;");
        titleCol.setStyle("-fx-background-color: #cc9966;");
        authorCol.setStyle("-fx-background-color: #cc9966;");
        genreCol.setStyle("-fx-background-color: #cc9966;");

        // 🔹 Fetch data from BookManager
        List<BookFX> availableBooks = bookManager.getAvailableBooks();
        ObservableList<BookFX> data = FXCollections.observableArrayList(availableBooks);
        tableView.setItems(data);

        // 🔹 Buttons
        Button proceedBtn = new Button("➡ Proceed to Borrow");
        Button backBtn = new Button("⬅ Back to Menu");

        proceedBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        backBtn.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10, backBtn, proceedBtn);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        // 🔹 Layout
        root.setTop(titleLabel);
        root.setCenter(tableView);
        root.setBottom(buttonBox);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 15, 0));

        // 🔹 Button actions
        proceedBtn.setOnAction(e -> {
            BorrowBookPage borrowPage = new BorrowBookPage(stage, conn, user);
            borrowPage.show();
        });

        backBtn.setOnAction(e -> {
            MainMenuPage mainMenu = new MainMenuPage(stage, conn, user);
            mainMenu.show();
        });

        // 🔹 Scene setup
        Scene scene = new Scene(root, 1380, 750);
        stage.setTitle("View Available Books");
        stage.setScene(scene);
        stage.show();
    }
}