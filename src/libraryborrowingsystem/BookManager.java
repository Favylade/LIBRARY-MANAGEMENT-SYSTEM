/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryborrowingsystem;



import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookManager {
    private final Connection conn;

    public BookManager(Connection conn) {
        this.conn = conn;
    }

    // 🟢 1. Fetch all available books for viewing
    public List<BookFX> getAvailableBooks() {
        List<BookFX> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE isAvailable = TRUE";

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                books.add(new BookFX(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getBoolean("isAvailable")
                ));
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error fetching available books: " + e.getMessage());
        }
        return books;
    }
/**
 * Attempt to borrow a book for the given userID.Returns true if borrowed
 successfully, false otherwise.
     * @param title
     * @param userId
     * @return 
 */
public boolean borrowBook(String title, String userId) {
    String checkSql = "SELECT id FROM books WHERE title = ? AND isAvailable = TRUE";
    String updateSql = "UPDATE books SET isAvailable = FALSE WHERE id = ?";
    String insertHistory = "INSERT INTO borrow_history (userID, title, borrowDate, dueDate, isDamaged, fineAmount) VALUES (?, ?, ?, ?, FALSE, 0)";

    try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
        checkStmt.setString(1, title);
        try (ResultSet rs = checkStmt.executeQuery()) {
            if (!rs.next()) {
                // book not found or not available
                return false;
            }

            int bookId = rs.getInt("id");
            java.time.LocalDate borrowDate = java.time.LocalDate.now();
            java.time.LocalDate dueDate = borrowDate.plusDays(30);

            // update book availability
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();
            }

            // insert borrow history (use userId passed in)
            try (PreparedStatement insertStmt = conn.prepareStatement(insertHistory)) {
                insertStmt.setString(1, userId);
                insertStmt.setString(2, title);
                insertStmt.setDate(3, java.sql.Date.valueOf(borrowDate));
                insertStmt.setDate(4, java.sql.Date.valueOf(dueDate));
                insertStmt.executeUpdate();
            }

            // success
            System.out.println("\n✅ You borrowed: " + title);
            System.out.println("Due date: " + dueDate);
            return true;
        }
    } catch (java.sql.SQLException e) {
        // friendly message; technical message to stderr
        System.out.println("⚠️ Could not borrow the book right now. Please try again later.");
        System.err.println("Error in borrowBook(): " + e.getMessage());
        return false;
    }
}


    // 🟢 3. Fetch books borrowed by a specific user (for ReturnBookPage dropdown)
    public List<String> getBorrowedBooksByUser(String userId) {
        List<String> borrowedBooks = new ArrayList<>();
        String query = "SELECT bookTitle FROM borrow_history WHERE userID = ? AND returnDate IS NULL";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                borrowedBooks.add(rs.getString("bookTitle"));
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error fetching borrowed books: " + e.getMessage());
        }

        return borrowedBooks;
    }

    // 🟢 4. Return a book
    public boolean returnBook(String title, String userId) {
        try {
            // Mark book as available again
            String updateBook = "UPDATE books SET isAvailable = TRUE WHERE title = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateBook);
            updateStmt.setString(1, title);
            updateStmt.executeUpdate();

            // Update borrow history to include return date
            String updateHistory = "UPDATE borrow_history SET returnDate = ? WHERE userID = ? AND bookTitle = ? AND returnDate IS NULL";
            PreparedStatement historyStmt = conn.prepareStatement(updateHistory);
            historyStmt.setDate(1, Date.valueOf(LocalDate.now()));
            historyStmt.setString(2, userId);
            historyStmt.setString(3, title);
            historyStmt.executeUpdate();

            System.out.println("✅ Book returned successfully!");
            return true;

        } catch (SQLException e) {
            System.err.println("⚠️ Error returning book: " + e.getMessage());
            return false;
        }
    }

    // 🟢 5. Add a new book (optional for Admin)
    public boolean addBook(String title, String author, String genre) {
        String query = "INSERT INTO books (title, author, genre, isAvailable) VALUES (?, ?, ?, TRUE)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, genre);
            pstmt.executeUpdate();
            System.out.println("✅ Book added successfully!");
            return true;
        } catch (SQLException e) {
            System.err.println("⚠️ Error adding book: " + e.getMessage());
            return false;
        }
    }

    // 🟢 6. Utility: check if a book exists (optional validation)
    public boolean doesBookExist(String title) {
        String query = "SELECT COUNT(*) AS total FROM books WHERE title = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt("total") > 0;
        } catch (SQLException e) {
            System.err.println("⚠️ Error checking book existence: " + e.getMessage());
            return false;
        }
    }
}
