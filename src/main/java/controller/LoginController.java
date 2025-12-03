package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import mapper.BookMapper;
import model.User;
import model.validation.Notification;
import repository.order.OrderRepositoryMySQL;
import service.book.BookService;
import service.order.OrderService;
import service.order.OrderServiceImplementation;
import service.report.ReportService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.BookView;
import view.LoginView;

import java.sql.Connection;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final BookService bookService;
    private final OrderService orderService;
    private final ReportService reportService;
    private final Stage loginStage;

    public LoginController(LoginView loginView,
                           AuthenticationService authenticationService,
                           BookService bookService,
                           UserService userService,
                           OrderService orderService,
                           ReportService reportService,
                           Stage loginStage)
 {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.bookService = bookService;
        this.userService = userService;
        this.orderService = orderService;
        this.reportService = reportService;
        this.loginStage = loginStage;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);


            //if (loginNotification == null)  -- n o sa fie niciodata null chiar daca login ul esueaza, findByUsernameAndPassword returneaza mereu un Notification cu setResul(null)
            if (loginNotification.hasErrors()) {
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            } else {
                User loggedUser = loginNotification.getResult();

                loginView.setActionTargetText("LogIn Successful!");
                loginView.getStage().close();

                if (loggedUser.getRoles().stream().anyMatch(role -> role.getRole().equals("administrator"))) {
                    new AdminController(bookService, userService, orderService, loggedUser.getUsername(), reportService);
                } else {
                    Stage bookStage = new Stage();
                    BookView bookView = new BookView(bookStage, BookMapper.convertBookListToDTOList(bookService.findAll()));
                    new BookController(bookView, bookService, orderService, loggedUser.getUsername());
                }
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}