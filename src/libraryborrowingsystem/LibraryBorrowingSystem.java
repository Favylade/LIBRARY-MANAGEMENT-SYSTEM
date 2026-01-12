/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package libraryborrowingsystem;

import java.sql.Connection;
import java.util.Scanner;

public class LibraryBorrowingSystem {

    Connection conn;
    Sqlconnector sql = new Sqlconnector();
    private User user;

    public void start() {
        conn = sql.connect();
        if (conn == null) {
            System.out.println("Database connection failed. Exiting...");
            return;
        }

        Scanner input = new Scanner(System.in);

        // 🟢 1. User enters details
        System.out.println("Welcome to the Library System");
        System.out.println("1. Log in");
        System.out.println("2. Sign up");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int option = input.nextInt();
        input.nextLine();

        user = null;
        UserManager userManager = new UserManager(conn);

        if (option == 1) {
            user = userManager.login(input);
            if (user == null) {
                System.out.println("Login failed. Exiting...");
                return;
            }
        } else if (option == 2) {
            user = userManager.signUp(input);
            if (user == null) {
                System.out.println("Signup failed. Exiting...");
                return;
            }
        } else {
            System.out.println("Goodbye!");
            return;
        }

        // 🟢 User logged in or signed up successfully
        Admin admin = new Admin(conn, user.getUserID());

        boolean running = true;
        String lastAction = "main";

        while (running) {
            showMenu(lastAction);

            System.out.print("Enter your choice: ");
            int choice = input.nextInt();
            input.nextLine();

            switch (lastAction) {
                case "main":
                    switch (choice) {
                        case 1:
                            admin.displayAllBooks();
                            lastAction = "view";
                            break;
                        case 2:
                            if (borrowBookFlow(input, admin)) {
                                lastAction = "borrow";
                            } else {
                                lastAction = "view";
                            }
                            break;
                        case 3:
                            returnBookFlow(input, admin, user.getUserID());
                            lastAction = "return";
                            break;
                        case 4:
                            System.out.println("Goodbye! Thank you for using the system.");
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please select 1-4.");
                    }
                    break;

                case "view":
                    switch (choice) {
                        case 1:
                            if (borrowBookFlow(input, admin)) {
                                lastAction = "borrow";
                            } else {
                                lastAction = "view";
                            }
                            break;
                        case 2:
                            returnBookFlow(input, admin, user.getUserID());
                            lastAction = "return";
                            break;
                        case 3:
                            lastAction = "main";
                            break;
                        case 4:
                            System.out.println("Goodbye! Thank you for using the system.");
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please select 1-4.");
                    }
                    break;

                case "borrow":
                    switch (choice) {
                        case 1:
                            if (borrowBookFlow(input, admin)) {
                                lastAction = "borrow";
                            } else {
                                lastAction = "view";
                            }
                            break;
                        case 2:
                            returnBookFlow(input, admin, user.getUserID());
                            lastAction = "return";
                            break;
                        case 3:
                            lastAction = "main";
                            break;
                        case 4:
                            System.out.println("Goodbye! Thank you for using the system.");
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please select 1-4.");
                    }
                    break;

                case "return":
                    switch (choice) {
                        case 1:
                            if (borrowBookFlow(input, admin)) {
                                lastAction = "borrow";
                            } else {
                                lastAction = "view";
                            }
                            break;
                        case 2:
                            admin.displayAllBooks();
                            lastAction = "view";
                            break;
                        case 3:
                            lastAction = "main";
                            break;
                        case 4:
                            System.out.println("Goodbye! Thank you for using the system.");
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please select 1-4.");
                    }
                    break;
            }
        }

        input.close();
    }

    // 🧩 Submenu display logic
    private void showMenu(String lastAction) {
        System.out.println();
        switch (lastAction) {
            case "main":
                System.out.println("📚 What do you want to do today?");
                System.out.println("1. View available books");
                System.out.println("2. Borrow a book");
                System.out.println("3. Return a book");
                System.out.println("4. Exit");
                break;

            case "view":
                System.out.println("You’ve seen the available books. What would you like to do next?");
                System.out.println("1. Borrow a book");
                System.out.println("2. Return a book");
                System.out.println("3. Back to main menu");
                System.out.println("4. Exit");
                break;

            case "borrow":
                System.out.println("✅ You just borrowed a book. What would you like to do next?");
                System.out.println("1. Borrow another book");
                System.out.println("2. Return a book");
                System.out.println("3. Back to main menu");
                System.out.println("4. Exit");
                break;

            case "return":
                System.out.println(" What would you like to do next?");
                System.out.println("1. Borrow a book");
                System.out.println("2. View available books");
                System.out.println("3. Back to main menu");
                System.out.println("4. Exit");
                break;
        }
    }

    // 🧩 Borrow flow logic (fixed)
    private boolean borrowBookFlow(Scanner input, Admin admin) {
        System.out.print("Enter the title of the book you want to borrow: ");
        String borrowTitle = input.nextLine();

        boolean success = admin.findAndBorrowByTitle(borrowTitle, user.getUserID());

        if (!success) {
            System.out.println("❌ Book not available or not found.");
            System.out.println("Here’s a list of available books:");
            admin.displayAllBooks();
        }

        return success;
    }

    // 🧩 Return flow logic (unchanged)
    private void returnBookFlow(Scanner input, Admin admin, String userID) {
        System.out.print("Enter the title of the book you want to return: ");
        String returnTitle = input.nextLine();

        Fine fine = admin.findAndReturnByTitle(returnTitle, userID);

        if (fine == null) {
            System.out.println("❌ Book not found or not borrowed.");
        } else {
            System.out.println("\n📦 You returned a book: " + returnTitle);
            if (fine.getTotalFine() > 0) {
                System.out.println("⚠️ Late return. Fine: ₦" + fine.getTotalFine());
            } else {
                System.out.println("Returned on time. No fine.");
            }
        }
    }
}
