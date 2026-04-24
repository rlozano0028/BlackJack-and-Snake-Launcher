package snake;

import java.util.ArrayList;
import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GamePane extends Pane {
    private Snake snake;
    private Fruit fruit;
    private FruitSpawner fruitSpawner;
    private double speed = 100;

    private Canvas canvas;
    private GraphicsContext gc;
    private Image fruitImage;
    public static final int TOP_BAR_HEIGHT = 25;

    private ArrayList<Position> snakePositions;
    // private Direction direction = Direction.RIGHT;
    private Timeline timeline;

    // private static final String[] FRUIT_IMAGES = new String[]{
    //     "JavaFxGames/src/images/ic_apple.png",
    //     "JavaFxGames/src/images/ic_berry.png",
    //     "JavaFxGames/src/images/ic_orange.png",
    //     "JavaFxGames/src/images/ic_cherry.png",
    //     "JavaFxGames/src/images/ic_peach.png",
    //     "JavaFxGames/src/images/ic_watermelon.png"
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
            }
        });

        createTimeline();
    }

    private void createTimeline(){
        timeline = new Timeline(new KeyFrame(Duration.millis(speed), e -> {
            // switch (direction) {
            //     case UP:
            //         snake.moveUp();
            //         break;
            //     case DOWN:
            //         snake.moveDown();
            //         break;
            //     case LEFT:
            //         snake.moveLeft();
            //         break;
            //     case RIGHT:
            //         snake.moveRight();
            //         break;
            //}
            snake.move();

            //check for snake eat fruit
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

    //increase snake speed when fruit is eaten
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

        String imagePath = FRUIT_IMAGES[(int)(Math.random() * FRUIT_IMAGES.length)];
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

    // private void drawGrid() {
    //     gc.setFill(Color.BLACK);
    //     gc.fillRect(0, 0, GameVariables.WIDTH, GameVariables.HEIGHT);

    //     gc.setStroke(Color.WHITE);
    //     gc.setLineWidth(0.5);

    //     for (int x = 0; x <= GameVariables.WIDTH; x += GameVariables.TILE_SIZE) {
    //         gc.strokeLine(x, 0, x, GameVariables.HEIGHT);
    //     }

    //     for (int y = 0; y <= GameVariables.HEIGHT; y += GameVariables.TILE_SIZE) {
    //         gc.strokeLine(0, y, GameVariables.WIDTH, y);
    //     }
    // }

    private void drawTopBar(){
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GameVariables.WIDTH, TOP_BAR_HEIGHT);

        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + snake.getScore(), 10, 15);
    }

    private void drawBorder(){
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
                    GameVariables.TILE_SIZE
                );
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

        gc.drawImage(fruitImage, fruitPos.getX(), fruitPos.getY() + TOP_BAR_HEIGHT, GameVariables.TILE_SIZE, GameVariables.TILE_SIZE);
    }

    private void gameOver() {
        Stage gameOverStage = new Stage();
        gameOverStage.setTitle("Game Over");

        Label gameOverLabel = new Label("Game Over!!!");
        Label scoreLabel = new Label("Your Score: " + snake.getScore());

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> {
            snake.restart();
            spawnNewFruit();
            redraw();
            timeline.play();
            gameOverStage.close();
            requestFocus(); 
        });

        //Need to update and have game exit properly
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            gameOverStage.close();
        });

        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(gameOverLabel, scoreLabel, restartButton, exitButton);

        Scene popupScene = new Scene(layout, 200, 180);
        gameOverStage.setScene(popupScene);
        gameOverStage.show();
    }
}