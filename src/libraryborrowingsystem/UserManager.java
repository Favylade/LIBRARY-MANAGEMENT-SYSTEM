/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryborrowingsystem;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {

    private Connection conn;

    public UserManager(Connection conn) {
        this.conn = conn;
    }

    public User loginDirect(String userId) {
        try {
            String query = "SELECT * FROM users WHERE userID = ?";
            var pstmt = conn.prepareStatement(query);
            pstmt.setString(1, userId);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("name"), rs.getString("email"), rs.getString("phone"), userId);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            return null;
        }
    }

    public User signUpDirect(String name, String email, String phone) {
        try {
            // Generate new library-style ID
            String newUserId = generateUserId();

            // Check if user already exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE email = ? OR phone = ?";
            var checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, email);
            checkStmt.setString(2, phone);
            var rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                return null;
            }

            // Insert user
            String insertQuery = "INSERT INTO users (userID, name, email, phone) VALUES (?, ?, ?, ?)";
            var pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, newUserId);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.executeUpdate();

            return new User(name, email, phone, newUserId);

        } catch (SQLException e) {
            System.err.println("Sign-up error: " + e.getMessage());
            return null;
        }
    }
    // Login without Scanner (for JavaFX)

    public User login(String userId) {
        try {
            String query = "SELECT * FROM users WHERE userID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("userID")
                );
                System.out.println("Login successful: " + user.getName());
                return user;
            } else {
                System.out.println("Login failed: User not found.");
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

// Sign up without Scanner (for JavaFX)
public User signUp(String name, String email, String phone) {
    try {
        // 🔍 Check if email already exists
        String checkEmailSql = "SELECT 1 FROM users WHERE email = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql);
        checkStmt.setString(1, email);

        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            // Email already exists
            return null;
        }

        // Generate user ID
        String userID = "LIBU" + (int) (Math.random() * 10000);

        // Insert new user
        String insertSql =
                "INSERT INTO users (name, email, phone, userID) VALUES (?, ?, ?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
        insertStmt.setString(1, name);
        insertStmt.setString(2, email);
        insertStmt.setString(3, phone);
        insertStmt.setString(4, userID);
        insertStmt.executeUpdate();

        return new User(name, email, phone, userID);

    } catch (SQLException e) {
        System.err.println("Sign-up error: " + e.getMessage());
        return null;
    }
}


    // 🟢 LOGIN METHOD
    public User login(Scanner input) {
        System.out.print("Enter your User ID (e.g., LIBU123): ");
        String userId = input.nextLine();

        try {
            String query = "SELECT * FROM users WHERE userID = ?";
            var pstmt = conn.prepareStatement(query);
            pstmt.setString(1, userId);
            var rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");

                System.out.println("✅ Login successful! Welcome back, " + name + ".");
                return new User(name, email, phone, userId); // user now carries their unique ID
            } else {
                System.out.println("❌ Invalid User ID. Please try again or sign up.");
                return null;
            }

        } catch (SQLException e) {
            System.err.println("⚠️ Error during login: " + e.getMessage());
            return null;
        }
    }

    // 🟢 SIGNUP METHOD (with duplicate check)
    public User signUp(Scanner input) {
        System.out.print("Enter your full name: ");
        String name = input.nextLine();

        System.out.print("Enter your email: ");
        String email = input.nextLine();

        System.out.print("Enter your phone number: ");
        String phone = input.nextLine();

        // ✅ Generate a proper library-style ID like LIBU001
        String newUserId = generateUserId();

        try {
            // 🔍 STEP 1: Check if the user already exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE email = ? OR phone = ?";
            var checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, email);
            checkStmt.setString(2, phone);
            var rs = checkStmt.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                System.out.println("⚠️ A user with this email or phone number already exists. Please log in instead.");
                return null; // Stop signup here
            }

            // 🟢 STEP 2: Proceed with inserting the new user
            String insertQuery = "INSERT INTO users (userID, name, email, phone) VALUES (?, ?, ?, ?)";
            var pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, newUserId);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.executeUpdate();

            System.out.println("\n✅ Sign up successful!");
            System.out.println("Your library user ID is: " + newUserId);
            System.out.println("Please keep this ID safe for future logins.\n");

            return new User(name, email, phone, newUserId);

        } catch (SQLException e) {
            System.err.println("⚠️ Error during sign-up: " + e.getMessage());
            return null;
        }
    }

    // 🧩 Helper to generate IDs like LIBU001, LIBU002, ...
    private String generateUserId() {
        try {
            String query = "SELECT COUNT(*) AS total FROM users";
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);
            int count = 0;
            if (rs.next()) {
                count = rs.getInt("total");
            }
            int next = count + 1;
            return String.format("LIBU%03d", next);
        } catch (SQLException e) {
            System.err.println("⚠️ Error generating user ID: " + e.getMessage());
            return "LIBU000";
        }
    }

    // ✅ (Optional but helpful) — retrieve a User object directly by ID
    public User getUserById(String userId) {
        try {
            String query = "SELECT * FROM users WHERE userID = ?";
            var pstmt = conn.prepareStatement(query);
            pstmt.setString(1, userId);
            var rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                return new User(name, email, phone, userId);
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error fetching user: " + e.getMessage());
        }
        return null;
    }
}
