package launcher;

import controller.LoginController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImplementation;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImplementation;
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
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository =  new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceImplementation(userRepository, rightsRolesRepository);
        this.loginView = new LoginView(stage);
        this.loginController = new LoginController(loginView, authenticationService);
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImplementation(bookRepository);
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
