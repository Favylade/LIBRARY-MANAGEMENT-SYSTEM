/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryborrowingsystem;

import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;

public class Book {

    private int id;                 // Book ID (for database)
    private String title;
    private String author;
    private String genre;
    private boolean isAvailable;
    private LocalDate borrowDate;   // Remember when a book is borrowed

    // --- Constructor without ID (for adding new books manually) ---
    public Book(String title, String author, String genre, boolean isAvailable) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = isAvailable;
        this.borrowDate = null;
    }

    // --- Constructor with ID (for fetching from database) ---
    public Book(int id, String title, String author, String genre, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = isAvailable;
        this.borrowDate = null;
    }

    // --- Getter methods (used by other classes) ---
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // --- Setter methods (optional but useful later in GUI) ---
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // --- Display book details (for console testing) ---
    public void displayBookDetails() {
        System.out.println("ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Genre: " + genre);
        System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
    }

    // --- Borrow book ---
    public void borrowBook() {
        if (isAvailable) {
            isAvailable = false;
            borrowDate = LocalDate.now(); // Save the borrow date
            System.out.println("You borrowed: " + title + " on " + borrowDate);
        } else {
            System.out.println("Sorry, '" + title + "' is not available right now.");
        }
    }

    // --- Return book ---
    // --- Return book ---
    public void returnBook(boolean isDamaged) {
        isAvailable = true;
        LocalDate returnDate = LocalDate.now();
        System.out.println("You returned: " + title + " on " + returnDate);

        if (borrowDate != null) {
            // use 30 days as the borrow period (agreed)
            LocalDate dueDate = borrowDate.plusDays(30);

            // create Fine using date-based constructor (Fine will compute overdue days)
            Fine fine = new Fine(dueDate, returnDate, isDamaged);

            // calculate and print fine details
            fine.calculateFine();

            // if you need the numeric fine amount in code:
            int fineAmount = fine.getTotalFine(); // requires getTotalFine() in Fine class
            // (you can use fineAmount for further processing, DB updates, etc.)

        } else {
            // no borrow date recorded (book wasn't borrowed via this Book object)
            System.out.println("Note: borrow date not recorded for this book.");
        }
    }
}
