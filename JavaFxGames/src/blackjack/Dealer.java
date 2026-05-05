package blackjack;

import java.util.List;

public class Dealer extends Player 
{
    // Stand on all 17s (S17). Change to 18 if you prefer dealer hits soft 17.
    private static final int STAND_THRESHOLD = 17;

    private boolean holeCardRevealed;

    public Dealer() 
    {
        super("Dealer", 0);
        this.holeCardRevealed = true;
    }

    // Deal the dealer's opening two cards; the second is the hidden hole card.
    public void dealInitial(Deck deck) 
    {
        clearHand();
        hit(deck);
        hit(deck);
        holeCardRevealed = false;
    }

    public void revealHoleCard() 
    {
        holeCardRevealed = true;
    }

    public boolean isHoleCardRevealed() 
    {
        return holeCardRevealed;
    }

    public boolean isSoft17() {
        List<Card> hand = getHand();
        int total = 0;
        int aces = 0;

        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            total += c.getNumberValue();

            if (c.getRank().equals("A")) {
                aces++;
            }
        }

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total == 17 && aces > 0;
    }

    // Reveal hole card, then hit until reaching the stand threshold.
    public void playTurn(Deck deck) 
    {
        revealHoleCard();
        while (getStatus() == Status.PLAYING && getHandValue() < STAND_THRESHOLD || isSoft17()) 
        {
            hit(deck);
        }
        if (getStatus() == Status.PLAYING) stand();
    }

    // Sum of only the face-up cards (for UI "dealer showing X").
    public int getVisibleHandValue() 
    {
        if (holeCardRevealed) return getHandValue();

        List<Card> hand = getHand();
        int total = 0;
        int aces = 0;
        for (int i = 0; i < hand.size(); i++) 
        {
            if (i == 1) continue; // hole card is the second dealt
            Card c = hand.get(i);
            total += c.getNumberValue();
            if (c.getRank().equals("A")) aces++;
        }
        while (total > 21 && aces > 0) 
        {
            total -= 10;
            aces--;
        }
        return total;
    }

    @Override
    public void clearHand() 
    {
        super.clearHand();
        holeCardRevealed = true;
    }

    // Dealer has no bankroll; betting is a no-op.
    @Override
    public boolean placeBet(double amount) 
    {
        return false;
    }

    @Override
    public String toString() 
    {
        List<Card> hand = getHand();
        StringBuilder sb = new StringBuilder(getName())
            .append(" [").append(getStatus()).append("] [");
        for (int i = 0; i < hand.size(); i++) 
        {
            if (i > 0) sb.append(", ");
            if (i == 1 && !holeCardRevealed) sb.append("??");
            else sb.append(hand.get(i));
        }
        sb.append("] = ");
        sb.append(holeCardRevealed
            ? Integer.toString(getHandValue())
            : getVisibleHandValue() + "+?");
        return sb.toString();
    }
}
