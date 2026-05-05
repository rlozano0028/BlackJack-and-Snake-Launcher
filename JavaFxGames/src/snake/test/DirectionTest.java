package snake.test;

import snake.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DirectionTest {

    @Test
    public void testRightDeltas() {
        assertEquals(1,  Direction.RIGHT.getDx());
        assertEquals(0,  Direction.RIGHT.getDy());
    }

    @Test
    public void testLeftDeltas() {
        assertEquals(-1, Direction.LEFT.getDx());
        assertEquals(0,  Direction.LEFT.getDy());
    }

    @Test
    public void testUpDeltas() {
        assertEquals(0,  Direction.UP.getDx());
        assertEquals(-1, Direction.UP.getDy());
    }

    @Test
    public void testDownDeltas() {
        assertEquals(0,  Direction.DOWN.getDx());
        assertEquals(1,  Direction.DOWN.getDy());
    }

    @Test
    public void testRightIsOppositeLeft() {
        assertTrue(Direction.RIGHT.isOpposite(Direction.LEFT));
    }

    @Test
    public void testLeftIsOppositeRight() {
        assertTrue(Direction.LEFT.isOpposite(Direction.RIGHT));
    }

    @Test
    public void testUpIsOppositeDown() {
        assertTrue(Direction.UP.isOpposite(Direction.DOWN));
    }

    @Test
    public void testDownIsOppositeUp() {
        assertTrue(Direction.DOWN.isOpposite(Direction.UP));
    }

    @Test
    public void testRightNotOppositeUp() {
        assertFalse(Direction.RIGHT.isOpposite(Direction.UP));
    }

    @Test
    public void testNoDirectionIsOppositeItself() {
        for (Direction d : Direction.values()) {
            assertFalse(d + " should not be opposite to itself", d.isOpposite(d));
        }
    }

    @Test
    public void testRandomDirectionIsNonNull() {
        assertNotNull(Direction.getRandomDirection());
    }

    @Test
    public void testRandomDirectionIsValidValue() {
        for (int i = 0; i < 20; i++) {
            Direction d = Direction.getRandomDirection();
            assertTrue(d == Direction.UP || d == Direction.DOWN
                    || d == Direction.LEFT || d == Direction.RIGHT);
        }
    }
}
