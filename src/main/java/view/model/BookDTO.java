package view.model;

import javafx.beans.property.*;

public class BookDTO {

    private StringProperty author;
    private StringProperty title;
    private IntegerProperty stock;
    private DoubleProperty price;

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

    public void setStock(Integer stock) { stockProperty().set(stock); }
    public Integer getStock() { return stockProperty().get(); }

    public IntegerProperty stockProperty() {
        if (stock == null) {
            stock = new SimpleIntegerProperty(this, "stock");
        }
        return stock;
    }

    public void setPrice(Double price) { priceProperty().set(price); }
    public Double getPrice() { return priceProperty().get(); }

    public DoubleProperty priceProperty() {
        if (price == null) {
            price = new SimpleDoubleProperty(this, "price");
        }
        return price;
    }
}