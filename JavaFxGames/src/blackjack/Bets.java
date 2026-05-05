public class Bets 
{
    private double total;
    private double currentBet;

    public Bets(double startingTotal) 
    {
        if (startingTotal < 0) 
        {
            throw new IllegalArgumentException("Starting total must be non-negative");
        }
        this.total = startingTotal;
        this.currentBet = 0;
    }

    public double getTotal() 
    {
        return total;
    }

    public double getCurrentBet() 
    {
        return currentBet;
    }

    public boolean placeBet(double amount) 
    {
        if (amount <= 0 || amount > total) 
        {
            return false;
        }
        currentBet = amount;
        total -= amount;
        return true;
    }

    /**
     * Adds more chips from bankroll to the wager (mid-hand raise).
     * @return false if amount is invalid or exceeds available bankroll
     */
    public boolean raiseBet(double additional) 
    {
        if (additional <= 0 || additional > total) 
        {
            return false;
        }
        total -= additional;
        currentBet += additional;
        return true;
    }

    public void win() 
    {
        total += currentBet * 2;
        currentBet = 0;
    }

    public void blackjackWin() {
        total += currentBet * 2.5;
        currentBet = 0;
    }

    public void push() {
        total += currentBet;
        currentBet = 0;
    }

    public void lose() {
        currentBet = 0;
    }

    public void addFunds(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Added funds must be positive");
        }
        total += amount;
    }

    @Override
    public String toString() {
        return "Total: $" + total + ", Current bet: $" + currentBet;
    }
}
