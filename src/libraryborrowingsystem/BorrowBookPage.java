package libraryborrowingsystem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.time.LocalDate;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;

public class BorrowBookPage {

    private final Stage stage;
    private final Connection conn;
    private final User loggedInUser;
    private final BookManager bookManager;

    public BorrowBookPage(Stage stage, Connection conn, User loggedInUser) {
        this.stage = stage;
        this.conn = conn;
        this.loggedInUser = loggedInUser;
        this.bookManager = new BookManager(conn); // ✅ create BookManager with DB connection
    }

    public void show() {
        // 🏷 Title Label
        Label titleLabel = new Label(" Borrow a Book 📚");
        titleLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");

        Label instructionLabel = new Label("Enter the title of the book you want to borrow:");
        instructionLabel.setStyle("-fx-font-size: 15px;");

        // 🔤 TextField for book title
        TextField bookTitleField = new TextField();
        bookTitleField.setPromptText("e.g., Things Fall Apart");
        bookTitleField.setMaxWidth(300);

        // 🔘 Buttons
        Button borrowBtn = new Button("Borrow Book");
        Button backBtn = new Button("⬅ Back to Menu");
        Button exitBtn = new Button("🚪 Exit");

        // Style buttons
        String redButton = "-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10; -fx-padding: 10 20;";
        borrowBtn.setStyle(redButton);
        backBtn.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10; -fx-padding: 10 20;");
        exitBtn.setStyle("-fx-background-color: #999; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10; -fx-padding: 10 20;");

        // 🔔 Status label
        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");

        // 🧠 Borrow button action
        borrowBtn.setOnAction(e -> {
            String title = bookTitleField.getText().trim();

            if (title.isEmpty()) {
                statusLabel.setText("⚠ Please enter a book title.");
                statusLabel.setStyle("-fx-text-fill: darkorange;");
                return;
            }

            boolean success = bookManager.borrowBook(title, loggedInUser.getUserID()); // ✅ use user's ID
            if (success) {
                LocalDate borrowDate = LocalDate.now();
                LocalDate dueDate = borrowDate.plusDays(30);

                // Send confirmation email
                EmailService.sendBorrowConfirmationEmail(
                        loggedInUser.getEmail(), // user email
                        title,
                        borrowDate,
                        dueDate
                );

                statusLabel.setText(
                        "✅ You have successfully borrowed \"" + title + "\".\n"
                        + "📧 A confirmation email has been sent."
                );
                statusLabel.setStyle("-fx-text-fill: green;");
                bookTitleField.clear();
            } else {
                statusLabel.setText("❌ Book not available or already borrowed.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        // 🧭 Navigation buttons
        backBtn.setOnAction(e -> new MainMenuPage(stage, conn, loggedInUser).show());
        exitBtn.setOnAction(e -> stage.close());

        // 🪟 Layout
        VBox layout = new VBox(15, titleLabel, instructionLabel, bookTitleField, borrowBtn, statusLabel, backBtn, exitBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-padding: 40; -fx-background-color: #cc9966;");

        // 🖼 Scene
        Scene scene = new Scene(layout, 1380, 750);
        stage.setScene(scene);
        stage.setTitle("Borrow a Book");
        stage.show();
    }
}
