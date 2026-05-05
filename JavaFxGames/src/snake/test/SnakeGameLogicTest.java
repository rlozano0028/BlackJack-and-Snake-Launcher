package snake.test;

import snake.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SnakeGameLogicTest {

    private SnakeGameLogic logic;

    @Before
    public void setUp() {
        logic = new SnakeGameLogic();
    }

    @Test
    public void testNotGameOverInitially() {
        assertFalse(logic.isGameOver());
    }

    @Test
    public void testNotPausedInitially() {
        assertFalse(logic.isPaused());
    }

    @Test
    public void testInitialScore() {
        assertEquals(0, logic.getScore());
    }

    @Test
    public void testInitialSpeed() {
        assertEquals(100.0, logic.getSpeed(), 0.001);
    }

    @Test
    public void testGetSnakeNonNull() {
        assertNotNull(logic.getSnake());
    }

    @Test
    public void testGetFruitNonNull() {
        assertNotNull(logic.getFruit());
    }

    @Test
    public void testTogglePausePauses() {
        logic.togglePause();
        assertTrue(logic.isPaused());
    }

    @Test
    public void testTogglePauseTwiceUnpauses() {
        logic.togglePause();
        logic.togglePause();
        assertFalse(logic.isPaused());
    }

    @Test
    public void testUpdateReturnsFalseWhenPaused() {
        logic.togglePause();
        assertFalse(logic.update());
    }

    @Test
    public void testSetDirectionDelegates() {
        Direction current = logic.getSnake().getDirection();
        for (Direction d : Direction.values()) {
            if (!d.isOpposite(current) && d != current) {
                logic.setDirection(d);
                assertEquals(d, logic.getSnake().getDirection());
                return;
            }
        }
    }

    @Test
    public void testUpdateReturnsFalseWhenNoFruitEaten() {
        logic.getFruit().setPosition(findPositionAwayFromSnake(logic.getSnake()));
        assertFalse(logic.update());
    }

    @Test
    public void testUpdateReturnsTrueWhenFruitEaten() {
        placeFruitAheadOfSnake(logic);
        assertTrue(logic.update());
    }

    @Test
    public void testEatFruitIncreasesScore() {
        placeFruitAheadOfSnake(logic);
        int before = logic.getScore();
        logic.update();
        logic.update();
        assertEquals(before + 1, logic.getScore());
    }

    @Test
    public void testEatFruitDecreasesSpeed() {
        placeFruitAheadOfSnake(logic);
        double before = logic.getSpeed();
        logic.update();
        assertEquals(before - 3, logic.getSpeed(), 0.001);
    }

    @Test
    public void testSpeedNeverGoesBelowFloor() {
        for (int i = 0; i < 20; i++) {
            placeFruitAheadOfSnake(logic);
            logic.update();
            if (logic.isGameOver()) break;
        }
        assertTrue("Speed should not drop below 50 ms", logic.getSpeed() >= 50.0);
    }

    @Test
    public void testUpdateDoesNothingWhenGameOver() {
        driveIntoWall(logic);
        assertTrue(logic.isGameOver());
        assertFalse(logic.update());
    }

    @Test
    public void testTogglePauseNoEffectAfterGameOver() {
        driveIntoWall(logic);
        logic.togglePause();
        assertFalse(logic.isPaused());
    }

    @Test
    public void testRestartClearsGameOver() {
        driveIntoWall(logic);
        logic.restart();
        assertFalse(logic.isGameOver());
    }

    @Test
    public void testRestartResetsScore() {
        placeFruitAheadOfSnake(logic);
        logic.update();
        logic.restart();
        assertEquals(0, logic.getScore());
    }

    @Test
    public void testRestartResetsSpeed() {
        placeFruitAheadOfSnake(logic);
        logic.update();
        logic.restart();
        assertEquals(100.0, logic.getSpeed(), 0.001);
    }

    @Test
    public void testRestartClearsPaused() {
        logic.togglePause();
        logic.restart();
        assertFalse(logic.isPaused());
    }

    private void placeFruitAheadOfSnake(SnakeGameLogic l) {
        Snake snake = l.getSnake();
        Direction dir = snake.getDirection();
        Position ahead = new Position(
            snake.getHead().getX() + dir.getDx() * GameVariables.TILE_SIZE,
            snake.getHead().getY() + dir.getDy() * GameVariables.TILE_SIZE
        );
        l.getFruit().setPosition(ahead);
    }

    private Position findPositionAwayFromSnake(Snake snake) {
        Direction dir = snake.getDirection();
        return new Position(
            snake.getHead().getX() - dir.getDx() * GameVariables.TILE_SIZE * 5,
            snake.getHead().getY() - dir.getDy() * GameVariables.TILE_SIZE * 5
        );
    }

    private void driveIntoWall(SnakeGameLogic l) {
        l.getFruit().setPosition(new Position(0, 0));
        int maxMoves = GameVariables.COLS + GameVariables.ROWS + 10;
        for (int i = 0; i < maxMoves; i++) {
            l.update();
            if (l.isGameOver()) return;
        }
        fail("Snake did not reach game over within " + maxMoves + " moves");
    }
}
