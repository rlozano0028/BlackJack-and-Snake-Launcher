package gameManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreService {

    private static final String FILE = "high_scores.txt";

    /**
     * Returns the top `limit` ScoreEntry objects for the given game tag
     * ("BLACKJACK" or "SNAKE"), sorted highest score first.
     */
    public List<ScoreEntry> getTopScores(String game, int limit) {
        List<ScoreEntry> result = new ArrayList<>();
        for (String line : readLines()) {
            String[] parts = line.split(":");
            if (parts.length != 3) continue;
            if (!parts[0].equals(game)) continue;
            try {
                int score = Integer.parseInt(parts[2].trim());
                result.add(new ScoreEntry(parts[1], score));
            } catch (NumberFormatException ignored) {
                // skip malformed score
            }
        }
        result.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());
        return result.subList(0, Math.min(limit, result.size()));
    }

    /**
     * Writes default 1000 entries for both games for the given username,
     * but only if no entry already exists for that (game, username) pair.
     */
    public void ensureDefaultScore(String username) {
        ensureEntry("BLACKJACK", username, 1000);
        ensureEntry("SNAKE", username, 1000);
    }

    /**
     * Updates the score for (game, username). If an entry already exists and
     * the new score is higher, it replaces the old value. If no entry exists,
     * a new line is appended.
     */
    public void updateScore(String game, String username, int newScore) {
        List<String> lines = readLines();
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(":");
            if (parts.length != 3) continue;
            if (parts[0].equals(game) && parts[1].equals(username)) {
                found = true;
                try {
                    int existing = Integer.parseInt(parts[2].trim());
                    if (newScore > existing) {
                        lines.set(i, game + ":" + username + ":" + newScore);
                    }
                } catch (NumberFormatException ignored) {
                    lines.set(i, game + ":" + username + ":" + newScore);
                }
                break;
            }
        }
        if (!found) {
            lines.add(game + ":" + username + ":" + newScore);
        }
        writeLines(lines);
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    private void ensureEntry(String game, String username, int defaultScore) {
        List<String> lines = readLines();
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length != 3) continue;
            if (parts[0].equals(game) && parts[1].equals(username)) {
                return; // already present
            }
        }
        lines.add(game + ":" + username + ":" + defaultScore);
        writeLines(lines);
    }

    private List<String> readLines() {
        File f = new File(FILE);
        if (!f.exists()) {
            return new ArrayList<>();
        }
        try {
            return new ArrayList<>(Files.readAllLines(Paths.get(FILE)));
        } catch (IOException e) {
            System.err.println("ScoreService: could not read " + FILE + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void writeLines(List<String> lines) {
        try (FileWriter fw = new FileWriter(FILE, false)) {
            for (String line : lines) {
                if (!line.isBlank()) {
                    fw.write(line + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("ScoreService: could not write " + FILE + ": " + e.getMessage());
        }
    }
}
