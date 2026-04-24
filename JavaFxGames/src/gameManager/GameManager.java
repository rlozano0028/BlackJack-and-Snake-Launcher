package gameManager;

import javafx.application.Application;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameManager extends Application {
    private Stage stage;
    private BorderPane root;

    private String currentUser = "temp person";

    @Override
    public void start(Stage primaryStage){
        this.root = new BorderPane();
        this.stage = primaryStage;

        startScreen();

        Scene scene = new Scene(root, 1000, 900);
        stage.setTitle("Game Manager");
        stage.setScene(scene);
        stage.show();
    }

    private void startScreen() {
        Label title = new Label("Game Manager");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label subtitle = new Label("Click a button");
        subtitle.setFont(Font.font("Arial", 12));

        Button loginButton = new Button("Log In");
        Button createAccountButton = new Button("Create account");

        loginButton.setPrefWidth(180);
        createAccountButton.setPrefWidth(180);

        loginButton.setOnAction(e -> showLoginScreen());
        createAccountButton.setOnAction(e -> showCreateAccountScreen());

        VBox centerBox = new VBox(20, title, subtitle, loginButton, createAccountButton);
        centerBox.setAlignment(Pos.CENTER);

        root.setTop(null);
        root.setCenter(centerBox);
    }

    private void loginScreen() {
        Label title = new Label("Log In");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);

        Label infoLabel = new Label("temp ui log");

        Button loginButton = new Button("Log IN");
        button backButton = new Button("Back");

        loginButton.setPrefWidth(180);
        backButton.setPrefWidth(180);

        loginButton.setOnAction( e -> {
            if (usernameField.getText().isBlack()) {
                currentUser = "Guest";
            } else {
                currentUser = usernameField.getText();
            }
        });

        backButton.setOnAction(e -> startScreen);

        HBox buttonRow = new HBox(10, loginButton, backButton);
        buttonRow.setAlignment(Pos.CENTER);

        VBox box = new VBox(15, title, usernameField, passwordField, buttonRow, infoLabel);
        box.setAlignment(Pos.CENTER);

        root.setTop(null);
        root.setCenter(box);
    }

    private void createAccountScreen() {

    }

    private ToolBar buildToolBar() {

    }

    private void mainMenu() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}

