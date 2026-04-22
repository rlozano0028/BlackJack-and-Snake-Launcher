package snake;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GamePane extends Pane {
    private Snake snake;
    private Fruit fruit;
    private FruitSpawner fruitSpawner;
    private Canvas canvas;
    private GraphicsContext gc;
    private ArrayList<Position> snakePositions;
    private Direction direction = Direction.RIGHT;

    public GamePane() {
        canvas = new Canvas(GameVariables.WIDTH, GameVariables.HEIGHT);
        gc = canvas.getGraphicsContext2D();
        snake = new Snake();
        fruitSpawner = new FruitSpawner();
        fruit = fruitSpawner.spawnFruit(snake);
        snakePositions = snake.getPositions();
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

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
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
}