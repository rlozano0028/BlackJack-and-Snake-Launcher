package snake;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class GamePane extends Pane {
    private Snake snake;
    private Fruit fruit;
    private FruitSpawner fruitSpawner;
    private Canvas canvas;
    private GraphicsContext gc;
    private ArrayList<Position> snakePositions;
    // private Direction direction = Direction.RIGHT;
    private Timeline timeline;

    public GamePane() {
        canvas = new Canvas(GameVariables.WIDTH, GameVariables.HEIGHT);
        gc = canvas.getGraphicsContext2D();
        
        snake = new Snake();
        snakePositions = snake.getPositions();
        
        fruitSpawner = new FruitSpawner();
        fruit = fruitSpawner.spawnFruit(snake);
        
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

        timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
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
                fruit = fruitSpawner.spawnFruit(snake);
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

    private void redraw() {
        drawGrid();
        drawSnake();
        drawFruit();
    }

    private void drawGrid() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GameVariables.WIDTH, GameVariables.HEIGHT);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(0.5);

        for (int x = 0; x <= GameVariables.WIDTH; x += GameVariables.TILE_SIZE) {
            gc.strokeLine(x, 0, x, GameVariables.HEIGHT);
        }

        for (int y = 0; y <= GameVariables.HEIGHT; y += GameVariables.TILE_SIZE) {
            gc.strokeLine(0, y, GameVariables.WIDTH, y);
        }
    }

    private void drawSnake() {
        gc.setFill(Color.LIGHTGREEN);

        for (int i = 1; i <= snake.getLength(); i++) {
            Position curr = snakePositions.get(snakePositions.size() - i);
            int x = curr.getX();
            int y = curr.getY();

            gc.fillRect(x, y, GameVariables.TILE_SIZE, GameVariables.TILE_SIZE);
        }
    }

    private void drawFruit() {
        gc.setFill(Color.RED);
        Position fruitPos = fruit.getPosition();
        gc.fillOval(fruitPos.getX(), fruitPos.getY(), GameVariables.TILE_SIZE, GameVariables.TILE_SIZE);
    }

    private void gameOver() {
        // gc.setFill(Color.BLACK);
        // gc.fillRect(0, 0, GameVariables.WIDTH, GameVariables.HEIGHT);

        // gc.setFill(Color.WHITE);
        // gc.fillText("Game Over! \nScore: " + snake.getScore(), GameVariables.WIDTH / 2 - 50, GameVariables.HEIGHT / 2);

        Stage gameOverStage = new Stage();
        gameOverStage.setTitle("Game Over");

        Label gameOverLabel = new Label("Game Over!!!");
        Label scoreLabel = new Label("Your Score: " + snake.getScore());

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> {
            snake.restart();
            fruit = fruitSpawner.spawnFruit(snake);
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