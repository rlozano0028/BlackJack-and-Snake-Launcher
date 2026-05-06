package blackjack.test;
import blackjack.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeckTest {

    @Test 
    public void testDrawCard() {
        Deck deck = new Deck();
        deck.shuffle();

        Card c1 = deck.draw();
        Card c2 = deck.draw();

        assertNotNull(c1);
        assertNotNull(c2);
    }

    @Test 
    public void testDrawAllCards() {
        Deck deck = new Deck();
        deck.shuffle();

        for (int i = 0; i < 52; i++) {
            assertNotNull(deck.draw());
        }
        assertNull(deck.draw()); // chcks if empty
    }
}