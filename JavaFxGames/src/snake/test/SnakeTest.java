package snake.test;

import snake.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class SnakeTest {

    private Snake snake;

    @Before
    public void setUp() {
        snake = new Snake();
    }


    @Test
    public void testInitialLength() {
        assertEquals(3, snake.getLength());
    }

    @Test
    public void testInitialPositionsSize() {
        assertEquals(3, snake.getPositions().size());
    }

    @Test
    public void testInitialScore() {
        assertEquals(0, snake.getScore());
    }

    @Test
    public void testHeadNonNullAfterConstruction() {
        assertNotNull(snake.getHead());
    }

    @Test
    public void testHeadIsLastPosition() {
        ArrayList<Position> positions = snake.getPositions();
        assertEquals(positions.get(positions.size() - 1), snake.getHead());
    }

    @Test
    public void testDirectionNonNullAfterConstruction() {
        assertNotNull(snake.getDirection());
    }


    @Test
    public void testSetDirectionChanges() {
        Direction initial = snake.getDirection();
        Direction newDir = getNonOppositeDirection(initial);
        snake.setDirection(newDir);
        assertEquals(newDir, snake.getDirection());
    }

    @Test
    public void testSetDirectionIgnoresOpposite() {
        Direction initial = snake.getDirection();
        Direction opposite = getOpposite(initial);
        snake.setDirection(opposite);
        assertEquals(initial, snake.getDirection());
    }


    @Test
    public void testMoveKeepsPositionsSize() {
        int before = snake.getPositions().size();
        snake.move();
        assertEquals(before, snake.getPositions().size());
    }

    @Test
    public void testMoveAdvancesHead() {
        Direction dir = snake.getDirection();
        Position oldHead = snake.getHead();
        snake.move();
        Position newHead = snake.getHead();
        assertEquals(oldHead.getX() + dir.getDx() * GameVariables.TILE_SIZE, newHead.getX());
        assertEquals(oldHead.getY() + dir.getDy() * GameVariables.TILE_SIZE, newHead.getY());
    }


    @Test
    public void testGrowIncreasesLength() {
        int before = snake.getLength();
        snake.grow();
        snake.move();
        assertEquals(before + 1, snake.getLength());
    }

    @Test
    public void testGrowIncreasesPositionsSize() {
        int before = snake.getPositions().size();
        snake.grow();
        snake.move();
        assertEquals(before + 1, snake.getPositions().size());
    }

    @Test
    public void testGrowOnlyAppliesOnce() {
        snake.grow();
        snake.move();
        int afterGrow = snake.getLength();
        snake.move();
        assertEquals(afterGrow, snake.getLength());
    }

    @Test
    public void testScoreIncreasesAfterGrow() {
        int before = snake.getScore();
        snake.grow();
        snake.move();
        assertEquals(before + 1, snake.getScore());
    }


    @Test
    public void testNoWallCollisionInitially() {
        assertFalse(snake.checkWallCollision());
    }

    @Test
    public void testNoSelfCollisionInitially() {
        assertFalse(snake.checkSelfCollision());
    }

    @Test
    public void testWallCollisionDetected() {
        forceDirection(snake, Direction.RIGHT);
        driveSnakeToWall(snake);
        assertTrue(snake.checkWallCollision());
    }

    @Test
    public void testCheckCollisionTrueAtWall() {
        forceDirection(snake, Direction.RIGHT);
        driveSnakeToWall(snake);
        assertTrue(snake.checkCollision());
    }


    @Test
    public void testRestartResetsLength() {
        snake.grow();
        snake.move();
        snake.restart();
        assertEquals(3, snake.getLength());
    }

    @Test
    public void testRestartResetsPositionsSize() {
        snake.grow();
        snake.move();
        snake.restart();
        assertEquals(3, snake.getPositions().size());
    }

    @Test
    public void testRestartResetsScore() {
        snake.grow();
        snake.move();
        snake.restart();
        assertEquals(0, snake.getScore());
    }

    @Test
    public void testRestartSetsNonNullHeadAndDirection() {
        snake.restart();
        assertNotNull(snake.getHead());
        assertNotNull(snake.getDirection());
    }


    @Test
    public void testSetHeadUpdatesHead() {
        Position p = new Position(100, 100);
        snake.setHead(p);
        assertEquals(p, snake.getHead());
    }


    private Direction getNonOppositeDirection(Direction d) {
        for (Direction candidate : Direction.values()) {
            if (!candidate.isOpposite(d) && candidate != d) {
                return candidate;
            }
        }
        return d;
    }

    private Direction getOpposite(Direction d) {
        switch (d) {
            case RIGHT: return Direction.LEFT;
            case LEFT:  return Direction.RIGHT;
            case UP:    return Direction.DOWN;
            case DOWN:  return Direction.UP;
            default:    throw new IllegalArgumentException("Unknown direction: " + d);
        }
    }

    private void forceDirection(Snake s, Direction target) {
        Direction current = s.getDirection();
        if (current.isOpposite(target)) {
            Direction perp = (target == Direction.RIGHT || target == Direction.LEFT)
                    ? Direction.UP : Direction.RIGHT;
            s.setDirection(perp);
            s.move();
        }
        s.setDirection(target);
    }

    private void driveSnakeToWall(Snake s) {
        int maxSteps = GameVariables.COLS + GameVariables.ROWS + 10;
        for (int i = 0; i < maxSteps; i++) {
            s.move();
            if (s.checkWallCollision()) return;
        }
    }
}
