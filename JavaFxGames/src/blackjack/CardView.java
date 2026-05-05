package blackjack;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

// A visual-only card. Later we'll accept a blackjack.Card and map rank/suit.
public class CardView extends StackPane 
{
    public static final double WIDTH  = 90;
    public static final double HEIGHT = 130;

    public CardView(String rank, String suit, boolean faceDown) 
    {
        Rectangle bg = new Rectangle(WIDTH, HEIGHT);
        bg.setArcWidth(16);
        bg.setArcHeight(16);
        bg.setStroke(Color.web("#222"));
        bg.setStrokeWidth(1.5);

        if (faceDown) 
        {
            bg.setFill(Color.web("#1d3557"));

            Rectangle inner = new Rectangle(WIDTH - 16, HEIGHT - 16);
            inner.setArcWidth(12);
            inner.setArcHeight(12);
            inner.setFill(Color.TRANSPARENT);
            inner.setStroke(Color.web("#a8dadc"));
            inner.setStrokeWidth(2);
            inner.getStrokeDashArray().addAll(4.0, 4.0);

            Text mono = new Text("\u2663");
            mono.setFont(Font.font("Arial", FontWeight.BOLD, 36));
            mono.setFill(Color.web("#a8dadc"));

            getChildren().addAll(bg, inner, mono);
        }
        else 
        {
            bg.setFill(Color.WHITE);
            Color color = (suit.equals("\u2665") || suit.equals("\u2666"))
                ? Color.web("#c8102e")
                : Color.web("#111");

            Text rankText = new Text(rank);
            rankText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            rankText.setFill(color);

            Text suitText = new Text(suit);
            suitText.setFont(Font.font("Arial", 40));
            suitText.setFill(color);

            VBox content = new VBox(4, rankText, suitText);
            content.setAlignment(Pos.CENTER);

            getChildren().addAll(bg, content);
        }

        setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 8, 0.2, 2, 3);");
    }

    public static CardView faceUp(String rank, String suit) 
    {
        return new CardView(rank, suit, false);
    }

    public static CardView faceDown() 
    {
        return new CardView("", "", true);
    }

    /**
     * Replaces this card's visual content in-place with a face-up view.
     * Used mid-animation to swap face-down → face-up without creating a new node.
     */
    public void flipTo(String rank, String suit) 
    {
        getChildren().clear();

        Rectangle bg = new Rectangle(WIDTH, HEIGHT);
        bg.setArcWidth(16);
        bg.setArcHeight(16);
        bg.setStroke(Color.web("#222"));
        bg.setStrokeWidth(1.5);
        bg.setFill(Color.WHITE);

        Color color = (suit.equals("\u2665") || suit.equals("\u2666"))
            ? Color.web("#c8102e")
            : Color.web("#111");

        Text rankText = new Text(rank);
        rankText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        rankText.setFill(color);

        Text suitText = new Text(suit);
        suitText.setFont(Font.font("Arial", 40));
        suitText.setFill(color);

        VBox content = new VBox(4, rankText, suitText);
        content.setAlignment(Pos.CENTER);

        getChildren().addAll(bg, content);
    }
}
