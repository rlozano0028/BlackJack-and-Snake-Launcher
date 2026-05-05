package blackjack;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
import javafx.util.Duration;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.stage.Stage;

public class Main extends Application 
{
    private BlackjackGame game;

    private Label dealerScoreLabel;
    private Label playerScoreLabel;
    private Label ai1ScoreLabel;
    private Label ai2ScoreLabel;

    private Label bankrollLabel;
    private Label betLabel;
    private Label ai1BankrollLabel;
    private Label ai1BetLabel;
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

    // Animation overlay — a transparent Pane that sits on top of everything
    // so animated cards can fly freely across the full window.
    private Pane overlayPane;

    // The visual deck pile in the center; used as the animation origin.
    private StackPane deckNode;

    // -----------------------------------------------------------------------
    // CardInfo — one entry per card to animate
    // -----------------------------------------------------------------------
    private static class CardInfo {
        final Card   card;
        final HBox   targetBox;
        final boolean faceDown;   // true = stay face-down after flip (dealer hole card)

        CardInfo(Card card, HBox targetBox, boolean faceDown) {
            this.card      = card;
            this.targetBox = targetBox;
            this.faceDown  = faceDown;
        }
    }

    // -----------------------------------------------------------------------
    // Application entry point
    // -----------------------------------------------------------------------
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

        // Overlay pane: transparent, mouse-transparent, covers full window
        overlayPane = new Pane();
        overlayPane.setMouseTransparent(true);
        overlayPane.setPickOnBounds(false);

        // Wrap root + overlay so animations fly over everything
        StackPane sceneRoot = new StackPane(root, overlayPane);

        Scene scene = new Scene(sceneRoot, 1180, 760);
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
    
    // -----------------------------------------------------------------------
    // Music
    // -----------------------------------------------------------------------
    MediaPlayer mediaPlayer;
    public void music() {
        String[] candidates = {
            "sound/Balatro-Main-Theme.mp3",          // running from project root
            "../sound/Balatro-Main-Theme.mp3",        // running from blackjack/ folder
            "JavaFxGames/sound/Balatro-Main-Theme.mp3" // running from project root (JavaFxGames layout)
        };
        for (String s : candidates) {
            java.io.File f = new java.io.File(s);
            if (f.exists()) {
                Media h = new Media(f.toURI().toString());
                mediaPlayer = new MediaPlayer(h);
                mediaPlayer.play();
                return;
            }
        }
        System.out.println("Music not found/loaded.");
    }

    // -----------------------------------------------------------------------
    // Dealer zone (top)
    // -----------------------------------------------------------------------
    private VBox buildDealerArea() 
    {
        Label name = sectionLabel("DEALER");
        dealerScoreLabel = scoreBadge("0");

        HBox header = new HBox(16, name, dealerScoreLabel);
        header.setAlignment(Pos.CENTER);

        dealerCardsBox = new HBox(-30);
        dealerCardsBox.setAlignment(Pos.CENTER);

        VBox box = new VBox(12, header, dealerCardsBox);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 0, 10, 0));
        return box;
    }

    // -----------------------------------------------------------------------
    // Middle banner — also hosts the deck pile
    // -----------------------------------------------------------------------
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

        // Deck pile visual — several face-down cards stacked with slight offset
        deckNode = buildDeckPile();

        VBox center = new VBox(18, messageLabel, chipStack, deckNode);
        center.setAlignment(Pos.CENTER);

        StackPane wrapper = new StackPane(center);
        wrapper.setPadding(new Insets(20));
        return wrapper;
    }

    // Build a small deck-of-cards visual (5 stacked face-down cards).
    private StackPane buildDeckPile() {
        StackPane pile = new StackPane();
        int layers = 5;
        for (int i = layers - 1; i >= 0; i--) {
            CardView c = CardView.faceDown();
            c.setScaleX(0.72);
            c.setScaleY(0.72);
            // slight shadow-stack offset
            c.setTranslateX(-i * 1.2);
            c.setTranslateY(-i * 1.2);
            pile.getChildren().add(c);
        }
        pile.setPickOnBounds(false);
        return pile;
    }

    // -----------------------------------------------------------------------
    // Bottom table area
    // -----------------------------------------------------------------------
    private VBox buildBottomArea() {
        VBox ai1Area    = buildAI1Area();
        VBox playerArea = buildPlayerAndControls();
        VBox ai2Area    = buildAI2Area();

        HBox row = new HBox(40, ai1Area, playerArea, ai2Area);
        row.setAlignment(Pos.BOTTOM_CENTER);

        VBox wrapper = new VBox(row);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(10, 0, 0, 0));
        return wrapper;
    }

    // -----------------------------------------------------------------------
    // AI player areas
    // -----------------------------------------------------------------------
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
        ai1BetLabel      = infoPill("Bet", "$0");

        VBox infoBox = new VBox(8, ai1BankrollLabel, ai1BetLabel);
        infoBox.setAlignment(Pos.CENTER);

        VBox box = new VBox(4, header, ai1CardsBox, infoBox);
        box.setAlignment(Pos.CENTER);
        box.setScaleX(0.8);
        box.setScaleY(0.8);
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
        ai2BetLabel      = infoPill("Bet", "$0");

        VBox infoBox = new VBox(8, ai2BankrollLabel, ai2BetLabel);
        infoBox.setAlignment(Pos.CENTER);

        VBox box = new VBox(4, header, ai2CardsBox, infoBox);
        box.setAlignment(Pos.CENTER);
        box.setScaleX(0.8);
        box.setScaleY(0.8);
        box.setTranslateY(-60);
        return box;
    }

    // -----------------------------------------------------------------------
    // Player zone + controls
    // -----------------------------------------------------------------------
    private VBox buildPlayerAndControls() 
    {
        playerCardsBox = new HBox(-30);
        playerCardsBox.setAlignment(Pos.CENTER);

        Label name = sectionLabel("YOU");
        playerScoreLabel = scoreBadge("0");

        HBox header   = new HBox(16, name, playerScoreLabel);
        header.setAlignment(Pos.CENTER);

        HBox infoRow   = buildInfoRow();
        HBox buttonRow = buildButtonRow();
        HBox raiseRow  = buildRaiseRow();

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
        deal  = actionButton("Deal",  "#2a9d8f");
        hit   = actionButton("Hit",   "#457b9d");
        stand = actionButton("Stand", "#e76f51");
        reset = actionButton("Reset", "#6c757d");

        deal.setOnAction(e -> {
            System.out.println("Deal clicked");
            if (game.startRound(game.getHuman().getAccount().getCurrentBet())) {
                // Clear card boxes — game logic already populated hands
                dealerCardsBox.getChildren().clear();
                playerCardsBox.getChildren().clear();
                ai1CardsBox.getChildren().clear();
                ai2CardsBox.getChildren().clear();

                // Build animation list mirroring real blackjack deal order
                List<CardInfo> cards = new ArrayList<>();
                cards.add(new CardInfo(game.getHuman().getHand().get(0),  playerCardsBox,  false));
                cards.add(new CardInfo(game.getAi1().getHand().get(0),    ai1CardsBox,     false));
                cards.add(new CardInfo(game.getAi2().getHand().get(0),    ai2CardsBox,     false));
                cards.add(new CardInfo(game.getDealer().getHand().get(0), dealerCardsBox,  false));
                cards.add(new CardInfo(game.getHuman().getHand().get(1),  playerCardsBox,  false));
                cards.add(new CardInfo(game.getAi1().getHand().get(1),    ai1CardsBox,     false));
                cards.add(new CardInfo(game.getAi2().getHand().get(1),    ai2CardsBox,     false));
                // Dealer card[1] is the hole card — stays face-down
                cards.add(new CardInfo(game.getDealer().getHand().get(1), dealerCardsBox,  true));

                setButtonsDisabled(true);
                animateDeal(cards, () -> updateUI());
            } else {
                updateUI(); // shows error message (e.g. no bet placed)
            }
        });

        hit.setOnAction(e -> {
            System.out.println("Hit clicked");
            int prevSize = game.getHuman().getHand().size();
            game.humanHit();

            if (game.getHuman().getHand().size() > prevSize) {
                Card newCard = game.getHuman().getHand().get(game.getHuman().getHand().size() - 1);
                List<CardInfo> cards = new ArrayList<>();
                cards.add(new CardInfo(newCard, playerCardsBox, false));
                setButtonsDisabled(true);
                animateDeal(cards, () -> updateUI());
            } else {
                updateUI();
            }
        });

        stand.setOnAction(e -> {
            System.out.println("Stand clicked");
            game.humanStand();
            updateUI();
        });

        reset.setOnAction(e -> {
            System.out.println("Reset clicked");
            game = new BlackjackGame();
            hasPlacedInitialBet = false;
            raiseBetBtn.setText("Set Bet");
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

        raiseBetBtn = actionButton("Set Bet", "#8e44ad");
        raiseBetBtn.setPrefWidth(120);
        raiseBetBtn.setOnAction(e -> {
            try {
                String text = raiseSpinner.getEditor().getText();
                int value = Integer.parseInt(text);

                game.humanRaiseBet(value);
                updateUI();

            } catch (NumberFormatException e1) {
                messageLabel.setText("Invalid number");
            }
        });

        HBox row = new HBox(10, lbl, raiseSpinner, raiseBetBtn);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(4, 0, 0, 0));
        return row;
    }

    // -----------------------------------------------------------------------
    // Animation — deal one card at a time, chained sequentially
    // -----------------------------------------------------------------------

    /**
     * Animates cards one-by-one from the deck pile to each target HBox.
     * Each card slides face-down, then flips to reveal its face on arrival.
     * Calls onDone when the last card finishes.
     */
    private void animateDeal(List<CardInfo> cards, Runnable onDone) {
        if (cards.isEmpty()) {
            if (onDone != null) onDone.run();
            return;
        }
        animateOneCard(cards.get(0), () ->
            animateDeal(cards.subList(1, cards.size()), onDone)
        );
    }

    private void animateOneCard(CardInfo info, Runnable onComplete) {
        // --- Origin: center of the deck pile in overlay-local coords ---
        Bounds deckScene     = deckNode.localToScene(deckNode.getBoundsInLocal());
        Bounds deckInOverlay = overlayPane.sceneToLocal(deckScene);

        double startX = deckInOverlay.getMinX() + (deckInOverlay.getWidth()  - CardView.WIDTH)  / 2;
        double startY = deckInOverlay.getMinY() + (deckInOverlay.getHeight() - CardView.HEIGHT) / 2;

        // Face-down card placed on overlay at deck position
        CardView animCard = CardView.faceDown();
        animCard.setLayoutX(startX);
        animCard.setLayoutY(startY);
        overlayPane.getChildren().add(animCard);

        // Destination: center of target HBox in overlay-local coords
        Bounds targetScene     = info.targetBox.localToScene(info.targetBox.getBoundsInLocal());
        Bounds targetInOverlay = overlayPane.sceneToLocal(targetScene);

        double endX = targetInOverlay.getMinX() + (targetInOverlay.getWidth()  - CardView.WIDTH)  / 2;
        double endY = targetInOverlay.getMinY() + (targetInOverlay.getHeight() - CardView.HEIGHT) / 2;

        // Phase 1: slide face-down card from deck to target hand
        TranslateTransition slide = new TranslateTransition(Duration.millis(450), animCard);
        slide.setToX(endX - startX);
        slide.setToY(endY - startY);

        slide.setOnFinished(slideEvt -> {
            // Phase 2a: rotate 0° → 90° (card "edge-on", disappears)
            RotateTransition rotateOut = new RotateTransition(Duration.millis(110), animCard);
            rotateOut.setAxis(javafx.geometry.Point3D.ZERO.add(0, 1, 0)); // Y-axis flip
            rotateOut.setFromAngle(0);
            rotateOut.setToAngle(90);

            rotateOut.setOnFinished(rotOutEvt -> {
                // Swap content: remove face-down, add revealed card
                overlayPane.getChildren().remove(animCard);

                CardView revealCard = info.faceDown
                    ? CardView.faceDown()
                    : CardView.faceUp(info.card.getRank(), suitToSymbol(info.card.getSuit()));

                revealCard.setLayoutX(startX);
                revealCard.setLayoutY(startY);
                revealCard.setTranslateX(endX - startX);
                revealCard.setTranslateY(endY - startY);
                // Start the flip-in from -90° (edge-on) so it opens toward the viewer
                revealCard.setRotationAxis(javafx.geometry.Point3D.ZERO.add(0, 1, 0));
                revealCard.setRotate(-90);
                overlayPane.getChildren().add(revealCard);

                // Phase 2b: rotate -90° → 0° (card opens to face)
                RotateTransition rotateIn = new RotateTransition(Duration.millis(110), revealCard);
                rotateIn.setAxis(javafx.geometry.Point3D.ZERO.add(0, 1, 0));
                rotateIn.setFromAngle(-90);
                rotateIn.setToAngle(0);

                rotateIn.setOnFinished(rotInEvt -> {
                    overlayPane.getChildren().remove(revealCard);
                    // Place permanent card in the target HBox
                    CardView staticCard = info.faceDown
                        ? CardView.faceDown()
                        : CardView.faceUp(info.card.getRank(), suitToSymbol(info.card.getSuit()));
                    info.targetBox.getChildren().add(staticCard);

                    if (onComplete != null) onComplete.run();
                });
                rotateIn.play();
            });
            rotateOut.play();
        });

        slide.play();
    }

    // Disable/enable all interactive controls during animation.
    private void setButtonsDisabled(boolean disabled) {
        deal.setDisable(disabled);
        hit.setDisable(disabled);
        stand.setDisable(disabled);
        // reset.setDisable(disabled); allows users to reset whenever
        raiseBetBtn.setDisable(disabled);
        raiseSpinner.setDisable(disabled);
    }

    // -----------------------------------------------------------------------
    // UI update
    // -----------------------------------------------------------------------
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

        boolean humanTurn = game.isRoundActive() && game.getCurrentTurn() == BlackjackGame.Turn.HUMAN;
        int bankFloor = (int) Math.floor(game.getHuman().getAccount().getTotal());
        boolean canRaise = !game.isRoundActive() && bankFloor >= 1;

        if (canRaise) {
            SpinnerValueFactory.IntegerSpinnerValueFactory fac =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) raiseSpinner.getValueFactory();
            fac.setMax(bankFloor);
            fac.setMin(1);
            if (fac.getValue() > bankFloor) fac.setValue(bankFloor);
            raiseSpinner.setDisable(false);
            raiseBetBtn.setDisable(false);
        } else {
            raiseSpinner.setDisable(true);
            raiseBetBtn.setDisable(true);
        }

        hit.setDisable(!humanTurn);
        stand.setDisable(!humanTurn);
        deal.setDisable(game.isRoundActive());
    }

    // -----------------------------------------------------------------------
    // Card box renderers
    // -----------------------------------------------------------------------
    private void updateDealerCards() {
        dealerCardsBox.getChildren().clear();
        for (int i = 0; i < game.getDealer().getHand().size(); i++) {
            Card card = game.getDealer().getHand().get(i);
            if (i == 1 && !game.getDealer().isHoleCardRevealed()) {
                dealerCardsBox.getChildren().add(CardView.faceDown());
            } else {
                dealerCardsBox.getChildren().add(CardView.faceUp(card.getRank(), suitToSymbol(card.getSuit())));
            }
        }
    }

    private void updateHumanCards() {
        playerCardsBox.getChildren().clear();
        for (Card card : game.getHuman().getHand()) {
            playerCardsBox.getChildren().add(CardView.faceUp(card.getRank(), suitToSymbol(card.getSuit())));
        }
    }

    private void updateAI1Cards() {
        ai1CardsBox.getChildren().clear();
        for (Card card : game.getAi1().getHand()) {
            ai1CardsBox.getChildren().add(CardView.faceUp(card.getRank(), suitToSymbol(card.getSuit())));
        }
    }

    private void updateAI2Cards() {
        ai2CardsBox.getChildren().clear();
        for (Card card : game.getAi2().getHand()) {
            ai2CardsBox.getChildren().add(CardView.faceUp(card.getRank(), suitToSymbol(card.getSuit())));
        }
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------
    private String suitToSymbol(String suit) {
        switch (suit) {
            case "Hearts":  return "\u2665";
            case "Diamond": return "\u2666";
            case "Club":    return "\u2663";
            case "Spade":   return "\u2660";
            default:        return "?";
        }
    }

    private String formatMoney(double amount) {
        if (amount == (int) amount) return Integer.toString((int) amount);
        return String.format("%.2f", amount);
    }

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
