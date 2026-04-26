package gameManager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

import javafx.stage.Stage;
import blackjack.Main;
import snake.GamePane;

public class GameManager extends Application {

    // -----------------------------------------------------------------------
    // State
    // -----------------------------------------------------------------------
    private BorderPane root;

    private final AccountService accountService = new AccountService();
    private final ScoreService   scoreService   = new ScoreService();

    private String   currentUser          = null;
    private GamePane currentSnakePane     = null;
    private Main     currentBlackjackMain = null;

    // -----------------------------------------------------------------------
    // JavaFX entry point
    // -----------------------------------------------------------------------
    @Override
    public void start(Stage primaryStage) {
        this.root = new BorderPane();

        startScreen();

        Scene scene = new Scene(root, 1000, 900);
        primaryStage.setTitle("Game Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // -----------------------------------------------------------------------
    // Pre-login screens  (no toolbar)
    // -----------------------------------------------------------------------

    private void startScreen() {
        root.setTop(null);

        Label title    = new Label("Game Manager");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        Label subtitle = new Label("Please log in or create an account.");
        subtitle.setFont(Font.font("Arial", 14));

        Button loginBtn  = new Button("Log In");
        Button createBtn = new Button("Create Account");
        loginBtn.setPrefWidth(200);
        createBtn.setPrefWidth(200);

        loginBtn.setOnAction(e -> loginScreen());
        createBtn.setOnAction(e -> createAccountScreen());

        VBox box = new VBox(20, title, subtitle, loginBtn, createBtn);
        box.setAlignment(Pos.CENTER);
        root.setCenter(box);
    }

    private void loginScreen() {
        root.setTop(null);

        Label title = new Label("Log In");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));

        TextField     usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");
        usernameField.setMaxWidth(250);
        passwordField.setMaxWidth(250);

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginBtn = new Button("Log In");
        Button backBtn  = new Button("Back");
        loginBtn.setPrefWidth(180);
        backBtn.setPrefWidth(180);

        loginBtn.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText();

            if (user.isBlank() || pass.isBlank()) {
                errorLabel.setText("Please enter both username and password.");
                return;
            }
            if (accountService.fileIsMissing()) {
                errorLabel.setText("No accounts exist. Please create one.");
                return;
            }
            if (!accountService.validateLogin(user, pass)) {
                errorLabel.setText("Invalid username or password.");
                return;
            }
            currentUser = user;
            mainMenu();
        });

        backBtn.setOnAction(e -> startScreen());

        HBox buttonRow = new HBox(10, loginBtn, backBtn);
        buttonRow.setAlignment(Pos.CENTER);

        VBox box = new VBox(15, title, usernameField, passwordField, buttonRow, errorLabel);
        box.setAlignment(Pos.CENTER);
        root.setCenter(box);
    }

    private void createAccountScreen() {
        root.setTop(null);

        Label title = new Label("Create Account");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));

        TextField     usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");
        usernameField.setMaxWidth(250);
        passwordField.setMaxWidth(250);

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");

        Button createBtn = new Button("Create");
        Button backBtn   = new Button("Back");
        createBtn.setPrefWidth(180);
        backBtn.setPrefWidth(180);

        createBtn.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText();

            if (user.isBlank() || pass.isBlank()) {
                errorLabel.setText("Please enter both username and password.");
                return;
            }
            if (user.contains(":") || pass.contains(":")) {
                errorLabel.setText("Username/password may not contain ':'.");
                return;
            }
            boolean created = accountService.createAccount(user, pass);
            if (!created) {
                errorLabel.setText("Username already taken.");
                return;
            }
            scoreService.ensureDefaultScore(user);
            loginScreen();
        });

        backBtn.setOnAction(e -> startScreen());

        HBox buttonRow = new HBox(10, createBtn, backBtn);
        buttonRow.setAlignment(Pos.CENTER);

        VBox box = new VBox(15, title, usernameField, passwordField, buttonRow, errorLabel);
        box.setAlignment(Pos.CENTER);
        root.setCenter(box);
    }

    // -----------------------------------------------------------------------
    // Post-login navigation
    // -----------------------------------------------------------------------

    /**
     * Saves any active game score, clears the game references, then shows the
     * main menu. This is the single correct way to navigate back to main menu.
     */
    private void goToMainMenu() {
        if (currentSnakePane != null) {
            int score = currentSnakePane.getScore();
            scoreService.updateScore("SNAKE", currentUser, score);
            currentSnakePane = null;
        }
        if (currentBlackjackMain != null) {
            int score = currentBlackjackMain.getCurrentScore();
            scoreService.updateScore("BLACKJACK", currentUser, score);
            currentBlackjackMain = null;
        }
        mainMenu();
    }

    private void mainMenu() {
        root.setTop(buildToolBar());

        // --- Scores pane (left) ---
        List<ScoreEntry> bjScores    = scoreService.getTopScores("BLACKJACK", 5);
        List<ScoreEntry> snakeScores = scoreService.getTopScores("SNAKE", 5);

        Label scoreTitle = new Label("Top 5 High Scores");
        scoreTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label bjTitle = new Label("Blackjack");
        bjTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label snakeTitle = new Label("Snake");
        snakeTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        VBox scoresPane = new VBox(
                15, scoreTitle,
                bjTitle,    buildScoreList(bjScores),
                snakeTitle, buildScoreList(snakeScores)
        );
        scoresPane.setPadding(new Insets(20));
        scoresPane.setPrefWidth(350);

        // --- Launcher pane (right) ---
        Label launchTitle = new Label("Play a Game");
        launchTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button bjBtn    = new Button("Open Blackjack");
        Button snakeBtn = new Button("Open Snake");
        Button ph1      = new Button("Coming Soon");
        Button ph2      = new Button("Coming Soon");

        bjBtn.setPrefWidth(200);
        snakeBtn.setPrefWidth(200);
        ph1.setPrefWidth(200);
        ph2.setPrefWidth(200);

        ph1.setDisable(true);
        ph2.setDisable(true);

        bjBtn.setOnAction(e -> blackjackGame());
        snakeBtn.setOnAction(e -> snakeGame());

        VBox launcherPane = new VBox(15, launchTitle, bjBtn, snakeBtn, ph1, ph2);
        launcherPane.setAlignment(Pos.CENTER);
        launcherPane.setPadding(new Insets(20));
        launcherPane.setPrefWidth(350);

        HBox content = new HBox(40, scoresPane, launcherPane);
        content.setAlignment(Pos.CENTER);

        root.setCenter(content);
    }

    private VBox buildScoreList(List<ScoreEntry> scores) {
        VBox box = new VBox(5);
        for (int i = 0; i < 5; i++) {
            if (i < scores.size()) {
                ScoreEntry e = scores.get(i);
                box.getChildren().add(new Label((i + 1) + ". " + e.getUsername() + " - " + e.getScore()));
            } else {
                box.getChildren().add(new Label((i + 1) + ". ----"));
            }
        }
        return box;
    }

    // -----------------------------------------------------------------------
    // Persistent toolbar
    // -----------------------------------------------------------------------

    private ToolBar buildToolBar() {
        Button mainMenuBtn = new Button("Main Menu");
        Button logoutBtn   = new Button("Log Out");
        Label  userLabel   = new Label("User: " + currentUser);

        mainMenuBtn.setOnAction(e -> goToMainMenu());

        logoutBtn.setOnAction(e -> {
            // Save any open game score before logging out
            if (currentSnakePane != null) {
                scoreService.updateScore("SNAKE", currentUser, currentSnakePane.getScore());
                currentSnakePane = null;
            }
            if (currentBlackjackMain != null) {
                scoreService.updateScore("BLACKJACK", currentUser, currentBlackjackMain.getCurrentScore());
                currentBlackjackMain = null;
            }
            currentUser = null;
            startScreen();
        });

        return new ToolBar(mainMenuBtn, logoutBtn, userLabel);
    }

    // -----------------------------------------------------------------------
    // Game views
    // -----------------------------------------------------------------------

    private void snakeGame() {
        root.setTop(buildToolBar());

        currentSnakePane = new GamePane();
        currentSnakePane.setOnExit(() -> goToMainMenu());

        root.setCenter(currentSnakePane);
        currentSnakePane.requestFocus();
    }

    private void blackjackGame() {
        root.setTop(buildToolBar());

        currentBlackjackMain = new Main();
        BorderPane bjView = currentBlackjackMain.buildView();

        root.setCenter(bjView);
    }

    // -----------------------------------------------------------------------
    // Application entry point
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        launch(args);
    }
}
