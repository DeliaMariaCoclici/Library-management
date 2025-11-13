import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.*;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import database.DatabaseConnectionFactory;
import database.JDBConnectionWrapper;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryMySQLTest {

    private static BookRepository bookRepository;
    private static Book testBook;

    @BeforeAll
    public static void setUp() {
        JDBConnectionWrapper connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(true);
        Connection connection = connectionWrapper.getConnection();
        bookRepository = new BookRepositoryMySQL(connection);
        bookRepository.removeAll();

        testBook = new BookBuilder()
                .setTitle("Test Book")
                .setAuthor("Test Author")
                .setPublishedDate(LocalDate.of(2020, 1, 1))
                .build();

        bookRepository.save(testBook);
    }

    @Test
    public void testSave() {
        boolean saved = bookRepository.save(testBook);
        assertTrue(saved);
        assertNotNull(testBook.getId());
    }

    @Test
    public void testFindAll() {
        List<Book> books = bookRepository.findAll();
        assertEquals(1, books.size());
        assertEquals(testBook.getTitle(), books.get(0).getTitle());
    }

    @Test
    public void testFindById() {
        Optional<Book> foundBook = bookRepository.findById(testBook.getId());
        assertTrue(foundBook.isPresent());
        assertEquals(testBook.getAuthor(), foundBook.get().getAuthor());
    }

    @Test
    public void testDelete() {
        boolean deleted = bookRepository.delete(testBook);
        assertTrue(deleted);
        Optional<Book> foundBook = bookRepository.findById(testBook.getId());
        assertTrue(foundBook.isEmpty());
    }

    @AfterAll
    public static void tearDown() {
        bookRepository.removeAll();
    }
}
