package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;

public class AdminView {
    private Button booksButton;
    private Button userButton;
    private Button reportButton;
    private final Stage stage;

    public AdminView(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("Admin");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        initializeSceneTitle(gridPane);
        initializeFields(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
        gridPane.setStyle("-fx-background-color: #ADD8E6;");
    }

    private void initializeSceneTitle(GridPane gridPane){
        Text sceneTitle = new Text("                      ADMIN PAGE SETTINGS");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.BOLD, 20));
        sceneTitle.setFill(javafx.scene.paint.Color.BLACK);
        gridPane.add(sceneTitle, 0, 0, 2, 1);
        GridPane.setHalignment(sceneTitle, HPos.CENTER);
    }

    private void initializeFields(GridPane gridPane){
        booksButton = new Button("Manage books");
        HBox booksButtonHBox = new HBox(10);
        booksButtonHBox.setAlignment(Pos.CENTER_LEFT);
        booksButtonHBox.getChildren().add(booksButton);
        gridPane.add(booksButtonHBox, 0, 4);
        booksButton.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 15;"
        );

        userButton = new Button("Manage employees");
        HBox userButtonHBox = new HBox(10);
        userButtonHBox.setAlignment(Pos.CENTER);
        userButtonHBox.getChildren().add(userButton);
        gridPane.add(userButtonHBox, 1, 4);
        userButton.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 15;"
        );

        reportButton = new Button("Manage monthly reports");
        HBox reportButtonHBox = new HBox(10);
        reportButtonHBox.setAlignment(Pos.CENTER_RIGHT);
        reportButtonHBox.getChildren().add(reportButton);
        gridPane.add(reportButtonHBox, 2, 4);
        reportButton.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 15;"
        );
    }

    public void addBooksButtonListener(EventHandler<ActionEvent> booksButtonListener) {
        booksButton.setOnAction(booksButtonListener);
    }

    public void addUserButtonListener(EventHandler<ActionEvent> userButtonListener) {
        userButton.setOnAction(userButtonListener);
    }

    public void addReportButtonListener(EventHandler<ActionEvent> reportButtonListener) {
        reportButton.setOnAction(reportButtonListener);
    }

    public Stage getStage() {
        return stage;
    }
}
