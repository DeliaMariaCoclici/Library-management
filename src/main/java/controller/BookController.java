package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import model.Order;
import service.book.BookService;
import service.order.OrderService;
import service.order.OrderServiceImplementation;
import view.BookView;
import view.builder.BookDTOBuilder;
import view.model.BookDTO;

public class BookController {

    private final BookView bookView;
    private final BookService bookService;

    private final OrderService orderService;
    private final String loggedEmployeeEmail;

    public BookController(BookView bookView, BookService bookService, OrderService orderService, String loggedEmployeeEmail) {
        this.bookView = bookView;
        this.bookService = bookService;
        this.orderService = orderService;
        this.loggedEmployeeEmail = loggedEmployeeEmail;
        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
        this.bookView.addBuyButtonListener(new BuyButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();
            String stockStr = bookView.getStock();
            String priceStr = bookView.getPrice();

            Integer stock;
            Double price;

            if (title.isEmpty() || author.isEmpty() || stockStr.isEmpty() || priceStr.isEmpty()) {
                bookView.addDisplayAlertMessage("Save error", "Problem at fields", "All fields must be filled");
            } else {
                try {
                    stock = Integer.parseInt(stockStr);
                    price = Double.parseDouble(priceStr);
                } catch (NumberFormatException e) {
                    bookView.addDisplayAlertMessage("Save error", "Data Format Problem", "Stock and price must be valid numbers");
                    return;
                }

                if (stock < 0) {
                    bookView.addDisplayAlertMessage("Save error", "Data Format Problem", "Stock must be positive");
                    return;
                }

                BookDTO bookDTO = new BookDTOBuilder()
                        .setTitle(title)
                        .setAuthor(author)
                        .setStock(stock)
                        .setPrice(price)
                        .build();

                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));
                if (savedBook) {
                    bookView.addDisplayAlertMessage("Save successful", "Book Saved", "Book Saved in Database");
                    bookView.addBookToObservableList(bookDTO);
                } else {
                    bookView.addDisplayAlertMessage("Save error", "Problem at adding book", "Try again in a few moments!!");
                }
            }
        }
    }

    private class BuyButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            BookDTO bookDTO = bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO == null) {
                bookView.addDisplayAlertMessage("Sale Error", "Book Not Selected", "Select book before buying");
                return;
            }

            String quantityStr = bookView.getQuantity();
            int quantity;

            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                bookView.addDisplayAlertMessage("Sale Error", "Quantity Error", "Please enter a valid number for quantity");
                return;
            }

            if (quantity < 0) {
                bookView.addDisplayAlertMessage("Sale Error", "Quantity Error", "Please enter a positive quantity");
                return;
            }

            Integer currentStock = bookDTO.getStock();
            if (quantity > currentStock) {
                bookView.addDisplayAlertMessage("Sale Error", "Quantity Error", "Not enough stock. Available: " + currentStock);
                return;
            }

            Integer newStock = currentStock - quantity;
            bookDTO.setStock(newStock);

            boolean updateSale = bookService.sell(BookMapper.convertBookDTOToBook(bookDTO));

            if (updateSale) {
                bookView.addDisplayAlertMessage("Sale successful", "Book Sold", "Book stock updated");
                bookView.getBookTableView().refresh();

                //inserare automata in orders
                Order order = new Order();
                order.setBookTitle(bookDTO.getTitle());
                order.setQuantity(quantity);
                order.setEmployeeEmail(loggedEmployeeEmail);
                order.setTotalPrice(bookDTO.getPrice() * quantity);
                boolean saved = orderService.saveOrder(order);
                if (!saved) {
                    bookView.addDisplayAlertMessage("Order Error", "Database issue", "Could not save order.");
                }


            } else {
                bookView.addDisplayAlertMessage("Sale error", "Failed update", "Stock unable to update.");
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            BookDTO bookDTO = bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO != null) {
                boolean deletionSucc = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));
                if (deletionSucc) {
                    bookView.addDisplayAlertMessage("Delete done", "Its gone", "Successfully deleted");
                    bookView.removeBookFromObservableList(bookDTO);
                } else {
                    bookView.addDisplayAlertMessage("Delete error", "Problem at deleting book", "Try again later :(");
                }
            } else {
                bookView.addDisplayAlertMessage("Delete error", "Problem at deleting book", "Select the book first");
            }
        }
    }
}
