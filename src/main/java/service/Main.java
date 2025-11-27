package service;

import com.mysql.cj.jdbc.JdbcConnection;
import controller.LoginController;
import database.JDBConnectionWrapper;
import javafx.application.Application;
import javafx.stage.Stage;
import model.validation.UserValidator;
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

import static database.Constants.Schemas.PRODUCTION;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception{

        //dependintele din aplicatie - dependecy injection tree
        final Connection connection = new JDBConnectionWrapper(PRODUCTION).getConnection();

        final RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        final UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        final AuthenticationService authenticationService = new AuthenticationServiceImplementation(userRepository, rightsRolesRepository);

        final BookRepository bookRepository = new BookRepositoryMySQL(connection);
        final BookService bookService = new BookServiceImplementation(bookRepository);

        final LoginView loginView = new LoginView(primaryStage);

        new LoginController(loginView, authenticationService, primaryStage, bookService);
    }
}
