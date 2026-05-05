package snake.test;

import snake.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FruitTest {

    private Snake snake;
    private Fruit fruit;

    @Before
    public void setUp() {
        snake = new Snake();
        fruit = new Fruit(snake);
    }

    @Test
    public void testFruitHasPositionAfterConstruction() {
        assertNotNull(fruit.getPosition());
    }

    @Test
    public void testFruitPositionAlignedToTileSize() {
        Position p = fruit.getPosition();
        assertEquals(0, p.getX() % GameVariables.TILE_SIZE);
        assertEquals(0, p.getY() % GameVariables.TILE_SIZE);
    }

    @Test
    public void testFruitPositionInBounds() {
        Position p = fruit.getPosition();
        assertTrue(p.getX() >= 0 && p.getX() < GameVariables.WIDTH);
        assertTrue(p.getY() >= 0 && p.getY() < GameVariables.HEIGHT);
    }

    @Test
    public void testFruitDoesNotSpawnOnSnake() {
        for (int i = 0; i < 50; i++) {
            fruit = new Fruit(snake);
            assertFalse(snake.getPositions().contains(fruit.getPosition()));
        }
    }

    @Test
    public void testMoveRelocatesFruit() {
        Position original = fruit.getPosition();
        boolean changed = false;
        for (int i = 0; i < 50; i++) {
            fruit.move();
            if (!fruit.getPosition().equals(original)) {
                changed = true;
                break;
            }
        }
        assertTrue("Fruit should relocate after move()", changed);
    }

    @Test
    public void testMoveProducesAlignedPosition() {
        for (int i = 0; i < 20; i++) {
            fruit.move();
            Position p = fruit.getPosition();
            assertEquals(0, p.getX() % GameVariables.TILE_SIZE);
            assertEquals(0, p.getY() % GameVariables.TILE_SIZE);
        }
    }

    @Test
    public void testSetPositionWorks() {
        Position p = new Position(0, 0);
        fruit.setPosition(p);
        assertEquals(p, fruit.getPosition());
    }
}
