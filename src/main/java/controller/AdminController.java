package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import mapper.BookMapper;
import mapper.UserMapper;
import service.book.BookService;
import service.order.OrderService;
import service.report.PDFReport;
import service.report.ReportService;
import service.user.AuthenticationService;
import service.user.UserService;
import service.user.UserServiceImplementation;
import view.AdminView;
import view.BookView;
import view.LoginView;
import view.UsersView;
import view.model.ReportDTO;
import view.model.UserDTO;

import java.io.File;
import java.util.List;


public class AdminController {
    private final AdminView adminView;
    private final BookService bookService;
    private final UserService userService;
    private final Stage adminStage;
    private final OrderService orderService;
    private final String loggedAdminEmail;
    private final ReportService reportService;
    private final AuthenticationService authenticationService;
    private final PDFReport pdfReport = new PDFReport();

    public AdminController(BookService bookService,
                           UserService userService,
                           OrderService orderService,
                           String loggedUserEmail,
                           ReportService reportService,
                           AuthenticationService authenticationService) {
        this.bookService = bookService;
        this.userService = userService;
        this.loggedAdminEmail = loggedUserEmail;
        this.orderService = orderService;
        this.reportService = reportService;
        this.authenticationService = authenticationService;
        this.adminStage =  new Stage();
        this.adminView = new AdminView(adminStage);

        this.adminView.addBooksButtonListener(new AdminController.BookButtonListener());
        this.adminView.addUserButtonListener(new AdminController.UserButtonListener());
        this.adminView.addReportButtonListener(new AdminController.ReportButtonListener());

        adminStage.show();
    }

    private class BookButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            Stage bookStage = new Stage();
            BookView bookView = new BookView(bookStage,
                    BookMapper.convertBookListToDTOList(bookService.findAll()));
            new BookController(bookView, bookService, orderService, loggedAdminEmail );
        }
    }

    private class UserButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Stage usersStage = new Stage();
            List<UserDTO> dtoList = userService.findAll()
                    .stream()
                    .map(UserMapper::convertUserToDTO)
                    .toList();
            UsersView usersView = new UsersView(usersStage, dtoList);
            new UsersController(usersView, userService, authenticationService);
            usersStage.show();
        }
    }

    private class ReportButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                ReportDTO dto = reportService.buildLastMonthReport();
                File file = pdfReport.generate(dto, "lastMonthReport.pdf");
                System.out.println("Raport generat: " + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}