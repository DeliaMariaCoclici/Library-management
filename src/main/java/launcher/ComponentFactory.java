package launcher;

import controller.BookController;
import database.DatabaseConnectionFactory;
import database.JDBConnectionWrapper;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImplementation;
import service.order.OrderService;
import service.order.OrderServiceImplementation;
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
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    private ComponentFactory(Boolean componentsForTest, Stage primaryStage) {
        JDBConnectionWrapper connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest);
        Connection connection = connectionWrapper.getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImplementation(bookRepository);
        this.orderRepository = new OrderRepositoryMySQL(connection);
        this.orderService = new OrderServiceImplementation(orderRepository);
        List<BookDTO> books = BookMapper.convertBookListToDTOList(bookService.findAll());
        this.bookView = new BookView(primaryStage, books);
        this.bookController = new BookController(bookView, bookService, orderService, null);
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
