package blackjack.test;
import blackjack.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DealerTest {

    @Test 
    public void testHoleCardRevealed() {
        Dealer dealer = new Dealer(); 
        Deck deck = new Deck();
        deck.shuffle();

        dealer.dealInitial(deck);

        assertFalse(dealer.isHoleCardRevealed());
    }

    @Test 
    public void testRevealHoleCard() {
        Dealer dealer = new Dealer(); 
        Deck deck = new Deck();
        deck.shuffle();

        dealer.dealInitial(deck);
        dealer.revealHoleCard();

        assertTrue(dealer.isHoleCardRevealed());
    }
    
    @Test 
    public void testDealerHandGreaterThan17() {
        Dealer dealer = new Dealer(); 
        Deck deck = new Deck();
        deck.shuffle();

        dealer.dealInitial(deck);
        dealer.playTurn(deck);

        assertTrue(dealer.getHandValue() >= 17);
    }
    
    @Test 
    public void testClearHand() {
        Dealer dealer = new Dealer(); 
        Deck deck = new Deck();
        deck.shuffle();

        dealer.dealInitial(deck);
        dealer.clearHand();

        assertTrue(dealer.isHoleCardRevealed()); // checks if next card is still facedown
    }
    
    @Test 
    public void testVisibleHandLessThanOrEqual() {
        Dealer dealer = new Dealer(); 
        Deck deck = new Deck();
        deck.shuffle();

        dealer.dealInitial(deck);

        assertTrue(dealer.getVisibleHandValue() <= dealer.getHandValue()); // if dealer cards normal
    }
}