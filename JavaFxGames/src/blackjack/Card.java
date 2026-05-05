package blackjack;
public class Card {
    private String suit;
    private String rank;

    public Card (String suit, String rank){
        this.suit = suit;
        this.rank = rank;
    }

    // get suit h,d,c,s
    public String getSuit() {
        return suit;
    }

    // get rank a,2,3,3,4,5,6,,7...k
    public String getRank() {
        return rank;
    }
    
    // get number value for 21

    public int getNumberValue() {
        if (rank.equals ("A")) {
            return 11;
        } else if (rank.equals ("K") || rank.equals ("Q") || rank.equals ("J")) {
            return 10;
        } else {
            return Integer.parseInt(rank); // converts string to int
        }
    }

    // print the card
    @Override
    public String toString() {
        return rank + "-" + suit;
    }
}