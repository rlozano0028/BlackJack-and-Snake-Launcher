package blackjack.test;
import blackjack.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTest {
    
    @Test 
    public void testBlackjack() {
        Player p = new Player("John", 100);

        p.receiveCard(new Card("Hearts", "A"));
        p.receiveCard(new Card("Club", "K"));

        assertTrue(p.hasBlackjack());
    }

    @Test 
    public void testBust() {
        Player p = new Player("John", 100);

        p.receiveCard(new Card("Hearts", "K"));
        p.receiveCard(new Card("Spade", "5"));
        p.receiveCard(new Card("Diamond", "Q"));
        
        assertTrue(p.isBust());
    }

    @Test 
    public void testAceValue() {
        Player p = new Player("John", 100);
        
        p.receiveCard(new Card("Hearts", "A"));
        p.receiveCard(new Card("Club", "9"));
        p.receiveCard(new Card("Hearts", "8")); // this changes val of A = 11 to A=1

        assertEquals(18, p.getHandValue());
    }
}
