package view.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BookDTO {

    private StringProperty author;
    private StringProperty title;

    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public void setAuthor(String author) {
        authorProperty().set(author);
    }

    public String getAuthor() {
        return authorProperty().get();
    }

    public StringProperty authorProperty() {
        if (author == null) {
            author = new SimpleStringProperty(this, "author");
        }
        return author;
    }

    public void setTitle(String title) {
        titleProperty().set(title);
    }

    public String getTitle() {
        return titleProperty().get();
    }

    public StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty(this, "title");
        }
        return title;
    }
}