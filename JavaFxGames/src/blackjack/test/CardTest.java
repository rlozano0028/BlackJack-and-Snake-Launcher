package blackjack.test;
import blackjack.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CardTest {
    
    @Test
    public void testCardValues() {
        assertEquals(11, new Card("Hearts", "A").getNumberValue());
        assertEquals(6, new Card("Clubs", "6").getNumberValue());
        assertEquals(10, new Card("Hearts", "J").getNumberValue());
    }

    @Test 
    public void testCardGetters() {
        Card card1 = new Card("Diamond", "7");
        assertEquals("Diamond", card1.getSuit());
        assertEquals("7", card1.getRank());

        Card card2 = new Card("Hearts", "K");
        assertEquals("Hearts", card2.getSuit());
        assertEquals("K", card2.getRank());
    }
}