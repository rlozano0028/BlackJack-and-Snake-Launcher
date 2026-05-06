package gameManager.tests;
import gameManager.ScoreService;
import gameManager.ScoreEntry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class ScoreServiceTest {

    private static final String SCORES_FILE = "high_scores.txt";
    private ScoreService scoreService;

    @Before
    public void setUp() {
        deleteFile(SCORES_FILE);
        scoreService = new ScoreService();
    }

    @After
    public void tearDown() {
        deleteFile(SCORES_FILE);
    }

    // --- getUserScore ---

    @Test
    public void getUserScore_returnsZeroWhenNoFile() {
        assertEquals(0, scoreService.getUserScore("SNAKE", "alice"));
    }

    @Test
    public void getUserScore_returnsZeroForUnknownUser() {
        scoreService.updateScore("SNAKE", "alice", 100);
        assertEquals(0, scoreService.getUserScore("SNAKE", "bob"));
    }

    @Test
    public void getUserScore_returnsStoredScore() {
        scoreService.updateScore("BLACKJACK", "alice", 250);
        assertEquals(250, scoreService.getUserScore("BLACKJACK", "alice"));
    }

    @Test
    public void getUserScore_distinguishesByGame() {
        scoreService.updateScore("SNAKE",     "alice", 100);
        scoreService.updateScore("BLACKJACK", "alice", 999);
        assertEquals(100, scoreService.getUserScore("SNAKE",     "alice"));
        assertEquals(999, scoreService.getUserScore("BLACKJACK", "alice"));
    }

    // --- updateScore ---

    @Test
    public void updateScore_createsNewEntry() {
        scoreService.updateScore("SNAKE", "alice", 50);
        assertEquals(50, scoreService.getUserScore("SNAKE", "alice"));
    }

    @Test
    public void updateScore_replacesScoreWhenNewIsHigher() {
        scoreService.updateScore("SNAKE", "alice",  50);
        scoreService.updateScore("SNAKE", "alice", 150);
        assertEquals(150, scoreService.getUserScore("SNAKE", "alice"));
    }

    @Test
    public void updateScore_doesNotReplaceScoreWhenNewIsLower() {
        scoreService.updateScore("SNAKE", "alice", 200);
        scoreService.updateScore("SNAKE", "alice",  50);
        assertEquals(200, scoreService.getUserScore("SNAKE", "alice"));
    }

    @Test
    public void updateScore_doesNotReplaceScoreWhenEqual() {
        scoreService.updateScore("SNAKE", "alice", 100);
        scoreService.updateScore("SNAKE", "alice", 100);
        assertEquals(100, scoreService.getUserScore("SNAKE", "alice"));
    }

    @Test
    public void updateScore_handlesMultipleUsersIndependently() {
        scoreService.updateScore("SNAKE", "alice", 300);
        scoreService.updateScore("SNAKE", "bob",   150);
        assertEquals(300, scoreService.getUserScore("SNAKE", "alice"));
        assertEquals(150, scoreService.getUserScore("SNAKE", "bob"));
    }

    @Test
    public void updateScore_handlesZeroScore() {
        scoreService.updateScore("SNAKE", "alice", 0);
        assertEquals(0, scoreService.getUserScore("SNAKE", "alice"));
    }

    // --- getTopScores ---

    @Test
    public void getTopScores_returnsEmptyListWhenNoFile() {
        List<ScoreEntry> scores = scoreService.getTopScores("SNAKE", 5);
        assertTrue(scores.isEmpty());
    }

    @Test
    public void getTopScores_returnsEmptyListForUnknownGame() {
        scoreService.updateScore("SNAKE", "alice", 100);
        assertTrue(scoreService.getTopScores("TETRIS", 5).isEmpty());
    }

    @Test
    public void getTopScores_returnsSingleEntry() {
        scoreService.updateScore("SNAKE", "alice", 42);
        List<ScoreEntry> scores = scoreService.getTopScores("SNAKE", 5);
        assertEquals(1, scores.size());
        assertEquals("alice", scores.get(0).getUsername());
        assertEquals(42,      scores.get(0).getScore());
    }

    @Test
    public void getTopScores_sortsByScoreDescending() {
        scoreService.updateScore("SNAKE", "alice", 100);
        scoreService.updateScore("SNAKE", "bob",   300);
        scoreService.updateScore("SNAKE", "carol", 200);

        List<ScoreEntry> scores = scoreService.getTopScores("SNAKE", 5);
        assertEquals(300, scores.get(0).getScore());
        assertEquals(200, scores.get(1).getScore());
        assertEquals(100, scores.get(2).getScore());
    }

    @Test
    public void getTopScores_limitsResultCount() {
        scoreService.updateScore("SNAKE", "a", 500);
        scoreService.updateScore("SNAKE", "b", 400);
        scoreService.updateScore("SNAKE", "c", 300);
        scoreService.updateScore("SNAKE", "d", 200);
        scoreService.updateScore("SNAKE", "e", 100);
        scoreService.updateScore("SNAKE", "f",  50);

        List<ScoreEntry> top3 = scoreService.getTopScores("SNAKE", 3);
        assertEquals(3, top3.size());
        assertEquals(500, top3.get(0).getScore());
    }

    @Test
    public void getTopScores_limitLargerThanEntriesReturnsAll() {
        scoreService.updateScore("BLACKJACK", "alice", 100);
        scoreService.updateScore("BLACKJACK", "bob",   200);

        List<ScoreEntry> scores = scoreService.getTopScores("BLACKJACK", 10);
        assertEquals(2, scores.size());
    }

    @Test
    public void getTopScores_separatesBlackjackAndSnakeEntries() {
        scoreService.updateScore("SNAKE",     "alice", 100);
        scoreService.updateScore("BLACKJACK", "alice", 999);

        List<ScoreEntry> snakeScores = scoreService.getTopScores("SNAKE", 5);
        assertEquals(1, snakeScores.size());
        assertEquals(100, snakeScores.get(0).getScore());
    }

    @Test
    public void getTopScores_limitZeroReturnsEmptyList() {
        scoreService.updateScore("SNAKE", "alice", 100);
        List<ScoreEntry> scores = scoreService.getTopScores("SNAKE", 0);
        assertTrue(scores.isEmpty());
    }

    // -----------------------------------------------------------------------
    private void deleteFile(String path) {
        File f = new File(path);
        if (f.exists()) f.delete();
    }
}