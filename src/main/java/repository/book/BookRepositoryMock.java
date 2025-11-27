package repository.book;

import model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository {

    private final List<Book> books;

    public BookRepositoryMock() {
        books = new ArrayList<>();    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public boolean update(Book book) {
        if(book.getId() == null) return false;

        for (int i = 0; i < books.size(); i++) {
            Book existingBook = books.get(i);
            if (book.getId().equals(existingBook.getId())) {
                books.set(i, book);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean delete(Book book) {
        return books.remove(book);
    }

    @Override
    public void removeAll() {
        books.clear();
    }
}