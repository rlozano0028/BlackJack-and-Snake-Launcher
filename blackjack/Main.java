import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// Visual-only scaffolding. No game logic wired in yet.
public class Main extends Application 
{
    @Override
    public void start(Stage stage) 
    {
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(tableFelt(), CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(24));

        root.setTop(buildDealerArea());
        root.setCenter(buildCenterArea());
        root.setBottom(buildPlayerAndControls());

        Scene scene = new Scene(root, 980, 720);
        stage.setTitle("Blackjack");
        stage.setScene(scene);
        stage.setMinWidth(860);
        stage.setMinHeight(640);
        stage.show();
    }

    // --- Dealer zone (top) -------------------------------------------------
    private VBox buildDealerArea() 
    {
        Label name = sectionLabel("DEALER");
        Label score = scoreBadge("10 + ?");

        HBox header = new HBox(16, name, score);
        header.setAlignment(Pos.CENTER);

        HBox cards = new HBox(-30,
            CardView.faceUp("K", "\u2665"),
            CardView.faceDown()
        );
        cards.setAlignment(Pos.CENTER);

        VBox box = new VBox(12, header, cards);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 0, 10, 0));
        return box;
    }

    // --- Middle banner -----------------------------------------------------
    private StackPane buildCenterArea() 
    {
        Label msg = new Label("PLACE YOUR BET");
        msg.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        msg.setTextFill(Color.web("#f1faee"));
        msg.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 6, 0.2, 1, 2);");

        Circle chip = new Circle(34, Color.web("#e63946"));
        chip.setStroke(Color.web("#f1faee"));
        chip.setStrokeWidth(4);
        chip.getStrokeDashArray().addAll(6.0, 6.0);

        Label chipAmount = new Label("$10");
        chipAmount.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        chipAmount.setTextFill(Color.WHITE);

        StackPane chipStack = new StackPane(chip, chipAmount);

        VBox center = new VBox(18, msg, chipStack);
        center.setAlignment(Pos.CENTER);

        StackPane wrapper = new StackPane(center);
        wrapper.setPadding(new Insets(20));
        return wrapper;
    }

    // --- Player zone + controls (bottom) -----------------------------------
    private VBox buildPlayerAndControls() 
    {
        HBox cards = new HBox(-30,
            CardView.faceUp("A", "\u2660"),
            CardView.faceUp("9", "\u2666")
        );
        cards.setAlignment(Pos.CENTER);

        Label name = sectionLabel("YOU");
        Label score = scoreBadge("20");

        HBox header = new HBox(16, name, score);
        header.setAlignment(Pos.CENTER);

        HBox infoRow = buildInfoRow();
        HBox buttonRow = buildButtonRow();

        VBox box = new VBox(12, cards, header, infoRow, buttonRow);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 0, 0, 0));
        return box;
    }

    private HBox buildInfoRow() 
    {
        Label bankroll = infoPill("Bankroll", "$90");
        Label bet      = infoPill("Current Bet", "$10");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(14, bankroll, spacer, bet);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(6, 40, 6, 40));
        return row;
    }

    private HBox buildButtonRow() 
    {
        Button deal   = actionButton("Deal",   "#2a9d8f");
        Button hit    = actionButton("Hit",    "#457b9d");
        Button stand  = actionButton("Stand",  "#e76f51");
        Button dbl    = actionButton("Double", "#f4a261");
        Button reset  = actionButton("Reset",  "#6c757d");

        // Non-functional for now; just shows buttons register clicks.
        deal.setOnAction(e  -> System.out.println("Deal clicked"));
        hit.setOnAction(e   -> System.out.println("Hit clicked"));
        stand.setOnAction(e -> System.out.println("Stand clicked"));
        dbl.setOnAction(e   -> System.out.println("Double clicked"));
        reset.setOnAction(e -> System.out.println("Reset clicked"));

        HBox row = new HBox(12, deal, hit, stand, dbl, reset);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(10, 0, 0, 0));
        return row;
    }

    // --- Small UI helpers --------------------------------------------------
    private Label sectionLabel(String text) 
    {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        l.setTextFill(Color.web("#f1faee"));
        l.setStyle("-fx-letter-spacing: 0.15em;");
        return l;
    }

    private Label scoreBadge(String text) 
    {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        l.setTextFill(Color.web("#1d3557"));
        l.setPadding(new Insets(4, 14, 4, 14));
        l.setStyle(
            "-fx-background-color: #f1faee;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: #1d3557;" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 2;"
        );
        return l;
    }

    private Label infoPill(String title, String value) 
    {
        Label l = new Label(title + "  " + value);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        l.setTextFill(Color.web("#f1faee"));
        l.setPadding(new Insets(8, 18, 8, 18));
        l.setStyle(
            "-fx-background-color: rgba(0,0,0,0.35);" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: rgba(255,255,255,0.25);" +
            "-fx-border-radius: 20;" +
            "-fx-border-width: 1;"
        );
        return l;
    }

    private Button actionButton(String text, String hexColor) 
    {
        Button b = new Button(text);
        b.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        b.setPrefWidth(110);
        b.setPrefHeight(42);
        b.setStyle(
            "-fx-background-color: " + hexColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 5, 0.2, 1, 2);"
        );
        b.setOnMouseEntered(e -> b.setStyle(b.getStyle() + "-fx-opacity: 0.88;"));
        b.setOnMouseExited(e  -> b.setStyle(b.getStyle().replace("-fx-opacity: 0.88;", "")));
        return b;
    }

    // Dark-green radial felt.
    private RadialGradient tableFelt() 
    {
        return new RadialGradient(
            0, 0, 0.5, 0.5, 0.9, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#2d6a4f")),
            new Stop(1.0, Color.web("#081c15"))
        );
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}
