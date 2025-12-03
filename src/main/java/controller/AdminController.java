package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import mapper.BookMapper;
import mapper.UserMapper;
import service.book.BookService;
import service.order.OrderService;
import service.order.OrderServiceImplementation;
import service.report.PDFReport;
import service.report.ReportService;
import service.report.ReportServiceImplementation;
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
    private final OrderService orderService;
    private final String loggedAdminEmail;
    private final ReportService reportService;
    private final PDFReport pdfReport = new PDFReport();

    public AdminController(BookService bookService,
                           UserService userService,
                           OrderService orderService,
                           String loggedUserEmail,
                           ReportService reportService) {
        this.bookService = bookService;
        this.userService = userService;
        this.adminStage = new Stage();
        this.adminView = new AdminView(adminStage);
        this.loggedAdminEmail = loggedUserEmail;
        this.orderService = orderService;
        this.reportService = reportService;

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

    private class ReportButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                var dto = reportService.buildLastMonthReport();
                var file = pdfReport.generate(dto, "lastMonthReport.pdf");
                System.out.println("Raport generat: " + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}