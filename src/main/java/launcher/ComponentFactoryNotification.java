package launcher;

import controller.BookController;
import controller.LoginController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.order.OrderRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImplementation;
import service.order.OrderService;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImplementation;
import service.user.UserService;
import view.BookView;
import view.LoginView;

import java.sql.Connection;

public class ComponentFactoryNotification {
    private final LoginView loginView;
    private final LoginController loginController;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private static volatile ComponentFactoryNotification instance;
    private final BookView bookView;
    //private final BookController bookController;
    private final UserService userService;
    private final OrderService orderService;

    public static ComponentFactoryNotification getInstance(Boolean componentsForTests, Stage stage) {
        if (instance == null) {
            synchronized (ComponentFactoryNotification.class) {
                if (instance == null) {
                    instance = new ComponentFactoryNotification(componentsForTests, stage);
                }
            }
        }
        return instance;
    }

    public ComponentFactoryNotification(Boolean componentsForTests, Stage stage) {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTests).getConnection();
        //AUTH + USER
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository =  new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.userService = new UserService(userRepository);
        this.authenticationService = new AuthenticationServiceImplementation(userRepository, rightsRolesRepository);

        this.orderService = new OrderService(new OrderRepositoryMySQL(connection));
        //BOOK
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImplementation(bookRepository);
        //BOOK VIEW + CONTROLLER
        this.bookView = new BookView(stage, BookMapper.convertBookListToDTOList(bookService.findAll()));
        //this.bookController = new BookController(bookView, bookService);
        //LOGIN VIEW + CONTROLLER
        this.loginView = new LoginView(stage);
        this.loginController = new LoginController(loginView, authenticationService, stage, bookService, userService, connection);
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public LoginController getLoginController() {
        return loginController;
    }
}
