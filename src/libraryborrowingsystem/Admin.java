/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryborrowingsystem;

import java.sql.*;
import java.time.LocalDate;

public class Admin {

    private final Connection conn;
    private final String userID; // ✅ add this field

    // ✅ updated constructor
    public Admin(Connection conn, String userID) {
        this.conn = conn;
        this.userID = userID;
    }

    /**
     * Show only available books (title, author, genre).
     */
    public void displayAllBooks() {
        String sql = "SELECT title, author, genre FROM books WHERE isAvailable = TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            System.out.println("\n📚 Available books:");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Author: " + rs.getString("author"));
                System.out.println("Genre: " + rs.getString("genre"));
                System.out.println("-----------------------------");
            }
            if (!found) {
                System.out.println("No available books at the moment.");
            }
        } catch (SQLException e) {
            // friendly message to user + technical to stderr for debugging
            System.out.println("⚠️ Could not retrieve books right now. Please try again later.");
            System.err.println("Error in displayAllBooks(): " + e.getMessage());
        }
    }

    /**
     * Attempt to borrow a book for the given userID. Returns true if borrowed
     * successfully, false otherwise.
     */
    public boolean findAndBorrowByTitle(String title, String userID) {
        String checkSql = "SELECT id FROM books WHERE title = ? AND isAvailable = TRUE";
        String updateSql = "UPDATE books SET isAvailable = FALSE WHERE id = ?";
        String insertHistory = "INSERT INTO borrow_history (userID, bookTitle, borrowDate, dueDate, isDamaged, fineAmount) VALUES (?, ?, ?, ?, FALSE, 0)";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, title);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    // book not found or not available
                    return false;
                }

                int bookId = rs.getInt("id");
                LocalDate borrowDate = LocalDate.now();
                LocalDate dueDate = borrowDate.plusDays(30);

                // update book availability
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, bookId);
                    updateStmt.executeUpdate();
                }

                // insert borrow history (use userID passed in)
                try (PreparedStatement insertStmt = conn.prepareStatement(insertHistory)) {
                    insertStmt.setString(1, userID);
                    insertStmt.setString(2, title);
                    insertStmt.setDate(3, Date.valueOf(borrowDate));
                    insertStmt.setDate(4, Date.valueOf(dueDate));
                    insertStmt.executeUpdate();
                }

                // success
                System.out.println("\n✅ You borrowed: " + title);
                System.out.println("Due date: " + dueDate);
                return true;
            }
        } catch (SQLException e) {
            // friendly message; technical message to stderr
            System.out.println("⚠️ Could not borrow the book right now. Please try again later.");
            System.err.println("Error in findAndBorrowByTitle(): " + e.getMessage());
            return false;
        }
    }

    /**
     * Attempt to return a book for a given userID. Returns true if return
     * accepted, false otherwise.
     */
    /**
     * Attempt to return a book for a given userID.Returns a Fine object if
 return was successful, or null if failed.
     * @param title
     * @param userID
     * @return 
     */
    public Fine findAndReturnByTitle(String title, String userID) {
        String checkHistory = "SELECT id, borrowDate, dueDate FROM borrow_history WHERE userID = ? AND bookTitle = ? AND returnDate IS NULL ORDER BY borrowDate DESC LIMIT 1";
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
