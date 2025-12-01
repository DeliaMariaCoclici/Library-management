package launcher;

import controller.BookController;
import database.DatabaseConnectionFactory;
import database.JDBConnectionWrapper;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.order.OrderRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImplementation;
import service.order.OrderService;
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
    private final OrderService orderService;
    private String loggedUserEmail;

    private ComponentFactory(Boolean componentsForTest, Stage primaryStage, String loggedUserEmail) {
        JDBConnectionWrapper connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest);
        Connection connection = connectionWrapper.getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImplementation(bookRepository);
        this.orderService = new OrderService(new OrderRepositoryMySQL(connection));
        this.loggedUserEmail = loggedUserEmail;
        List<BookDTO> books = BookMapper.convertBookListToDTOList(bookService.findAll());
        this.bookView = new BookView(primaryStage, books);
        this.bookController = new BookController(bookView, bookService, orderService, loggedUserEmail);
    }

    public static synchronized ComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage, String loggedUserEmail) {
        if (instance == null) {
            instance = new ComponentFactory(componentsForTest, primaryStage, loggedUserEmail);
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
