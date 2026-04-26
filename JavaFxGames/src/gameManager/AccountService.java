package gameManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private static final String FILE = "user_accounts.txt";

    // Returns true if username:password pair exists in file.
    public boolean validateLogin(String username, String password) {
        for (String line : readLines()) {
            int idx = line.indexOf(':');
            if (idx < 0) continue;
            String u = line.substring(0, idx);
            String p = line.substring(idx + 1);
            if (u.equals(username) && p.equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Returns true if any line has the given username (before the first colon).
    public boolean accountExists(String username) {
        for (String line : readLines()) {
            int idx = line.indexOf(':');
            if (idx < 0) continue;
            if (line.substring(0, idx).equals(username)) {
                return true;
            }
        }
        return false;
    }

    // Appends username:password. Returns false if username already taken.
    public boolean createAccount(String username, String password) {
        if (accountExists(username)) {
            return false;
        }
        try (FileWriter fw = new FileWriter(FILE, true)) {
            fw.write(username + ":" + password + "\n");
        } catch (IOException e) {
            System.err.println("AccountService: could not write to " + FILE + ": " + e.getMessage());
        }
        return true;
    }

    // Returns true when the accounts file is absent (no accounts have been created yet).
    public boolean fileIsMissing() {
        return !new File(FILE).exists();
    }

    private List<String> readLines() {
        File f = new File(FILE);
        if (!f.exists()) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(Paths.get(FILE));
        } catch (IOException e) {
            System.err.println("AccountService: could not read " + FILE + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
