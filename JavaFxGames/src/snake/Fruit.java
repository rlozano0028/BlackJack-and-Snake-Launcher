package snake;
import java.util.Random;
public class Fruit extends GameObject {
    
    private Snake snake;
    Random random = new Random();

    public Fruit(Snake snake) {
        
        this.snake = snake;
        this.position = generateFruitPosition();
    }

    private Position generateFruitPosition() {
        int x, y;
        Position newPosition;

        //ensures fruit doesn't spawn on snake
       do{
            x = random.nextInt(GameVariables.WIDTH / GameVariables.TILE_SIZE) * GameVariables.TILE_SIZE;
            y = random.nextInt(GameVariables.HEIGHT / GameVariables.TILE_SIZE) * GameVariables.TILE_SIZE;
            newPosition = new Position(x, y);
        } while (snake.getPositions().contains(newPosition));
        
        return newPosition;
    }
    @Override
    public void move() {
        Position position = generateFruitPosition();
        this.position = position;
    }
}
