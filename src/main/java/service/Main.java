package service;

import controller.LoginController;
import database.JDBConnectionWrapper;
import javafx.application.Application;
import javafx.stage.Stage;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImplementation;
import service.order.OrderService;
import service.order.OrderServiceImplementation;
import service.report.ReportService;
import service.report.ReportServiceImplementation;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImplementation;
import service.user.UserServiceImplementation;
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
        final UserServiceImplementation userService = new UserServiceImplementation(userRepository, rightsRolesRepository);
        final AuthenticationService authenticationService = new AuthenticationServiceImplementation(userRepository, rightsRolesRepository);

        final BookRepository bookRepository = new BookRepositoryMySQL(connection);
        final BookService bookService = new BookServiceImplementation(bookRepository);

        // ORDER
        final OrderRepository orderRepository = new OrderRepositoryMySQL(connection);
        final OrderService orderService = new OrderServiceImplementation(orderRepository);

        // REPORT
        final ReportService reportService = new ReportServiceImplementation(orderService);


        final LoginView loginView = new LoginView(primaryStage);

        new LoginController(loginView, authenticationService, bookService, userService, orderService, reportService, primaryStage);
    }
}
