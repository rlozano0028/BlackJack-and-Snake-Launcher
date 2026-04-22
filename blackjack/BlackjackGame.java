import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackjackGame {
    public enum Turn {
        HUMAN, AI1, AI2, DEALER, ROUND_OVER
    }

    private Deck deck;
    private Player human;
    private Player ai1;
    private Player ai2;
    private Dealer dealer;
    private Turn currentTurn;
    private String statusMessage;
    private boolean roundActive;

    public BlackjackGame() {
        deck = new Deck();
        human = new Player("You", 100);
        ai1 = new Player("AI 1", 100);
        ai2 = new Player("AI 2", 100);
        dealer = new Dealer();

        currentTurn = Turn.HUMAN;
        statusMessage = "Place your bets.";
        roundActive = false;
    }

    public boolean startRound(double humanBet) {
        if (humanBet <= 0) {
            statusMessage = "Bet cannot be less than or equals to 0.";
            return false;
        }

        // new shuffled deck
        deck = new Deck();
        deck.shuffle();
        
        // resets ahnds
        human.clearHand();
        ai1.clearHand();
        ai2.clearHand();
        dealer.clearHand();

        // put bets
        if (!human.placeBet(humanBet)) {
            statusMessage = "You don't have enough money.";
            return false;
        }

        placeAIBets();

        // starting cards
        human.hit(deck);
        ai1.hit(deck);
        ai2.hit(deck);
        dealer.hit(deck);

        human.hit(deck);
        ai1.hit(deck);
        ai2.hit(deck);
        dealer.hit(deck);

        currentTurn = Turn.HUMAN;
        roundActive = true;
        statusMessage = "Your turn.";

        // if human gets blackjack at start, next player
        if (human.getStatus() == Player.Status.BLACKJACK) {
            statusMessage = "You got Blackjack!";
            advanceTurn();
        }

        return true;
    }

    // ai bets
    private void placeAIBets() {
        // ai1 10 to max bet 
        double ai1Bet = Math.min(10, ai1.getAccount().getTotal());
        if (ai1Bet > 0) {
            ai1.placeBet(ai1Bet);
        }

        // ai2 bet 15 to 5 bets
        double ai2Bet;
        if (ai2.getAccount().getTotal() >= 15) {
            ai2Bet = 15;
        } else {
            ai2Bet = 5;
        }

        if (ai2Bet > 0) {
            ai2.placeBet(ai2Bet);
        }
    }

    // human player moves
    public void humanHit() {
        if (!roundActive || currentTurn != Turn.HUMAN) {
            return;
        }

        human.hit(deck);

        if (human.getSTatus() == Player.Status.BUST) {
            statusMessage = "You busted.";
            advanceTurn();
        } else if (human.getSTatus() == Player.Status.BLACKJACK) {
            statusMessage = "You got Blackjack!";
            advanceTurn();
        } else {
            statusMessage = "You drew a card.";
        }
    }

    public void humanStand() {
        if (!roundActive || currentTurn != Turn.HUMAN) {
            return;
        }

        human.stand();
        statusMessage = "You stand.";
        advanceTurn();
    }

    public void advanceTurn() {
        if (!roundActive) {
            return;
        }

        switch (currentTurn) {
            case HUMAN -> {
                currentTurn = Turn.AI1;
                runAI1Turn();
            }
            case AI1 -> {
                currentTurn = Turn.AI2;
                runAI2Turn();
            }
            case AI2 -> {
                currentTurn = Turn.DEALER;
                dealerTurn();
            }
            case DEALER -> {
                resolveRound();
            }
            case ROUND_OVER -> {
                // donothing
            }
        }
    }

    private void runAI1Turn() {
        statusMessage = "AI 1's turn.";

        while (ai1.getStatus() == Player.Status.PLAYING && ai1ShouldHit()) {
            ai1.hit(deck);
        }

        if (ai1.getStatus() == Player.Status.PLAYING) {
            ai1.stand();
            statusMessage = "AI 1 stands.";
        } else if (ai1.getStatus() == Player.Status.BUST) {
            statusMessage = "AI 1 busted.";
        } else if (ai1.getStatus() == Player.Status.BLACKJACK) {
            statusMessage = "AI 1 got Blackjack!";
        }

        advanceTurn();
    }

    private void runAI2Turn() {
        statusMessage = "AI 2's turn.";

        while (ai2.getStatus() == Player.Status.PLAYING && ai2ShouldHit()) {
            ai2.hit(deck);
        }

        if (ai2.getStatus() == Player.Status.PLAYING) {
            ai2.stand();
            statusMessage = "AI 2 stands.";
        } else if (ai2.getStatus() == Player.Status.BUST) {
            statusMessage = "AI 2 busted.";
        } else if (ai2.getStatus() == Player.Status.BLACKJACK) {
            statusMessage = "AI 2 got Blackjack!";
        }

        advanceTurn();
    }

    private void dealerTurn() {
        dealer.revealHoleCard();
        statusMessage = "Dealer's turn.";

        // dealer hit 17 or lower
        while (dealer.getStatus() == Player.Status.PLAYING && dealer.getHandValue() < 17) {
            dealer.hit(deck);
        }

        if (dealer.getStatus() == Player.Status.PLAYING) {
            dealer.stand();
            statusMessage = "Dealer stands.";
        } else if (dealer.getStatus() == Player.Status.BUST) {
            statusMessage = "Dealer busted.";
        } else if (dealer.getStatus() == Player.Status.BLACKJACK) {
            statusMessage = "Dealer got Blackjack.";
        }

        advanceTurn();
    }

    // ai behaviors
    // ai1 safe, ai2 risky
    private boolean ai1ShouldHit() {
        // safe ai
        return ai1.getHandValue() < 15;
    }

    private boolean ai2ShouldHit() {
        // risky ai
        int value = ai2.getHandValue();

        if (value < 16) return true;
        if (value > 16) return false;

        // on 16, sometimes hit or not
        return new Random().nextDouble() < 0.5;
    }

    private void resolveRound() {
        currentTurn = Turn.ROUND_OVER;
        roundActive = false;
        statusMessage = "Round over.";
    }
}