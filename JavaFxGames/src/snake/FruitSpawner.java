package snake;

import java.util.Random;

public class FruitSpawner {
    private Random random;

    //spawns fruit in random position
    public FruitSpawner() {
        random = new Random();
    }

    public Position generateFruitPosition(Snake snake) {
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

    public Fruit spawnFruit(Snake snake) {
        Position position = generateFruitPosition(snake);
        return new Fruit(position);
    }
}
