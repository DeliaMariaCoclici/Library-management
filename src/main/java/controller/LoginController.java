package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import mapper.BookMapper;
import model.User;
import model.validation.Notification;
import model.validation.UserValidator;
import service.book.BookService;
import service.user.AuthenticationService;
import view.BookView;
import view.LoginView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    private final Stage loginStage;
    private final BookService bookService;

    public LoginController(LoginView loginView, AuthenticationService authenticationService, Stage loginStage, BookService bookService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.loginStage = loginStage;
        this.bookService = bookService;

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
            if(loginNotification.hasErrors()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }else{
                loginView.setActionTargetText("LogIn Successful!");
                loginView.getStage().close();

                Stage bookStage = new Stage();
                BookView bookView = new BookView(bookStage, BookMapper.convertBookListToDTOList(bookService.findAll()));

                new BookController(bookView, bookService);
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