package gameManager;

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
import javafx.scene.control.ToolBar;
import javafx.geometry.Insets;
import java.util.ArrayList;
import java.util.Comparator;

public class GameManager extends Application {
    private Stage stage;
    private BorderPane root;
    private ArrayList<ScoreEntry> blackjackHighScores = new ArrayList<>();
    private ArrayList<ScoreEntry> snakeHighScores = new ArrayList<>();

    private String currentUser = null;

    @Override
    public void start(Stage primaryStage){
        this.root = new BorderPane();
        this.stage = primaryStage;

        loadTempScores();
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

        loginButton.setOnAction(e -> loginScreen());
        createAccountButton.setOnAction(e -> createAccountScreen());

        VBox centerBox = new VBox(20, title, subtitle, loginButton, createAccountButton);
        centerBox.setAlignment(Pos.CENTER);

        root.setTop(null);
        root.setCenter(centerBox);
    }

    // log in
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
        Button backButton = new Button("Back");

        loginButton.setPrefWidth(180);
        backButton.setPrefWidth(180);

        loginButton.setOnAction( e -> {
            if (usernameField.getText().isBlank() || passwordField.getText().isBlank()) {
                infoLabel.setText("Enter username and password.");
            } else {
                currentUser = usernameField.getText();
                mainMenu();
            }
        });

        backButton.setOnAction(e -> startScreen());

        HBox buttonRow = new HBox(10, loginButton, backButton);
        buttonRow.setAlignment(Pos.CENTER);

        VBox box = new VBox(15, title, usernameField, passwordField, buttonRow, infoLabel);
        box.setAlignment(Pos.CENTER);

        root.setTop(null);
        root.setCenter(box);
    }

    // create account
    private void createAccountScreen() {
        Label title = new Label("Create ACcount");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(200);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(200);

        Button createButton = new Button("Create");
        Button backButton = new Button("Back");

        createButton.setPrefWidth(120);
        backButton.setPrefWidth(120);

        // temp log in screen
        createButton.setOnAction(e -> loginScreen());
        backButton.setOnAction(e -> startScreen());

        HBox buttonRow = new HBox(10, createButton, backButton);
        buttonRow.setAlignment(Pos.CENTER);

        VBox box = new VBox(15, title, usernameField, passwordField, buttonRow);
        box.setAlignment(Pos.CENTER);

        root.setTop(null);
        root.setCenter(box);
    }

    // shared toolbar
    private ToolBar buildToolBar() {
        Button mainMenuButton = new Button("Main Menu");
        Button logoutButton = new Button("Log Out");

        Label userLabel = new Label("User: " + currentUser);

        mainMenuButton.setOnAction(e -> mainMenu());
        logoutButton.setOnAction(e -> {
            currentUser = null;
            startScreen();
        });

        return new ToolBar(mainMenuButton, logoutButton, userLabel);
        
    }

    private void loadTempScores() {
        blackjackHighScores.clear();
        snakeHighScores.clear();

        blackjackHighScores.add(new ScoreEntry("Jane", 1000));
        snakeHighScores.add(new ScoreEntry("John", 1000));
    }

    private VBox buildScoreList(ArrayList<ScoreEntry> scores) {
        VBox scoreBox = new VBox(5);

        for (int i = 0; i < 5; i++) {
            if (i < scores.size()) {
                ScoreEntry entry = scores.get(i);
                scoreBox.getChildren().add(new Label((i + 1) + ". " + entry.getUsername() + " - " + entry.getScore()));
            } else {
                scoreBox.getChildren().add(new Label((i + 1) + ". ----"));
            }
        } 
        return scoreBox;
    }

    private void mainMenu() {
        root.setTop(buildToolBar());

        for (int i = 0; i < blackjackHighScores.size(); i++) {
            for (int j = i + 1; j < blackjackHighScores.size(); j++) {
                if (blackjackHighScores.get(j).getScore() > blackjackHighScores.get(i).getScore()) {
                    // swap so reverse order
                    ScoreEntry temp = blackjackHighScores.get(i);
                    blackjackHighScores.set(i, blackjackHighScores.get(j));
                    blackjackHighScores.set(j, temp);
                }
            }
        }
        for (int i = 0; i < snakeHighScores.size(); i++) {
            for (int j = i + 1; j < snakeHighScores.size(); j++) {
                if (snakeHighScores.get(j).getScore() > snakeHighScores.get(i).getScore()) {
                    // swap so reverse order
                    ScoreEntry temp = snakeHighScores.get(i);
                    snakeHighScores.set(i, snakeHighScores.get(j));
                    snakeHighScores.set(j, temp);
                }
            }
        }

        Label scoreTitle = new Label("Top 5 High Scores");
        scoreTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label blackjackTitle = new Label("Blackjack");
        blackjackTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        VBox blackjackScores = buildScoreList(blackjackHighScores);

        Label snakeTitle = new Label("Snake");
        snakeTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        VBox snakeScores = buildScoreList(snakeHighScores);

        VBox scoresPane = new VBox(15, scoreTitle, blackjackTitle, blackjackScores, snakeTitle, snakeScores);
        scoresPane.setPadding(new Insets(20));
        scoresPane.setPrefWidth(350);

        Button blackjackButton = new Button("Open Blackjack");
        Button snakeButton = new Button("Open Snake");
        blackjackButton.setPrefWidth(200);
        snakeButton.setPrefWidth(200);

        blackjackButton.setOnAction(e -> blackjackGame());
        snakeButton.setOnAction(e -> snakeGame());

        VBox launcherPane = new VBox(15, blackjackButton, snakeButton);
        launcherPane.setAlignment(Pos.CENTER);
        launcherPane.setPadding(new Insets(20));
        launcherPane.setPrefWidth(350);

        HBox content = new HBox(40, scoresPane, launcherPane);
        content.setAlignment(Pos.CENTER);

        root.setCenter(content);
    }

    // games
    private void snakeGame() {
        root.setTop(buildToolBar());

        Label title = new Label("Snake Placeholder");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        VBox box = new VBox(20, title);
        box.setAlignment(Pos.CENTER);

        root.setCenter(box);
    }

    private void blackjackGame() {
        root.setTop(buildToolBar());

        Label title = new Label("Blackjack Placeholder");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        VBox box = new VBox(20, title);
        box.setAlignment(Pos.CENTER);

        root.setCenter(box);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

