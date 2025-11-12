package repository;

public abstract class BookRepositoryDecorator implements BookRepository {
    protected BookRepository decoratedbookRepository;

    public BookRepositoryDecorator(BookRepository bookRepository) {
        decoratedbookRepository = bookRepository;
    }
}
