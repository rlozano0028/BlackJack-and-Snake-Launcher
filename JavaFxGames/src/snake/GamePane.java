package snake;

import java.util.ArrayList;
// import java.io.File;

import javafx.animation.KeyFrame;
// import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import javafx.scene.layout.Pane;
// import javafx.scene.layout.VBox;
// import javafx.scene.Scene;
// import javafx.stage.Stage;

import javafx.scene.control.Button;
// import javafx.scene.control.Label;

public class GamePane extends Pane {
    private Snake snake;
    private Fruit fruit;
    private FruitSpawner fruitSpawner;
    private static final int INITIAL_SPEED = 100;
    private double speed = INITIAL_SPEED;

    private Canvas canvas;
    private GraphicsContext gc;
    private Image fruitImage;
    public static final int TOP_BAR_HEIGHT = 25;

    private ArrayList<Position> snakePositions;
    private Timeline timeline;

    private boolean paused;
    private boolean gameOver;

    private Button restartButton;
    private Button exitButton;

    private Runnable onExit;

    // private static final String[] FRUIT_IMAGES = new String[]{
    // "JavaFxGames/src/images/ic_apple.png",
    // "JavaFxGames/src/images/ic_berry.png",
    // "JavaFxGames/src/images/ic_orange.png",
    // "JavaFxGames/src/images/ic_cherry.png",
    // "JavaFxGames/src/images/ic_peach.png",
    // "JavaFxGames/src/images/ic_watermelon.png"
    // };

    private static final String[] FRUIT_IMAGES = {
            "/images/ic_apple.png",
            "/images/ic_berry.png",
            "/images/ic_orange.png",
            "/images/ic_cherry.png",
            "/images/ic_peach.png",
            "/images/ic_watermelon.png"
    };

    public GamePane() {
        canvas = new Canvas(GameVariables.WIDTH, GameVariables.HEIGHT + TOP_BAR_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        snake = new Snake();
        snakePositions = snake.getPositions();

        paused = false;
        gameOver = false;

        fruitSpawner = new FruitSpawner();
        spawnNewFruit();

        getChildren().add(canvas);
        setFocusTraversable(true);

        setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP:
                    snake.setDirection(Direction.UP);
                    break;
                case DOWN:
                    snake.setDirection(Direction.DOWN);
                    break;
                case LEFT:
                    snake.setDirection(Direction.LEFT);
                    break;
                case RIGHT:
                    snake.setDirection(Direction.RIGHT);
                    break;
                case ESCAPE:
                    if(gameOver){
                        break;
                    }
                    paused = !paused;
                    if (paused) {
                        timeline.pause();
                        drawPause();
                    } else {
                        timeline.play();
                    }
                    break;
            }
        });

        createTimeline();
    }

    /** Registers a callback invoked when the player clicks "Exit" after game over. */
    public void setOnExit(Runnable handler) {
        this.onExit = handler;
    }

    /** Returns the current snake score (number of fruits eaten). */
    public int getScore() {
        return snake.getScore();
    }

    private void createTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(speed), e -> {
            snake.move();

            // check for snake eat fruit
            if (snake.getHead().equals(fruit.getPosition())) {
                snake.grow();
                spawnNewFruit();
                increaseSpeed();
            }

            if (snake.checkCollision()) {
                timeline.stop();
                gameOver();
                return;
            }

            redraw();
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        redraw();
    }

    // increase snake speed when fruit is eaten
    private void increaseSpeed() {
        speed -= 3;

        if (speed < 50) {
            speed = 50;
        }

        timeline.stop();
        createTimeline();
    }

    private void spawnNewFruit() {
        fruit = fruitSpawner.spawnFruit(snake);

        String imagePath = FRUIT_IMAGES[(int) (Math.random() * FRUIT_IMAGES.length)];
        // File fruitFile = new File(imagePath);

        // fruitImage = new Image(fruitFile.toURI().toString());
        fruitImage = new Image(getClass().getResourceAsStream(imagePath));
    }

    private void redraw() {
        drawTopBar();
        drawGrid();
        drawSnake();
        drawFruit();
        drawBorder();
    }

    private void drawTopBar() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GameVariables.WIDTH, TOP_BAR_HEIGHT);
        
        gc.setFont(javafx.scene.text.Font.font(15));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(javafx.scene.text.TextAlignment.LEFT);
        gc.setTextBaseline(javafx.geometry.VPos.CENTER);
        
        gc.fillText("Score: " + snake.getScore(), 10, TOP_BAR_HEIGHT / 2);
    }

    private void drawBorder() {
        // gc.setStroke(Color.web("#2f924b"));
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeRect(1, TOP_BAR_HEIGHT + 1, GameVariables.WIDTH - 2, GameVariables.HEIGHT - 2);
    }

    private void drawGrid() {
        for (int row = 0; row < GameVariables.ROWS; row++) {
            for (int col = 0; col < GameVariables.COLS; col++) {

                if ((row + col) % 2 == 0) {
                    gc.setFill(Color.web("#AAD751"));
                } else {
                    gc.setFill(Color.web("#A2D149"));
                }

                gc.fillRect(
                        col * GameVariables.TILE_SIZE,
                        row * GameVariables.TILE_SIZE + TOP_BAR_HEIGHT,
                        GameVariables.TILE_SIZE,
                        GameVariables.TILE_SIZE);
            }
        }
    }

    private void drawSnake() {
        // gc.setFill(Color.BLUE);
        gc.setFill(Color.web("#4371A0"));

        for (int i = 1; i <= snake.getLength(); i++) {
            Position curr = snakePositions.get(snakePositions.size() - i);
            int x = curr.getX();
            int y = curr.getY() + TOP_BAR_HEIGHT;

            gc.fillRect(x, y, GameVariables.TILE_SIZE, GameVariables.TILE_SIZE);
        }
    }

    private void drawFruit() {
        Position fruitPos = fruit.getPosition();

        gc.drawImage(fruitImage, fruitPos.getX(), fruitPos.getY() + TOP_BAR_HEIGHT, GameVariables.TILE_SIZE,
                GameVariables.TILE_SIZE);
    }

    private void drawPause() {
        gc.setFill(Color.color(0, 0, 0, 0.4));
        gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
        gc.fillRect(0, TOP_BAR_HEIGHT, GameVariables.WIDTH, GameVariables.HEIGHT);
        gc.setFont(javafx.scene.text.Font.font(36));
        gc.setFill(Color.WHITE);
        gc.fillText("PAUSED", GameVariables.WIDTH / 2.0, GameVariables.HEIGHT / 2.0 + TOP_BAR_HEIGHT);
    }

    private void gameOver() {
        // Stage gameOverStage = new Stage();
        // gameOverStage.setTitle("Game Over");

        // Label gameOverLabel = new Label("Game Over!!!");
        // Label scoreLabel = new Label("Your Score: " + snake.getScore());

        // Button restartButton = new Button("Restart");
        // restartButton.setOnAction(e -> {
        //     snake.restart();
        //     spawnNewFruit();
        //     redraw();
        //     timeline.play();
        //     gameOverStage.close();
        //     requestFocus();
        // });

        // // Need to update and have game exit properly
        // Button exitButton = new Button("Exit");
        // exitButton.setOnAction(e -> {
        //     gameOverStage.close();
        // });

        // VBox layout = new VBox(15);
        // layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        // layout.getChildren().addAll(gameOverLabel, scoreLabel, restartButton, exitButton);

        // Scene popupScene = new Scene(layout, 200, 180);
        // gameOverStage.setScene(popupScene);
        // gameOverStage.show();
        gameOver = true;
        showGameOverOverlay();
    }

    private void showGameOverOverlay() {
        gc.setFill(Color.color(0, 0, 0, 0.4));
        gc.fillRect(0, TOP_BAR_HEIGHT, GameVariables.WIDTH, GameVariables.HEIGHT);

        gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
        gc.setFill(Color.WHITE);

        gc.setFont(javafx.scene.text.Font.font(36));
        gc.fillText("GAME OVER", GameVariables.WIDTH / 2.0, GameVariables.HEIGHT / 2.0 + TOP_BAR_HEIGHT - 20);

        gc.setFont(javafx.scene.text.Font.font(20));
        gc.fillText("Score: " + snake.getScore(), GameVariables.WIDTH / 2.0, GameVariables.HEIGHT / 2.0 + TOP_BAR_HEIGHT + 20);

        restartButton = new Button("Restart");
        restartButton.setPrefWidth(80);
        restartButton.setLayoutX(GameVariables.WIDTH / 2.0 - 40);
        restartButton.setLayoutY(GameVariables.HEIGHT / 2.0 + TOP_BAR_HEIGHT + 40);

        Button exitButton = new Button("Exit");
        exitButton.setPrefWidth(80);
        exitButton.setLayoutX(GameVariables.WIDTH / 2.0 - 40);
        exitButton.setLayoutY(GameVariables.HEIGHT / 2.0 + TOP_BAR_HEIGHT + 80);
        exitButton.setOnAction(e -> {
            if (onExit != null) onExit.run();
        });

        restartButton.setOnAction(e -> {
            getChildren().removeAll(restartButton, exitButton);
            speed = INITIAL_SPEED;
            snake.restart();
            spawnNewFruit();
            gameOver = false;
            redraw();
            timeline.play();
            requestFocus();
        });

        getChildren().addAll(restartButton, exitButton);
    }
}