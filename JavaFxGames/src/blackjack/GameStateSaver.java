package blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Converts a BlackjackGame to a compact JSON save string and back.
 * No external libraries required — serialization uses StringBuilder,
 * deserialization uses targeted string extraction on a known fixed schema.
 */
public class GameStateSaver {

    // -----------------------------------------------------------------------
    // Public result bundle returned by load()
    // -----------------------------------------------------------------------
    public static class SaveResult {
        public final BlackjackGame game;
        public final boolean hasPlacedInitialBet;
        public final int raiseAmt;

        SaveResult(BlackjackGame game, boolean hasPlacedInitialBet, int raiseAmt) {
            this.game = game;
            this.hasPlacedInitialBet = hasPlacedInitialBet;
            this.raiseAmt = raiseAmt;
        }
    }

    // -----------------------------------------------------------------------
    // SAVE
    // -----------------------------------------------------------------------

    /**
     * Serializes the full game state to a compact single-line JSON string.
     *
     * @param game               the current BlackjackGame
     * @param hasPlacedInitialBet UI flag from Main
     * @param raiseAmt            spinner value from Main
     * @return compact JSON save string
     */
    public static String save(BlackjackGame game, boolean hasPlacedInitialBet, int raiseAmt) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"version\":1,");
        sb.append("\"roundActive\":").append(game.isRoundActive()).append(",");
        sb.append("\"currentTurn\":\"").append(game.getCurrentTurn().name()).append("\",");
        sb.append("\"statusMessage\":\"").append(escapeJson(game.getStatusMessage())).append("\",");
        sb.append("\"human\":").append(playerToJson(game.getHuman())).append(",");
        sb.append("\"ai1\":").append(playerToJson(game.getAi1())).append(",");
        sb.append("\"ai2\":").append(playerToJson(game.getAi2())).append(",");
        sb.append("\"dealer\":").append(dealerToJson(game.getDealer())).append(",");
        sb.append("\"deck\":").append(deckToJson(game.getDeck())).append(",");
        sb.append("\"ui\":{");
        sb.append("\"hasPlacedInitialBet\":").append(hasPlacedInitialBet).append(",");
        sb.append("\"raiseAmt\":").append(raiseAmt);
        sb.append("}");
        sb.append("}");
        return sb.toString();
    }

    private static String playerToJson(Player p) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"total\":").append(p.getAccount().getTotal()).append(",");
        sb.append("\"currentBet\":").append(p.getAccount().getCurrentBet()).append(",");
        sb.append("\"status\":\"").append(p.getStatus().name()).append("\",");
        sb.append("\"hand\":").append(handToJson(p.getHand()));
        sb.append("}");
        return sb.toString();
    }

    private static String dealerToJson(Dealer d) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"holeCardRevealed\":").append(d.isHoleCardRevealed()).append(",");
        sb.append("\"status\":\"").append(d.getStatus().name()).append("\",");
        sb.append("\"hand\":").append(handToJson(d.getHand()));
        sb.append("}");
        return sb.toString();
    }

    private static String deckToJson(Deck deck) {
        Card[] remaining = deck.getRemainingCards();
        StringBuilder sb = new StringBuilder("{\"remaining\":[");
        for (int i = 0; i < remaining.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(cardToJson(remaining[i]));
        }
        sb.append("]}");
        return sb.toString();
    }

    private static String handToJson(List<Card> hand) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < hand.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(cardToJson(hand.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    private static String cardToJson(Card c) {
        return "{\"suit\":\"" + escapeJson(c.getSuit())
             + "\",\"rank\":\"" + escapeJson(c.getRank()) + "\"}";
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // -----------------------------------------------------------------------
    // LOAD
    // -----------------------------------------------------------------------

    private static final Set<String> VALID_SUITS = new HashSet<>(
        Arrays.asList("Club", "Diamond", "Hearts", "Spade")
    );
    private static final Set<String> VALID_RANKS = new HashSet<>(
        Arrays.asList("A","2","3","4","5","6","7","8","9","10","J","Q","K")
    );

    /**
     * Parses a save string produced by {@link #save} and reconstructs a
     * full game state.
     *
     * @param json the save string to parse
     * @return a {@link SaveResult} on success, or {@code null} if the string
     *         is invalid, missing required fields, or has an unsupported version
     */
    public static SaveResult load(String json) {
        if (json == null || json.trim().isEmpty()) return null;
        json = json.trim();

        try {
            Integer version = extractInt(json, "version");
            if (version == null || version != 1) return null;

            Boolean roundActive   = extractBoolean(json, "roundActive");
            String  turnStr       = extractString(json, "currentTurn");
            String  statusMessage = extractString(json, "statusMessage");

            if (roundActive == null || turnStr == null || statusMessage == null) return null;

            BlackjackGame.Turn currentTurn;
            try {
                currentTurn = BlackjackGame.Turn.valueOf(turnStr);
            } catch (IllegalArgumentException e) {
                return null;
            }

            String humanJson  = extractObject(json, "human");
            String ai1Json    = extractObject(json, "ai1");
            String ai2Json    = extractObject(json, "ai2");
            String dealerJson = extractObject(json, "dealer");
            String deckJson   = extractObject(json, "deck");
            String uiJson     = extractObject(json, "ui");

            if (humanJson == null || ai1Json == null || ai2Json == null
                    || dealerJson == null || deckJson == null || uiJson == null) return null;

            Player human  = restorePlayer("You",  humanJson);
            Player ai1    = restorePlayer("AI 1", ai1Json);
            Player ai2    = restorePlayer("AI 2", ai2Json);
            Dealer dealer = restoreDealer(dealerJson);
            Deck   deck   = restoreDeck(deckJson);

            if (human == null || ai1 == null || ai2 == null || dealer == null || deck == null) return null;

            BlackjackGame game = new BlackjackGame(
                human, ai1, ai2, dealer, deck, currentTurn, statusMessage, roundActive
            );

            Boolean hasPlacedInitialBet = extractBoolean(uiJson, "hasPlacedInitialBet");
            Integer raiseAmt            = extractInt(uiJson, "raiseAmt");
            if (hasPlacedInitialBet == null) hasPlacedInitialBet = false;
            if (raiseAmt == null || raiseAmt < 1) raiseAmt = 10;

            return new SaveResult(game, hasPlacedInitialBet, raiseAmt);

        } catch (Exception e) {
            return null;
        }
    }

    private static Player restorePlayer(String name, String json) {
        Double  total      = extractDouble(json, "total");
        Double  currentBet = extractDouble(json, "currentBet");
        String  statusStr  = extractString(json, "status");
        String  handArr    = extractArray(json, "hand");

        if (total == null || currentBet == null || statusStr == null || handArr == null) return null;
        if (total < 0 || currentBet < 0) return null;

        Player.Status status;
        try {
            status = Player.Status.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            return null;
        }

        Player p = new Player(name, 0);
        p.getAccount().setTotal(total);
        p.getAccount().setCurrentBet(currentBet);

        List<Card> cards = parseCardArray(handArr);
        if (cards == null) return null;
        for (Card c : cards) {
            p.getHand().add(c);
        }
        p.setStatus(status);
        return p;
    }

    private static Dealer restoreDealer(String json) {
        Boolean holeCardRevealed = extractBoolean(json, "holeCardRevealed");
        String  statusStr        = extractString(json, "status");
        String  handArr          = extractArray(json, "hand");

        if (holeCardRevealed == null || statusStr == null || handArr == null) return null;

        Player.Status status;
        try {
            status = Player.Status.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            return null;
        }

        Dealer d = new Dealer();
        List<Card> cards = parseCardArray(handArr);
        if (cards == null) return null;
        for (Card c : cards) {
            d.getHand().add(c);
        }
        d.setStatus(status);
        d.setHoleCardRevealed(holeCardRevealed);
        return d;
    }

    private static Deck restoreDeck(String json) {
        String remainingArr = extractArray(json, "remaining");
        if (remainingArr == null) return null;
        List<Card> cardList = parseCardArray(remainingArr);
        if (cardList == null) return null;
        return new Deck(cardList.toArray(Card[]::new));
    }

    // -----------------------------------------------------------------------
    // JSON extraction helpers (no external dependencies)
    // -----------------------------------------------------------------------

    /** Extracts the value of {@code "key":"value"} handling backslash escapes. */
    private static String extractString(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start < 0) return null;
        start += search.length();
        StringBuilder val = new StringBuilder();
        int i = start;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                val.append(json.charAt(i + 1));
                i += 2;
            } else if (c == '"') {
                break;
            } else {
                val.append(c);
                i++;
            }
        }
        return val.toString();
    }

    /** Extracts the value of {@code "key":number}. */
    private static Double extractDouble(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start < 0) return null;
        start += search.length();
        int end = start;
        while (end < json.length()) {
            char c = json.charAt(end);
            if (Character.isDigit(c) || c == '.' || c == '-') end++;
            else break;
        }
        if (start == end) return null;
        try {
            return Double.parseDouble(json.substring(start, end));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer extractInt(String json, String key) {
        Double d = extractDouble(json, key);
        return (d == null) ? null : d.intValue();
    }

    /** Extracts the value of {@code "key":true} or {@code "key":false}. */
    private static Boolean extractBoolean(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start < 0) return null;
        start += search.length();
        if (json.startsWith("true",  start)) return true;
        if (json.startsWith("false", start)) return false;
        return null;
    }

    /** Extracts the full {@code {...}} value of {@code "key":{...}}. */
    private static String extractObject(String json, String key) {
        String search = "\"" + key + "\":{";
        int start = json.indexOf(search);
        if (start < 0) return null;
        return extractBalanced(json, start + search.length() - 1, '{', '}');
    }

    /** Extracts the full {@code [...]} value of {@code "key":[...]}. */
    private static String extractArray(String json, String key) {
        String search = "\"" + key + "\":[";
        int start = json.indexOf(search);
        if (start < 0) return null;
        return extractBalanced(json, start + search.length() - 1, '[', ']');
    }

    /**
     * Returns the substring from {@code start} up to and including the
     * matching close character, respecting nesting and quoted strings.
     */
    private static String extractBalanced(String json, int start, char open, char close) {
        int depth = 0;
        int i = start;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '"') {
                i++;
                while (i < json.length()) {
                    char sc = json.charAt(i);
                    if (sc == '\\') { i += 2; continue; }
                    if (sc == '"')  { i++; break; }
                    i++;
                }
                continue;
            }
            if (c == open)  depth++;
            else if (c == close) {
                depth--;
                if (depth == 0) return json.substring(start, i + 1);
            }
            i++;
        }
        return null;
    }

    /**
     * Parses a JSON array of {@code {"suit":...,"rank":...}} objects.
     * Returns null if any card entry is malformed or contains an unknown suit/rank.
     */
    private static List<Card> parseCardArray(String arrayJson) {
        List<Card> cards = new ArrayList<>();
        int i = 1; // skip opening [
        while (i < arrayJson.length() - 1) {
            if (arrayJson.charAt(i) == '{') {
                String obj = extractBalanced(arrayJson, i, '{', '}');
                if (obj == null) return null;
                String suit = extractString(obj, "suit");
                String rank = extractString(obj, "rank");
                if (suit == null || rank == null) return null;
                if (!VALID_SUITS.contains(suit) || !VALID_RANKS.contains(rank)) return null;
                cards.add(new Card(suit, rank));
                i += obj.length();
            } else {
                i++;
            }
        }
        return cards;
    }
}
