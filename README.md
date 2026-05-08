<div align="center">

# Game Manager System — CS151 Spring 2026

### BlackJack + Snake Game JavaFX Project

</div>

---

## Overview

This project is a **JavaFX-based Game Manager application** that allows users to create accounts, log in, view high scores, and launch two games from one central menu.

The application includes:

- **BlackJack** — a turn-based card game with AI players, betting, and save/load functionality
- **Snake Game** — a real-time Snake game using keyboard controls
- **Account System** — user accounts saved locally in `user_accounts.txt`
- **High Score System** — scores for both games saved in one shared file, `high_scores.txt`
- **Persistent Toolbar** — navigation available at all times after login

---

## Key Features

| Feature | Description |
|---|---|
| **User Login** | Users can create accounts and log in with saved credentials |
| **Persistent Accounts** | Account data remains after closing and reopening the program |
| **High Scores** | Top 5 scores for BlackJack and Snake are displayed in the main menu |
| **Game Launcher** | Users can choose BlackJack or Snake from the main menu |
| **Toolbar Navigation** | Main Menu and Log Out buttons remain available after login |
| **BlackJack** | Includes betting, AI players, dealer logic, hit/stand actions, and money tracking |
| **Snake** | Includes arrow-key movement, food, growth, score tracking, pause, collision detection, and restart |

---

## Design

### Package Structure

| Package | Classes | Description |
|---|---|---|
| `gameManager` | `GameManager`, `AccountService`, `ScoreService`, `ScoreEntry` | Handles login, account storage, high scores, main menu, and navigation |
| `blackjack` | `Main`, `BlackjackGame`, `Player`, `Dealer`, `Deck`, `Card`, `Bets`, `CardView` | Contains BlackJack game logic, UI, cards, players, betting, and dealer behavior |
| `snake` | `GamePane`, `SnakeGameLogic`, `Snake`, `Fruit`, `GameObject`, `Direction`, `Position`, `Resettable`, `SoundPlayer` | Contains Snake game logic, rendering, movement, food, pause, scoring, and sound |
| `images` | Fruit images and game assets | Image resources used by the games |
| `sound` | Music/audio files | Audio resources used by the games |

---

## Object-Oriented Design

### Inheritance

- `Dealer` extends `Player` in the BlackJack package.
- `Snake` extends `GameObject` in the Snake package.
- `Fruit` extends `GameObject` in the Snake package.

### Interfaces

- `Resettable` is used in the Snake package to define restart behavior.

### Encapsulation

- Game data is managed through classes such as `BlackjackGame`, `SnakeGameLogic`, `AccountService`, `ScoreService`, and `GameStateSaver1`.
- Account and score data are handled through service classes instead of being written directly inside the UI.
- BlackJack save/load logic is handled through GameStateSaver.

### Separation of Responsibilities

| Class | Responsibility |
|---|---|
| `GameManager` | Main JavaFX application, login screens, main menu, toolbar, and game launching |
| `AccountService` | Reads and writes account data in `user_accounts.txt` |
| `ScoreService` | Reads, writes, updates, and sorts high scores in `high_scores.txt` |
| `BlackjackGame` | Handles BlackJack rules and game state |
| `GameStateSaver` | Saves and loads BlackJack game state using save strings |
| `Main` | Builds and updates the BlackJack JavaFX interface |
| `SnakeGameLogic` | Handles Snake movement, fruit collection, collision, scoring, pause, and restart logic |
| `GamePane` | Draws the Snake game board and handles keyboard input |

---

## Installation Instructions

<div>

### Prerequisites

Before running the project, install the following:

<ul>
  <li><strong>Java JDK 17 or higher</strong></li>
  <li><strong>JavaFX SDK</strong></li>
  <li><strong>Git</strong></li>
</ul>

---

### 1. Install Java

Download and install Java JDK 17 or higher from Oracle:

<a href="https://www.oracle.com/java/technologies/downloads/">Oracle Java Downloads</a>

After installation, confirm Java is installed by opening Terminal, Command Prompt, or PowerShell and running:

```bash
java -version
```

---

### 2. Install JavaFX

Download the JavaFX SDK from Gluon:

<a href="https://gluonhq.com/products/javafx/">Gluon JavaFX Downloads</a>

Choose the JavaFX SDK for your operating system, then extract the folder somewhere easy to find.

Example Windows location:

```text
C:/Users/YourName/Downloads/javafx-sdk-26/lib
```

Example Mac location:

```text
/Users/YourName/Downloads/javafx-sdk-26/lib
```

<p><strong>Important:</strong> The JavaFX path used later must point to the <code>lib</code> folder inside the JavaFX SDK.</p>

---

### 3. Clone the Project

Open Terminal, Command Prompt, or PowerShell and run:

```bash
# 1. Make a folder for the project
mkdir GameManagerProject
cd GameManagerProject

# 2. Clone the repository
git clone git@github.com:rlozano0028/CS-151-06-Spring26-Project3-Roman-Jonas-Conrad-Trung.git

# 3. Go into the JavaFX game folder
cd CS-151-06-Spring26-Project3-Roman-Jonas-Conrad-Trung/JavaFxGames
```

---

### 4. Compile the Project

Replace the JavaFX path below with the location of your own JavaFX <code>lib</code> folder.

#### Windows Example

```bash
javac --module-path "C:/Users/YourName/Downloads/javafx-sdk-26/lib" --add-modules javafx.controls,javafx.fxml,javafx.media -d bin src/gameManager/*.java src/blackjack/*.java src/snake/*.java
```

#### Mac Example

```bash
javac --module-path "/Users/YourName/Downloads/javafx-sdk-26/lib" --add-modules javafx.controls,javafx.fxml,javafx.media -d bin src/gameManager/*.java src/blackjack/*.java src/snake/*.java
```

---

### 5. Run the Program

Again, replace the JavaFX path below with your own JavaFX <code>lib</code> folder path.

#### Windows Example

```bash
java --module-path "C:/Users/YourName/Downloads/javafx-sdk-26/lib" --add-modules javafx.controls,javafx.fxml,javafx.media -cp bin gameManager.GameManager
```

#### Mac Example

```bash
java --module-path "/Users/YourName/Downloads/javafx-sdk-26/lib" --add-modules javafx.controls,javafx.fxml,javafx.media -cp bin gameManager.GameManager
```


---

## Usage

### Starting the Application

When the application opens, the user will see two options:

1. **Log In**
2. **Create Account**

New users should click **Create Account** first.

---

### Creating an Account

1. Click **Create Account**.
2. Enter a username.
3. Enter a password.
4. Click **Create**.
5. The account will be saved in:

```text
user_accounts.txt
```

---

### Logging In

1. Click **Log In**.
2. Enter the username and password.
3. Click **Log In**.
4. After logging in, the main menu will appear.

---

### Main Menu

The main menu has two main sections.

| Section | Description |
|---|---|
| **High Scores** | Shows the top 5 scores for BlackJack and Snake |
| **Game Launcher** | Lets the user open BlackJack or Snake |

The main menu also includes placeholder buttons for future games.

---

## BlackJack Usage

### How to Open BlackJack

1. Log in.
2. Go to the main menu.
3. Click **Open BlackJack**.
4. Choose:
   
| Option | Description |
|---|---|
| **New Game** | Starts a new BlackJack game |
| **Load Game** | Paste a save string to restore a previous game |

---

### BlackJack Controls

| Button | Action |
|---|---|
| **Initial Bet** | Places the first bet before the round starts |
| **Deal** | Deals cards and begins the round |
| **Hit** | Draws another card |
| **Stand** | Ends the human player's turn |
| **Raise** | Increases the current bet when allowed |
| **Reset** | Starts over with a new BlackJack game |

---

### BlackJack Rules

- The game includes one human player, two AI players, and one dealer.
- The turn order is:

```text
Human Player → AI 1 → AI 2 → Dealer
```

- The player may hit or stand.
- AI players act automatically.
- The dealer follows BlackJack dealer rules.
- The winner is determined by comparing hand values against the dealer.
- Players lose if they bust or have a lower hand value than the dealer.
- Players win if the dealer busts or if their hand beats the dealer.

---

## Snake Game Usage

### How to Open Snake

1. Log in.
2. Go to the main menu.
3. Click **Open Snake**.

---

### Snake Controls

| Key | Action |
|---|---|
| **Up Arrow** | Move up |
| **Down Arrow** | Move down |
| **Left Arrow** | Move left |
| **Right Arrow** | Move right |
| **Escape** | Pause or resume the game |

---

### Snake Rules

- The snake moves continuously in the current direction.
- The snake grows when it eats fruit.
- The score increases when fruit is collected.
- Fruit appears randomly on the board.
- The game ends if the snake hits a wall or itself.
- A game over screen shows the final score and a restart button.

---

## Data Files

The project uses two text files to save data.

| File | Purpose |
|---|---|
| `user_accounts.txt` | Stores usernames and passwords |
| `high_scores.txt` | Stores BlackJack and Snake high scores |

Example account format:

```text
username:password
```

Example high score format:

```text
BLACKJACK:username:1000
SNAKE:username:1000
```

---
## Contributions

### Roman

- Game logic – implemented BlackJack rules and gameplay
- UI – designed the BlackJack interface
- Game flow – managed how each round progresses

### Jonas

- AI logic – handled how AI players decide to hit or stand
- Players & Cards – built the core BlackJack classes
- Connected visuals to logic – made the UI update based on game actions
- JUnit tests – tested BlackJack functionality

### Conrad

- GamePane – handled drawing and visuals for Snake
- GameVariables – set up grid and size constants
- Position – tracked object locations on the board
- Snake – built movement, growth, and collisions
- Music – added background music
- JUnit tests – tested Snake components

### Trung

- Direction – handled movement and direction logic
- Fruit – implemented food spawning
- GameObject – created a base class for game objects
- Resettable – added restart functionality interface
- SnakeGameLogic – handled the core game loop and updates
