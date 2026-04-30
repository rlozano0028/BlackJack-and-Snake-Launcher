package snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import javafx.scene.layout.Pane;

import javafx.scene.control.Button;

public class GamePane extends Pane {

    private SnakeGameLogic gameLogic;

    private Canvas canvas;
    private GraphicsContext gc;
    private Image fruitImage;
    public static final int TOP_BAR_HEIGHT = 25;

    private Timeline timeline;

    private Button restartButton;
    // private Button exitButton;

    private Runnable onExit;

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

        gameLogic = new SnakeGameLogic();
        loadRandomFruitImage();

        getChildren().add(canvas);
        setFocusTraversable(true);

        setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP:
                    gameLogic.setDirection(Direction.UP);
                    break;
                case DOWN:
                    gameLogic.setDirection(Direction.DOWN);
                    break;
                case LEFT:
                    gameLogic.setDirection(Direction.LEFT);
                    break;
                case RIGHT:
                    gameLogic.setDirection(Direction.RIGHT);
                    break;
                case ESCAPE:
                    if (gameLogic.isGameOver()) {
                        break;
                    }
                    gameLogic.togglePause();
                    if (gameLogic.isPaused()) {
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
        timeline = new Timeline(new KeyFrame(Duration.millis(gameLogic.getSpeed()), e -> {
            double oldSpeed = gameLogic.getSpeed();
            boolean ateFruit = gameLogic.update();

            if (gameLogic.isGameOver()) {
                timeline.stop();
                showGameOverOverlay();
                return;
            }

            if (ateFruit) {
                loadRandomFruitImage();
            }

            redraw();

            if (gameLogic.getSpeed() != oldSpeed) {
                timeline.stop();
                redraw();
                createTimeline();
                return;
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void loadRandomFruitImage() {
        String imagePath = FRUIT_IMAGES[(int) (Math.random() * FRUIT_IMAGES.length)];
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
        
        gc.fillText("Score: " + gameLogic.getScore(), 10, TOP_BAR_HEIGHT / 2);
    }

    private void drawBorder() {
        // gc.setStroke(Color.web("#2f924b"));
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(0, TOP_BAR_HEIGHT, GameVariables.WIDTH, GameVariables.HEIGHT);
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

        var snakePositions = gameLogic.getSnake().getPositions();
        int length = gameLogic.getSnake().getLength();

        for (int i = 1; i <= length; i++) {
            Position curr = snakePositions.get(snakePositions.size() - i);
            int x = curr.getX();
            int y = curr.getY() + TOP_BAR_HEIGHT;

            gc.fillRect(x, y, GameVariables.TILE_SIZE, GameVariables.TILE_SIZE);
        }
    }

    private void drawFruit() {
        Position fruitPos = gameLogic.getFruit().getPosition();

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

    private void showGameOverOverlay() {
        gc.setFill(Color.color(0, 0, 0, 0.4));
        gc.fillRect(0, TOP_BAR_HEIGHT, GameVariables.WIDTH, GameVariables.HEIGHT);

        gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
        gc.setFill(Color.WHITE);

        gc.setFont(javafx.scene.text.Font.font(36));
        gc.fillText("GAME OVER", GameVariables.WIDTH / 2.0, GameVariables.HEIGHT / 2.0 + TOP_BAR_HEIGHT - 20);

        gc.setFont(javafx.scene.text.Font.font(20));
        gc.fillText("Score: " + gameLogic.getScore(), GameVariables.WIDTH / 2.0, GameVariables.HEIGHT / 2.0 + TOP_BAR_HEIGHT + 20);

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
            gameLogic.restart();
            loadRandomFruitImage();
            timeline.stop();
            createTimeline();
            redraw();
            requestFocus();
        });

        getChildren().addAll(restartButton, exitButton);
    }
}