package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import mapper.BookMapper;
import mapper.UserMapper;
import service.book.BookService;
import service.order.OrderServiceImplementation;
import service.user.UserService;
import view.AdminView;
import view.BookView;
import view.UsersView;
import view.model.UserDTO;

import java.util.List;


public class AdminController {
    private final AdminView adminView;
    private final BookService bookService;
    private final UserService userService;
    private final Stage adminStage;
    private final OrderServiceImplementation orderService;
    private final String loggedAdminEmail;

    public AdminController(BookService bookService, UserService userService, OrderServiceImplementation orderService, String loggedUserEmail) {
        this.bookService = bookService;
        this.userService = userService;
        this.adminStage = new Stage();
        this.adminView = new AdminView(adminStage);
        this.loggedAdminEmail = loggedUserEmail;
        this.orderService = orderService;

        this.adminView.addBooksButtonListener(new AdminController.BookButtonListener());
        this.adminView.addUserButtonListener(new AdminController.UserButtonListener());

        adminStage.show();
    }

    private class BookButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            Stage bookStage = new Stage();
            BookView bookView = new BookView(bookStage,
                    BookMapper.convertBookListToDTOList(bookService.findAll()));
            new BookController(bookView, bookService,orderService, loggedAdminEmail );

            //sa inchid admin page cand deschid book view
        }
    }

    private class UserButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Stage userStage = new Stage();

            List<UserDTO> userDTOList = userService.findAll()
                    .stream()
                    .map(UserMapper::convertUserToDTO)
                    .toList();

            UsersView userView = new UsersView(userStage, userDTOList);
            userStage.show();   /////pop-up, daca vrei sa se inchida adaugi adminStage.close();
        }
    }
}