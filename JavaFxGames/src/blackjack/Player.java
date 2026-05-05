package blackjack;

import java.util.ArrayList;
import java.util.List;

public class Player 
{

    public enum Status { PLAYING, STAND, BUST, BLACKJACK, SURRENDER }

    private final String name;
    private final List<Card> hand;
    private final Bets account;
    private Status status;

    public Player(String name, double startingFunds)
    {
        this.name = name;
        this.hand = new ArrayList<>();
        this.account = new Bets(startingFunds);
        this.status = Status.PLAYING;
    }

    // --- Betting (delegates to Bets) ---
    public boolean placeBet(double amount) 
    {
        return account.placeBet(amount);
    }

    public boolean raiseBet(double additional) 
    {
        return account.raiseBet(additional);
    }

    public Bets getAccount() 
    {
        return account;
    }

    // --- Hand management ---
    public void receiveCard(Card card) 
    {
        hand.add(card);
        updateStatusAfterDeal();
    }

    public boolean hit(Deck deck) 
    {
        if (status != Status.PLAYING) return false;
        Card c = deck.draw();
        if (c == null) return false;
        receiveCard(c);
        return true;
    }

    public void stand() 
    {
        if (status == Status.PLAYING) status = Status.STAND;
    }

    public void clearHand() 
    {
        hand.clear();
        status = Status.PLAYING;
    }

    // --- Hand evaluation ---
    // Aces count as 11 by default, demoted to 1 as needed to avoid busting.
    public int getHandValue() 
    {
        int total = 0;
        int aces = 0;
        for (Card c : hand) 
        {
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

    public boolean isBust()       { return getHandValue() > 21; }
    public boolean hasBlackjack() { return hand.size() == 2 && getHandValue() == 21; }

    private void updateStatusAfterDeal() 
    {
        if (hasBlackjack())      status = Status.BLACKJACK;
        else if (isBust())       status = Status.BUST;
    }

    // --- Getters ---
    public String getName()     { return name; }
    public List<Card> getHand() { return hand; }
    public Status getStatus()   { return status; }

    @Override
    public String toString() 
    {
        return name + " [" + status + "] " + hand + " = " + getHandValue()
             + " | " + account;
    }
}
