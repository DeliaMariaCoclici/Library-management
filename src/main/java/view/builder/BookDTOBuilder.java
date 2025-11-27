package view.builder;

import view.model.BookDTO;

public class BookDTOBuilder {
    private BookDTO bookDTO;

    public BookDTOBuilder() {
        bookDTO = new BookDTO();
    }

    public BookDTOBuilder setAuthor(String author) {
        bookDTO.setAuthor(author);
        return this;
    }

    public BookDTOBuilder setTitle(String title) {
        bookDTO.setTitle(title);
        return this;
    }

    public BookDTOBuilder setId(Long id) {
        bookDTO.setId(id);
        return this;
    }

    public BookDTOBuilder setPrice(Double price) {
        bookDTO.setPrice(price);
        return this;
    }

    public BookDTOBuilder setStock(Integer stock) {
        bookDTO.setStock(stock);
        return this;
    }

    public BookDTO build() {
        return bookDTO;
    }
}
