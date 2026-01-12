/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryborrowingsystem;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.geometry.Pos;

public class ReturnBookPage {

    private final Stage stage;
    private final Connection conn;
    private final User user; // ✅ changed from String userId → User user

    public ReturnBookPage(Stage stage, Connection conn, User user) {
        this.stage = stage;
        this.conn = conn;
        this.user = user;
    }

    public void show() {
        // 🔹 Root layout
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #cc9966;");
        root.setAlignment(Pos.CENTER); // this centers children horizontally

        // 🔹 Title
        Label titleLabel = new Label(" Return a Book 📖");
        titleLabel.setStyle("-fx-font-size: 44px; -fx-font-weight: bold; -fx-text-fill: #2a4d69;");

        // 🔹 Input field
        Label bookTitleLabel = new Label("Enter the title of the book you want to return:");
        TextField bookTitleField = new TextField();
        bookTitleField.setPromptText("Book title...");
        bookTitleField.setMaxWidth(300);
        bookTitleField.setAlignment(Pos.CENTER);

        // 🔹 Buttons
        Button returnBtn = new Button("✅ Return Book");
        Button backBtn = new Button("⬅ Back to Menu");
        Button exitBtn = new Button("🚪 Exit");

        returnBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        backBtn.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-weight: bold;");
        exitBtn.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10, returnBtn, backBtn, exitBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // 🔹 Status message label
        Label messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        // 🔹 Layout setup
        root.getChildren().addAll(titleLabel, bookTitleLabel, bookTitleField, buttonBox, messageLabel);

        // 🔹 Button actions
        returnBtn.setOnAction(e -> {
            String title = bookTitleField.getText().trim();
            if (title.isEmpty()) {
                messageLabel.setText("⚠ Please enter the book title.");
                return;
            }

            Fine fine = returnBook(title, user.getUserID()); // ✅ fix here
            if (fine != null) {
                LocalDate returnDate = LocalDate.now();

                // Send return confirmation email
                EmailService.sendReturnConfirmationEmail(
                        user.getEmail(), // user's email
                        title,
                        returnDate
                );

                messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                messageLabel.setText(
                        "✅ Book returned successfully!\n"
                        + "📧 A confirmation email has been sent.\n"
                        + "Fine: ₦" + fine.getTotalFine()
                );

                bookTitleField.clear();
            } else {
                messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                messageLabel.setText("❌ Book not found or was not borrowed.");
            }
        });

        backBtn.setOnAction(e -> {
            MainMenuPage mainMenu = new MainMenuPage(stage, conn, user); // ✅ pass the same User object
            mainMenu.show();
        });

        exitBtn.setOnAction(e -> stage.close());

        // 🔹 Scene setup
        Scene scene = new Scene(root, 1380, 750);
        stage.setTitle("Return Book");
        stage.setScene(scene);
        stage.show();
    }

    public Fine returnBook(String title, String userID) {
        String checkHistory = "SELECT id, borrowDate, dueDate FROM borrow_history WHERE userID = ? AND title = ? AND returnDate IS NULL ORDER BY borrowDate DESC LIMIT 1";
        String updateHistory = "UPDATE borrow_history SET returnDate = ?, fineAmount = ? WHERE id = ?";
        String updateBook = "UPDATE books SET isAvailable = TRUE WHERE title = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkHistory)) {
            checkStmt.setString(1, userID);
            checkStmt.setString(2, title);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    // User didn't borrow this title or already returned
                    return null;
                }

                int historyId = rs.getInt("id");
                LocalDate dueDate = rs.getDate("dueDate").toLocalDate();
                LocalDate returnDate = LocalDate.now();

                // Calculate fine using existing Fine class
                Fine fine = new Fine(dueDate, returnDate, false);
                fine.calculateFine();
                int fineAmount = fine.getTotalFine();

                // Update borrow_history
                try (PreparedStatement updateHistStmt = conn.prepareStatement(updateHistory)) {
                    updateHistStmt.setDate(1, Date.valueOf(returnDate));
                    updateHistStmt.setInt(2, fineAmount);
                    updateHistStmt.setInt(3, historyId);
                    updateHistStmt.executeUpdate();
                }

                // Mark book as available
                try (PreparedStatement updateBookStmt = conn.prepareStatement(updateBook)) {
                    updateBookStmt.setString(1, title);
                    updateBookStmt.executeUpdate();
                }

                return fine; // return Fine object to let LibraryBorrowingSystem handle messages
            }

        } catch (SQLException e) {
            System.err.println("Error in findAndReturnByTitle(): " + e.getMessage());
            return null;
        }
    }
}

// 🔹 Method to handle returning a book
/**
 * private boolean returnBook(String title) { try { var pstmt =
 * conn.prepareStatement( "UPDATE books SET isAvailable = TRUE WHERE title = ?
 * AND isAvailable = FALSE"); pstmt.setString(1, title); int updated =
 * pstmt.executeUpdate();
 *
 * if (updated > 0) { var historyStmt = conn.prepareStatement( "INSERT INTO
 * borrow_history (userId, title, returnDate) VALUES (?, ?, ?,
 * CURRENT_TIMESTAMP)"); historyStmt.setString(1, user.getUserID());
 * historyStmt.setString(2, title); historyStmt.setString(3, "Returned");
 * historyStmt.executeUpdate();
 *
 * return true; }
 *
 * return false; } catch (SQLException e) { System.err.println("Return book
 * error: " + e.getMessage()); return false; } }
 */
