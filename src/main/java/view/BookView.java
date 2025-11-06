package view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.model.BookDTO;

public class BookView {
    private TableView<BookDTO> bookTable;
    private final ObservableList<BookDTO> booksObservableList;
    private TextField authorTextField;
    private TextField titleTextField;
    private Label authorLabel;
    private Label titleLabel;
    private Button saveButton;
    private Button deleteButton;

    public BookView(Stage primaryStage, List<BookDTO> books) {
        primaryStage.setTitle("Library");
        GridPane gridPane = new GridPane();
        initializeGridPage(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        booksObservableList = FXCollections.observableArrayList(books);
        initTableView(gridPane);
        initSaveOptions(gridPane);

        primaryStage.show();
    }

    private void initializeGridPage(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane) {
        bookTable = new TableView<>();
        bookTable.setPlaceholder(new Label("No Books Found"));

        TableColumn<BookDTO, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookDTO, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        bookTable.getColumns().addAll(titleColumn, authorColumn);
        bookTable.setItems(booksObservableList);

        gridPane.add(bookTable, 0, 0, 5, 1);
    }

    public void initSaveOptions(GridPane gridPane) {
        titleLabel = new Label("Title");
        gridPane.add(titleLabel, 1, 1);

        titleTextField = new TextField();
        gridPane.add(titleTextField, 2, 1);

        authorLabel = new Label("Author");
        gridPane.add(authorLabel, 3, 1);

        authorTextField = new TextField();
        gridPane.add(authorTextField, 4, 1);

        saveButton = new Button("Save");
        gridPane.add(saveButton, 5, 1);

        deleteButton = new Button("Delete");
        gridPane.add(deleteButton, 6, 1);
    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener) {
        saveButton.setOnAction(saveButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener) {
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addDisplayAlertMessage(String title, String header, String contextInfo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(contextInfo);
        alert.showAndWait();
    }

    public String getTitle() {
        return titleTextField.getText();
    }

    public String getAuthor() {
        return authorTextField.getText();
    }

    public void addBookToObservableList(BookDTO bookDTO) {
        booksObservableList.add(bookDTO);
    }

    public void removeBookFromObservableList(BookDTO bookDTO) {
        booksObservableList.remove(bookDTO);
    }

    public TableView<BookDTO> getBookTableView() {
        return bookTable;
    }
}