package blackjack;

import java.util.Arrays;
import java.util.Random;

public class Deck {
    private Card[] cards = new Card[52];
    private int nextCardIndex = 0;

    /** Restoring constructor: accepts a pre-ordered array of remaining cards. */
    Deck(Card[] orderedCards) {
        this.cards = Arrays.copyOf(orderedCards, orderedCards.length);
        this.nextCardIndex = 0;
    }

    public Deck() {
        String[] suits = {"Club","Diamond","Hearts","Spade"};
        String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};

        int index = 0;

        // loop card to store
        for(int i = 0; i < suits.length; i++) {
            for(int j = 0; j < ranks.length; j++) {
                cards[index] = new Card(suits[i], ranks[j]);
                index++;
            }
        }
    }

    //shuffle if null then choose another one
    public void shuffle() {
        Card[] shuffled = new Card[52];
        Random rand = new Random();
        int index;

        for (int i = 0; i < 52; i++) {
            
            // find pick unil find non null card
            do {
                index = rand.nextInt(52);
            } while (cards[index] == null);
            //choose card
            shuffled[i] = cards[index];
            // make that card null
            cards[index] = null;
        }

        // replace deck
        cards = shuffled;
        nextCardIndex = 0;
        
    }

    /** Returns a copy of all cards not yet drawn, in draw order. */
    Card[] getRemainingCards() {
        return Arrays.copyOfRange(cards, nextCardIndex, cards.length);
    }

    // draw next card
    public Card draw() {
        if (nextCardIndex >= cards.length) {
            return null;
        }
        // gives user card
        return cards[nextCardIndex++];
    }

    ///test
    public static void main(String[] args) {
        Deck deck = new Deck();
        deck.shuffle(); // take off for ordered

        for (int i = 0; i < 52; i++) {
            System.out.println(deck.draw());
        }
    }
}
