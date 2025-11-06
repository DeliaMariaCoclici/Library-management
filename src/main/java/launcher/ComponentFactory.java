package launcher;

import controller.BookController;
import database.DatabaseConnectionFactory;
import database.JDBConnectionWrapper;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.BookRepository;
import repository.BookRepositoryMySQL;
import service.BookService;
import service.BookServiceImplementation;
import view.BookView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

public class ComponentFactory {
    private static ComponentFactory instance;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final BookView bookView;
    private final BookController bookController;

    private ComponentFactory(Boolean componentsForTest, Stage primaryStage) {
        JDBConnectionWrapper connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest);
        Connection connection = connectionWrapper.getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImplementation(bookRepository);
        List<BookDTO> books = BookMapper.convertBookListToDTOList(bookService.findAll());
        this.bookView = new BookView(primaryStage, books);
        this.bookController = new BookController(bookView, bookService);
    }

    public static synchronized ComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage) {
        if (instance == null) {
            instance = new ComponentFactory(componentsForTest, primaryStage);
        }
        return instance;
    }

    public BookView getBookView() {
        return bookView;
    }

    public BookController getBookController() {
        return bookController;
    }
}
