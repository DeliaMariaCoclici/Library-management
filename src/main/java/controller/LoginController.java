package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.User;
import model.validation.Notification;
import model.validation.UserValidator;
import service.user.AuthenticationService;
import view.LoginView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    public LoginController(LoginView loginView, AuthenticationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;

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