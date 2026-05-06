package gameManager.tests;
import gameManager.ScoreEntry;

import org.junit.Test;
import static org.junit.Assert.*;

public class ScoreEntryTest {

    @Test
    public void constructor_storesUsernameAndScore() {
        ScoreEntry entry = new ScoreEntry("alice", 42);
        assertEquals("alice", entry.getUsername());
        assertEquals(42, entry.getScore());
    }

    @Test
    public void setScore_updatesScore() {
        ScoreEntry entry = new ScoreEntry("bob", 10);
        entry.setScore(99);
        assertEquals(99, entry.getScore());
    }

    @Test
    public void constructor_allowsZeroScore() {
        ScoreEntry entry = new ScoreEntry("carol", 0);
        assertEquals(0, entry.getScore());
    }

    @Test
    public void constructor_allowsNegativeScore() {
        ScoreEntry entry = new ScoreEntry("dave", -5);
        assertEquals(-5, entry.getScore());
    }
}