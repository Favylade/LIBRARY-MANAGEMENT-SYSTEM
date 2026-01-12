/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryborrowingsystem;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BookFX {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleStringProperty genre;
    private final SimpleBooleanProperty isAvailable;

    // ✅ Constructor
    public BookFX(int id, String title, String author, String genre, boolean isAvailable) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleStringProperty(genre);
        this.isAvailable = new SimpleBooleanProperty(isAvailable);
    }

    // ✅ Getters (for TableView)
    public int getId() {
        return id.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getGenre() {
        return genre.get();
    }

    public boolean isAvailable() {
        return isAvailable.get();
    }

    // ✅ Property getters (for JavaFX bindings)
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public SimpleStringProperty genreProperty() {
        return genre;
    }

    public SimpleBooleanProperty isAvailableProperty() {
        return isAvailable;
    }

    // ✅ Optional: toString() (useful for debugging)
    @Override
    public String toString() {
        return String.format("BookFX{id=%d, title='%s', author='%s', genre='%s', available=%s}",
                getId(), getTitle(), getAuthor(), getGenre(), isAvailable());
    }
}

