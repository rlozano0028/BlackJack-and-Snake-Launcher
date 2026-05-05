package snake.test;

import snake.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class GameVariablesTest {

    @Test
    public void testWidthEqualsColsTimesTileSize() {
        assertEquals(GameVariables.COLS * GameVariables.TILE_SIZE, GameVariables.WIDTH);
    }

    @Test
    public void testHeightEqualsRowsTimesTileSize() {
        assertEquals(GameVariables.ROWS * GameVariables.TILE_SIZE, GameVariables.HEIGHT);
    }

    @Test
    public void testTileSizeIsPositive() {
        assertTrue(GameVariables.TILE_SIZE > 0);
    }

    @Test
    public void testColsIsPositive() {
        assertTrue(GameVariables.COLS > 0);
    }

    @Test
    public void testRowsIsPositive() {
        assertTrue(GameVariables.ROWS > 0);
    }
}
