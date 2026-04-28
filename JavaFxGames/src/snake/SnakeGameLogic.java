package snake;

public class SnakeGameLogic implements Resettable {
    private static final double INITIAL_SPEED = 100;
    private static final double MIN_SPEED = 50;

    private Snake snake;
    private Fruit fruit;
    // private FruitSpawner fruitSpawner;
    private double speed;
    private boolean paused;
    private boolean gameOver;
    private SoundPlayer music;

    public SnakeGameLogic() {
        // fruitSpawner = new FruitSpawner();
        snake = new Snake();
        fruit = new Fruit(snake);
        music = new SoundPlayer();
        restart();
    }

    public boolean update() {
        if (paused || gameOver) {
            return false;
        }

        snake.move();

        boolean ateFruit = false;

        if (snake.getHead().equals(fruit.getPosition())) {
            snake.grow();
            spawnNewFruit();
            increaseSpeed();
            ateFruit = true;
        }

        if (snake.checkCollision()) {
            gameOver = true;
            music.stop();
        }

        return ateFruit;
    }

    private void spawnNewFruit() {
        fruit.move();
    }

    private void increaseSpeed() {
        speed -= 3;

        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }
    }

    @Override
    public void restart() {
        snake.restart();
        spawnNewFruit();
        speed = INITIAL_SPEED;
        paused = false;
        gameOver = false;
        music.stop();
        music.playLoop("/snake/SnakeMusic.wav");
    }

    public void setDirection(Direction direction) {
        snake.setDirection(direction);
    }

    public Snake getSnake() {
        return snake;
    }

    public Fruit getFruit() {
        return fruit;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void togglePause() {
        if (!gameOver) {
            paused = !paused;
        }
    }

    public int getScore() {
        return snake.getScore();
    }

}
