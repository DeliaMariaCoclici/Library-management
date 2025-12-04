package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import mapper.UserMapper;
import model.validation.Notification;
import service.user.AuthenticationService;
import service.user.UserService;
import view.LoginView;
import view.UsersView;
import view.model.UserDTO;

import java.util.List;

public class UsersController {
    private final UsersView usersView;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UsersController(UsersView usersView,
                           UserService userService,
                           AuthenticationService authenticationService) {
        this.usersView = usersView;
        this.userService = userService;
        this.authenticationService = authenticationService;

        usersView.addAddEmployeeButtonListener(event -> openRegisterWindow());
        usersView.addDeleteEmployeeButtonListener(event -> deleteEmployee());
    }

    private void openRegisterWindow() {
        Stage registerStage = new Stage();
        LoginView loginView = new LoginView(registerStage, true);
        new LoginController(loginView, authenticationService, null, null, null, null, registerStage);

        registerStage.setOnHiding(e -> {
            List<UserDTO> lista = userService.findAll()
                    .stream()
                    .map(UserMapper::convertUserToDTO)
                    .toList();
            usersView.refreshUserList(lista);
            usersView.setActionTargetText("Employee list updated.");
        });
    }


    private void deleteEmployee() {
        UserDTO selected = usersView.getSelectedUser();
        if (selected == null) {
            usersView.setActionTargetText("No employee selected.");
            return;
        }

        Notification<Boolean> result = userService.deleteByUsername(selected.getUsername());
        if (result.hasErrors()) {
            usersView.setActionTargetText(result.getFormattedErrors());
        } else {
            List<UserDTO> lista = userService.findAll()
                    .stream()
                    .map(UserMapper::convertUserToDTO)
                    .toList();
            usersView.refreshUserList(lista);
            usersView.setActionTargetText("Employee deleted.");
        }
    }
}