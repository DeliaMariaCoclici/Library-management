package model;

import java.time.LocalDate;

public class Book {
    private Long id;
    private String title;
    private String author;
    private LocalDate publishedDate;

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Book: Id: " + id + ", Title: " + title + ", Author: " + author + ", PublishedDate: " + publishedDate;
    }
}