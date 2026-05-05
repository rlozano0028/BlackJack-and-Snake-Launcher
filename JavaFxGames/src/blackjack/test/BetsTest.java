package blackjack.test;
import blackjack.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BetsTest {
    
    @Test
    public void testPlaceBets() {
        Bets bets = new Bets(100);

        assertTrue(bets.placeBet(20));
        assertEquals(80, bets.getTotal(), 0.001);
        assertEquals(20, bets.getCurrentBet(), 0.001);
    }

    @Test
    public void testCannotOverBet() {
        Bets bets = new Bets(50);
        
        assertFalse(bets.placeBet(100));
        assertEquals(50, bets.getTotal(), 0.001);
    }

    @Test
    public void testRaiseBet() {
        Bets bets = new Bets(100);

        bets.placeBet(20);
        assertTrue(bets.raiseBet(10));

        assertEquals(70, bets.getTotal(), 0.001);
        assertEquals(30, bets.getCurrentBet(), 0.001);
    }

    @Test
    public void testBlackjack() {
        Bets bets = new Bets(100);

        bets.placeBet(20);
        bets.win();

        assertEquals(120, bets.getTotal(), 0.001);
        assertEquals(0, bets.getCurrentBet(), 0.001);
    }
}