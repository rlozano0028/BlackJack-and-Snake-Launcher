package gameManager.tests;
import gameManager.AccountService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class AccountServiceTest {

    private static final String ACCOUNTS_FILE = "user_accounts.txt";
    private AccountService accountService;

    @Before
    public void setUp() {
        deleteFile(ACCOUNTS_FILE);
        accountService = new AccountService();
    }

    @After
    public void tearDown() {
        deleteFile(ACCOUNTS_FILE);
    }

    @Test
    public void fileIsMissing_returnsTrueWhenNoFile() {
        assertTrue(accountService.fileIsMissing());
    }

    @Test
    public void fileIsMissing_returnsFalseAfterAccountCreated() {
        accountService.createAccount("user1", "pass1");
        assertFalse(accountService.fileIsMissing());
    }


    @Test
    public void createAccount_returnsTrueForNewUser() {
        assertTrue(accountService.createAccount("alice", "secret"));
    }

    @Test
    public void createAccount_returnsFalseForDuplicateUsername() {
        accountService.createAccount("alice", "secret");
        assertFalse(accountService.createAccount("alice", "different"));
    }

    @Test
    public void createAccount_allowsSamePasswordDifferentUsername() {
        assertTrue(accountService.createAccount("alice", "shared"));
        assertTrue(accountService.createAccount("bob",   "shared"));
    }

    @Test
    public void createAccount_createsFileOnDisk() {
        accountService.createAccount("user1", "pass1");
        assertTrue(new File(ACCOUNTS_FILE).exists());
    }

    @Test
    public void accountExists_returnsFalseWhenNoAccounts() {
        assertFalse(accountService.accountExists("ghost"));
    }

    @Test
    public void accountExists_returnsTrueAfterCreation() {
        accountService.createAccount("alice", "pass");
        assertTrue(accountService.accountExists("alice"));
    }

    @Test
    public void accountExists_returnsFalseForUnknownUser() {
        accountService.createAccount("alice", "pass");
        assertFalse(accountService.accountExists("bob"));
    }

    @Test
    public void accountExists_isCaseSensitive() {
        accountService.createAccount("Alice", "pass");
        assertFalse(accountService.accountExists("alice"));
    }


    @Test
    public void validateLogin_returnsTrueForCorrectCredentials() {
        accountService.createAccount("alice", "mypassword");
        assertTrue(accountService.validateLogin("alice", "mypassword"));
    }

    @Test
    public void validateLogin_returnsFalseForWrongPassword() {
        accountService.createAccount("alice", "mypassword");
        assertFalse(accountService.validateLogin("alice", "wrongpassword"));
    }

    @Test
    public void validateLogin_returnsFalseForUnknownUser() {
        assertFalse(accountService.validateLogin("nobody", "pass"));
    }

    @Test
    public void validateLogin_returnsFalseWhenFileAbsent() {
        assertFalse(accountService.validateLogin("alice", "pass"));
    }

    @Test
    public void validateLogin_isCaseSensitiveForUsername() {
        accountService.createAccount("Alice", "pass");
        assertFalse(accountService.validateLogin("alice", "pass"));
    }

    @Test
    public void validateLogin_isCaseSensitiveForPassword() {
        accountService.createAccount("alice", "Pass");
        assertFalse(accountService.validateLogin("alice", "pass"));
    }

    @Test
    public void validateLogin_worksWithMultipleAccounts() {
        accountService.createAccount("alice", "pass1");
        accountService.createAccount("bob",   "pass2");
        accountService.createAccount("carol", "pass3");
        assertTrue(accountService.validateLogin("bob", "pass2"));
    }

    @Test
    public void validateLogin_passwordWithColonCharacter() throws IOException {
        // Manually write a line that has a colon in the password to verify
        // substring-based parsing reads only the first colon as the delimiter.
        try (FileWriter fw = new FileWriter(ACCOUNTS_FILE, true)) {
            fw.write("tricky:pass:word\n");
        }
        assertTrue(accountService.validateLogin("tricky", "pass:word"));
    }

    // -----------------------------------------------------------------------
    private void deleteFile(String path) {
        File f = new File(path);
        if (f.exists()) f.delete();
    }
}