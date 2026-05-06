package blackjack.test;
import blackjack.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlackjackGameTest {
    
    @Test 
    public void testPlaceBetBeforeRound() {
        BlackjackGame game = new BlackjackGame();

        assertTrue(game.humanRaiseBet(10));
        assertEquals(90, game.getHuman().getAccount().getTotal(), 0.001);
        assertEquals(10, game.getHuman().getAccount().getCurrentBet(), 0.001);
    }

    @Test 
    public void testCantChangeBetMidGame() {
        BlackjackGame game = new BlackjackGame();

        assertTrue(game.humanRaiseBet(10));
        assertTrue(game.startRound(10));

        double bankrollBefore = game.getHuman().getAccount().getTotal();
        double betBefore = game.getHuman().getAccount().getCurrentBet();

        assertFalse(game.humanRaiseBet(20));

        assertEquals(bankrollBefore, game.getHuman().getAccount().getTotal(), 0.001);
        assertEquals(betBefore, game.getHuman().getAccount().getCurrentBet(), 0.001);
        
    }
    
    @Test 
    public void testCantDealWithoutBet() {
        BlackjackGame game = new BlackjackGame();
        
        assertFalse(game.startRound(0));
        assertFalse(game.isRoundActive());
    }
}
