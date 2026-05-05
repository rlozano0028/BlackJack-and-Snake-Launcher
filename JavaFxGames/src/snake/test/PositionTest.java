package snake.test;

import snake.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PositionTest {

    @Test
    public void testConstructorStoresCoordinates() {
        Position p = new Position(100, 200);
        assertEquals(100, p.getX());
        assertEquals(200, p.getY());
    }

    @Test
    public void testEqualsReturnsTrueForSameCoords() {
        Position a = new Position(50, 75);
        Position b = new Position(50, 75);
        assertEquals(a, b);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentX() {
        assertNotEquals(new Position(10, 20), new Position(11, 20));
    }

    @Test
    public void testEqualsReturnsFalseForDifferentY() {
        assertNotEquals(new Position(10, 20), new Position(10, 21));
    }

    @Test
    public void testEqualsNullReturnsFalse() {
        assertNotEquals(null, new Position(0, 0));
    }

    @Test
    public void testEqualsWrongTypeReturnsFalse() {
        Position p = new Position(1, 2);
        assertNotEquals("not a position", p);
    }

    @Test
    public void testEqualsReflexive() {
        Position p = new Position(5, 5);
        assertEquals(p, p);
    }

    @Test
    public void testHashCodeConsistentWithEquals() {
        Position a = new Position(42, 99);
        Position b = new Position(42, 99);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testHashCodeDiffersForDifferentCoords() {
        assertNotEquals(new Position(1, 2).hashCode(), new Position(2, 1).hashCode());
    }

    @Test
    public void testZeroAndNegativeCoords() {
        Position p = new Position(-5, 0);
        assertEquals(-5, p.getX());
        assertEquals(0, p.getY());
    }
}
