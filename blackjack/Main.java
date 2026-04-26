import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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

import java.nio.file.Paths;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.stage.Stage;

// Visual-only scaffolding. No game logic wired in yet.
public class Main extends Application 
{
    private BlackjackGame game;

    // outisde so when changed, it changes everywhere
    private Label dealerScoreLabel;
    private Label playerScoreLabel;
    private Label ai1ScoreLabel;
    private Label ai2ScoreLabel;

    //human money
    private Label bankrollLabel;
    private Label betLabel;
    //ai 2
    private Label ai1BankrollLabel;
    private Label ai1BetLabel;
    //ai2
    private Label ai2BankrollLabel;
    private Label ai2BetLabel;

    private Label messageLabel;

    private HBox dealerCardsBox;
    private HBox playerCardsBox;
    private HBox ai1CardsBox;
    private HBox ai2CardsBox;

    private Button deal;
    private Button hit;
    private Button stand;
    private Button reset;
    private Spinner<Integer> raiseSpinner;
    private Button raiseBetBtn;

    private int raiseAmt;
    private boolean hasPlacedInitialBet;


    @Override
    public void start(Stage stage) 
    {
        game = new BlackjackGame();
        hasPlacedInitialBet = false;
        
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(tableFelt(), CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(24));

        root.setTop(buildDealerArea());
        root.setCenter(buildCenterArea());
        root.setBottom(buildBottomArea());

        Scene scene = new Scene(root, 1180, 760);
        stage.setTitle("Blackjack");
        stage.setScene(scene);
        stage.setMinWidth(980);
        stage.setMinHeight(700);

        try {
            music();
            stage.setOnShown(e -> mediaPlayer.play());
        } catch (Exception e) {
            System.out.println("Music not found/loaded.");
        }

        stage.show();
        updateUI();
    }
    
    // music player
    MediaPlayer mediaPlayer;
    public void music() {
        String s = "../sound/Balatro-Main-Theme.mp3";
        Media h = new Media(Paths.get(s).toUri().toString());
        mediaPlayer = new MediaPlayer(h);
    }

    // --- Dealer zone (top) -------------------------------------------------
    private VBox buildDealerArea() 
    {
        Label name = sectionLabel("DEALER");
        dealerScoreLabel = scoreBadge("0");

        HBox header = new HBox(16, name, dealerScoreLabel);
        header.setAlignment(Pos.CENTER);

        dealerCardsBox = new HBox(-30);
        // dealerCardsBox = new HBox(-30,
        //     CardView.faceUp("K", "\u2665"),
        //     CardView.faceDown()
        // );
        dealerCardsBox.setAlignment(Pos.CENTER);

        VBox box = new VBox(12, header, dealerCardsBox);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 0, 10, 0));
        return box;
    }

    // --- Middle banner -----------------------------------------------------
    private StackPane buildCenterArea() 
    {
        messageLabel = new Label("PLACE YOUR BET");
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        messageLabel.setTextFill(Color.web("#f1faee"));
        messageLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 6, 0.2, 1, 2);");

        Circle chip = new Circle(34, Color.web("#e63946"));
        chip.setStroke(Color.web("#f1faee"));
        chip.setStrokeWidth(4);
        chip.getStrokeDashArray().addAll(6.0, 6.0);

        Label chipAmount = new Label("$10");
        chipAmount.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        chipAmount.setTextFill(Color.WHITE);

        StackPane chipStack = new StackPane(chip, chipAmount);

        VBox center = new VBox(18, messageLabel, chipStack);
        center.setAlignment(Pos.CENTER);

        StackPane wrapper = new StackPane(center);
        wrapper.setPadding(new Insets(20));
        return wrapper;
    }

    // --- bottom table area ----------
    private VBox buildBottomArea() {
        VBox ai1Area = buildAI1Area();
        VBox playerArea = buildPlayerAndControls();
        VBox ai2Area = buildAI2Area();

        HBox row = new HBox(40, ai1Area, playerArea, ai2Area);
        row.setAlignment(Pos.BOTTOM_CENTER);

        VBox wrapper = new VBox(row);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(10, 0, 0, 0));
        return wrapper;

    }

    // --- AI players area -----------------------------
    private VBox buildAI1Area() {
        Label name = sectionLabel("AI 1");
        ai1ScoreLabel = scoreBadge("0");

        HBox header = new HBox(12, name, ai1ScoreLabel);
        header.setAlignment(Pos.CENTER);
        header.setTranslateY(-30);

        ai1CardsBox = new HBox(-24);
        ai1CardsBox.setAlignment(Pos.CENTER);
        ai1CardsBox.setRotate(12);
        ai1CardsBox.setTranslateY(-20);

        ai1BankrollLabel = infoPill("Bankroll", "$100");
        ai1BetLabel = infoPill("Bet", "$0");

        VBox infoBox = new VBox(8, ai1BankrollLabel, ai1BetLabel);
        infoBox.setAlignment(Pos.CENTER);

        VBox box = new VBox(4, header, ai1CardsBox, infoBox);
        box.setAlignment(Pos.CENTER);

        // scale everything down
        box.setScaleX(0.8);
        box.setScaleY(0.8);

        // move up or down
        box.setTranslateY(-60);

        return box;
    }

    private VBox buildAI2Area() {
        Label name = sectionLabel("AI 2");
        ai2ScoreLabel = scoreBadge("0");

        HBox header = new HBox(12, name, ai2ScoreLabel);
        header.setAlignment(Pos.CENTER);
        header.setTranslateY(-30);

        ai2CardsBox = new HBox(-24);
        ai2CardsBox.setAlignment(Pos.CENTER);
        ai2CardsBox.setRotate(-12);
        ai2CardsBox.setTranslateY(-20);


        ai2BankrollLabel = infoPill("Bankroll", "$100");
        ai2BetLabel = infoPill("Bet", "$0");

        VBox infoBox = new VBox(8, ai2BankrollLabel, ai2BetLabel);
        infoBox.setAlignment(Pos.CENTER);

        VBox box = new VBox(4, header, ai2CardsBox, infoBox);
        box.setAlignment(Pos.CENTER);

        // scale everything down
        box.setScaleX(0.8);
        box.setScaleY(0.8);

         // move up or down
        box.setTranslateY(-60);

        return box;
    }

    // --- Player zone + controls (bottom) -----------------------------------
    private VBox buildPlayerAndControls() 
    {
        playerCardsBox = new HBox(-30);
        // playerCardsBox = new HBox(-30,
        //     CardView.faceUp("A", "\u2660"),
        //     CardView.faceUp("9", "\u2666")
        // );
        playerCardsBox.setAlignment(Pos.CENTER);

        Label name = sectionLabel("YOU");
        playerScoreLabel = scoreBadge("0");

        HBox header = new HBox(16, name, playerScoreLabel);
        header.setAlignment(Pos.CENTER);

        HBox infoRow = buildInfoRow();
        HBox buttonRow = buildButtonRow();
        HBox raiseRow = buildRaiseRow();

        VBox box = new VBox(12, playerCardsBox, header, infoRow, buttonRow, raiseRow);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 0, 0, 0));
        return box;
    }

    private HBox buildInfoRow() 
    {
        bankrollLabel = infoPill("Bankroll", "$90");
        betLabel      = infoPill("Current Bet", "$10");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(14, bankrollLabel, spacer, betLabel);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(6, 40, 6, 40));
        return row;
    }

    private HBox buildButtonRow() 
    {
        deal   = actionButton("Deal",   "#2a9d8f");
        hit    = actionButton("Hit",    "#457b9d");
        stand  = actionButton("Stand",  "#e76f51");
        reset  = actionButton("Reset",  "#6c757d");

        // Non-functional for now; just shows buttons register clicks.
        deal.setOnAction(e  -> {
            
            System.out.println("Deal clicked");
            game.startRound(game.getHuman().getAccount().getCurrentBet());
            updateUI();

        });

        hit.setOnAction(e  -> {
            
            System.out.println("Hit clicked");
            game.humanHit();
            updateUI();

        });
        stand.setOnAction(e  -> {
            
            System.out.println("Stand clicked");
            game.humanStand();
            updateUI();

        });
        reset.setOnAction(e  -> {
            
            System.out.println("Reset clicked");
            game = new BlackjackGame();
            hasPlacedInitialBet = false;
            raiseBetBtn.setText("Initial Bet");
            updateUI();

        });

        HBox row = new HBox(12, deal, hit, stand, reset);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(10, 0, 0, 0));
        return row;
    }

    private HBox buildRaiseRow() {
        Label lbl = new Label("Raise by ($):");
        lbl.setTextFill(Color.web("#f1faee"));
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        raiseSpinner = new Spinner<>();
        raiseSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 10, 5));
        raiseSpinner.setEditable(true);
        raiseSpinner.setPrefWidth(100);

        raiseBetBtn = actionButton("Initial Bet", "#8e44ad");
        raiseBetBtn.setPrefWidth(120);
        raiseBetBtn.setOnAction(e -> {
            raiseAmt = raiseSpinner.getValue();
            game.humanRaiseBet(raiseAmt);
            if (!hasPlacedInitialBet) {
                hasPlacedInitialBet = true;
                raiseBetBtn.setText("Raise");
            }
            updateUI();
        });

        HBox row = new HBox(10, lbl, raiseSpinner, raiseBetBtn);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(4, 0, 0, 0));
        return row;
    }

    // --- update ui ------------------------------
    private void updateUI() {
        messageLabel.setText(game.getStatusMessage());

        if (game.getDealer().isHoleCardRevealed()) {
            dealerScoreLabel.setText(String.valueOf(game.getDealer().getHandValue()));
        } else {
            dealerScoreLabel.setText(game.getDealer().getVisibleHandValue() + " + ?");
        }

        playerScoreLabel.setText(String.valueOf(game.getHuman().getHandValue()));
        ai1ScoreLabel.setText(String.valueOf(game.getAi1().getHandValue()));
        ai2ScoreLabel.setText(String.valueOf(game.getAi2().getHandValue()));

        // formatmoney makes it where it adds , 1000=>1,000
        bankrollLabel.setText("Bankroll $" + formatMoney(game.getHuman().getAccount().getTotal()));
        betLabel.setText("Current Bet $" + formatMoney(game.getHuman().getAccount().getCurrentBet()));

        ai1BankrollLabel.setText("Bankroll $" + formatMoney(game.getAi1().getAccount().getTotal()));
        ai1BetLabel.setText("Current Bet $" + formatMoney(game.getAi1().getAccount().getCurrentBet()));
        
        ai2BankrollLabel.setText("Bankroll $" + formatMoney(game.getAi2().getAccount().getTotal()));
        ai2BetLabel.setText("Current Bet $" + formatMoney(game.getAi2().getAccount().getCurrentBet()));

        updateDealerCards();
        updateHumanCards();
        updateAI1Cards();
        updateAI2Cards();

        // checks if human turn
        boolean humanTurn = game.isRoundActive() && game.getCurrentTurn() == BlackjackGame.Turn.HUMAN;
        int bankFloor = (int) Math.floor(game.getHuman().getAccount().getTotal());
        boolean canPlaceInitialBet = !hasPlacedInitialBet && !game.isRoundActive() && bankFloor >= 1;
        boolean canRaiseDuringTurn = humanTurn && game.getHuman().getStatus() == Player.Status.PLAYING && bankFloor >= 1;
        boolean canRaise = canPlaceInitialBet || canRaiseDuringTurn;

        if (canRaise) {
            SpinnerValueFactory.IntegerSpinnerValueFactory fac =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) raiseSpinner.getValueFactory();
            fac.setMax(bankFloor);
            fac.setMin(1);
            if (fac.getValue() > bankFloor) {
                fac.setValue(bankFloor);
            }
            raiseSpinner.setDisable(false);
            raiseBetBtn.setDisable(false);
        } else {
            raiseSpinner.setDisable(true);
            raiseBetBtn.setDisable(true);
        }

        // turn off button if not human turn
        hit.setDisable(!humanTurn);
        stand.setDisable(!humanTurn);
        deal.setDisable(game.isRoundActive());

    }

    //--- update cards of players ---------------------
    private void updateDealerCards(){
        // clears evreything in dealercardbox area
        dealerCardsBox.getChildren().clear();

        for (int i = 0; i < game.getDealer().getHand().size(); i++) {
            Card card = game.getDealer().getHand().get(i);

            if (i == 1 && !game.getDealer().isHoleCardRevealed()) {
                // show unshown card
                dealerCardsBox.getChildren().add(CardView.faceDown());
            } else {
                dealerCardsBox.getChildren().add(CardView.faceUp(card.getRank(), suitToSymbol(card.getSuit())));
            }
        }
    }

    private void updateHumanCards(){
        // clears evreything in card area
        playerCardsBox.getChildren().clear();

        for (int i = 0; i < game.getHuman().getHand().size(); i++) {
            Card card = game.getHuman().getHand().get(i);

            playerCardsBox.getChildren().add(CardView.faceUp(card.getRank(), suitToSymbol(card.getSuit())));
        }
    }

    private void updateAI1Cards(){

        ai1CardsBox.getChildren().clear();

        for (int i = 0; i < game.getAi1().getHand().size(); i++) {
            Card card = game.getAi1().getHand().get(i);

            ai1CardsBox.getChildren().add(CardView.faceUp(card.getRank(), suitToSymbol(card.getSuit())));
        }
    }
    private void updateAI2Cards(){
        
        ai2CardsBox.getChildren().clear();

        for (int i = 0; i < game.getAi2().getHand().size(); i++) {
            Card card = game.getAi2().getHand().get(i);

            ai2CardsBox.getChildren().add(CardView.faceUp(card.getRank(), suitToSymbol(card.getSuit())));
        }
    }

    // --- convert suit to cool shapes :D (hearts,diamoand,club,spade) -----
    private String suitToSymbol(String suit) {
        if (suit.equals("Hearts")) {
            return "\u2665";
        } else if (suit.equals("Diamond")){
            return "\u2666";
        } else if (suit.equals("Club")) {
            return "\u2663";
        } else if (suit.equals("Spade")) {
            return "\u2660";
        } else {
            return "?";
        }
    }

    // --- makes money readable like 1000 => 1,000
    private String formatMoney(double amount) {
        if (amount == (int) amount) {
            // whole number
            return Integer.toString((int) amount);
        }
        // decimals
        return String.format("%.2f", amount);
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
