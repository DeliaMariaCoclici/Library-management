package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import service.book.BookService;
import view.BookView;
import view.builder.BookDTOBuilder;
import view.model.BookDTO;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;

    public BookController(BookView bookView, BookService bookService) {
        this.bookView = bookView;
        this.bookService = bookService;
        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();
            if (title.isEmpty() || author.isEmpty()) {
                bookView.addDisplayAlertMessage("Save error", "Problem at fields", "Cant have an empty space");
            } else {
                BookDTO bookDTO = new BookDTOBuilder().setTitle(title).setAuthor(author).build();
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