package view;

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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.model.UserDTO;

import java.util.List;

public class UsersView {
    private final Stage stage;
    private TableView<UserDTO> userTable;
    private final ObservableList<UserDTO> userObservableList;

    private Button addButton;
    private Button deleteButton;

    public UsersView(Stage primaryStage, List<UserDTO> users) {
        this.stage = primaryStage;
        userObservableList = FXCollections.observableList(users);

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        initializeSceneTitle(gridPane);
        initializeTable(gridPane);
        initializeFields(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeSceneTitle(GridPane gridPane){
        Text sceneTitle = new Text("Employees List");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.BOLD, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
    }

    private void initializeTable(GridPane gridPane){
        userTable = new TableView<>();
        userTable.setPlaceholder(new Label("No users found"));

        TableColumn<UserDTO, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(data -> data.getValue().usernameProperty());
        userTable.getColumns().add(usernameColumn);
        userTable.setItems(userObservableList);

        gridPane.add(userTable, 0, 1, 2, 1);
    }

    private void initializeFields(GridPane gridPane){
        addButton = new Button("Add Employee");
        HBox addButtonHBox = new HBox(10);
        addButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        addButtonHBox.getChildren().add(addButton);
        gridPane.add(addButtonHBox, 1, 4);

        deleteButton = new Button("Delete Employee");
        HBox deleteButtonHBox = new HBox(10);
        deleteButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        deleteButtonHBox.getChildren().add(deleteButton);
        gridPane.add(deleteButtonHBox, 0, 4);

    }

    public void addAddButtonListener(EventHandler<ActionEvent> listener) {
        addButton.setOnAction(listener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> listener) {
        deleteButton.setOnAction(listener);
    }

    public TableView<UserDTO> getUserTable() {
        return userTable;
    }

    public ObservableList<UserDTO> getUsersObservableList() {
        return userObservableList;
    }

    public Stage getStage() {
        return stage;
    }
}


