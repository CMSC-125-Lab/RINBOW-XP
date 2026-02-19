package com.rinbowxp.app.game_logic;

import java.awt.CardLayout;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.swing.JPanel;

import com.rinbowxp.app.GameResultPage;
import com.rinbowxp.app.SpriteTransition;
import com.rinbowxp.app.word.*;

public class GameSession {
    private WordProvider provider;
    private WordEntry word;
    private String secretWord;
    private Set<String> guessed;
    private int wrongCount;
    private GameStatus status;
    private int damageLevel;
    private GameRules rules;
    private int currentRound;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private GameResultPage gameResultPage;
    private SpriteTransition spriteTransition;

    public GameSession(CardLayout cardLayout, JPanel cardPanel, GameResultPage gameResultPage, SpriteTransition spriteTransition) {
        this.provider = new CsvWordProvider("../resources/word-dict/terms.csv");
        this.word = provider.nextWord(Optional.of(Difficulty.EASY));
        this.secretWord = word.term();
        this.guessed = new HashSet<>();
        this.wrongCount = 0;
        this.status = GameStatus.RUNNING;
        this.damageLevel = 0;
        this.rules = new GameRules(8);
        this.currentRound = 1;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.gameResultPage = gameResultPage;
        this.spriteTransition = spriteTransition;

        // Tell SpriteTransition to call us back once stage8.gif has lingered.
        // This is the only place where game-over navigation happens on a LOST result.
        if (spriteTransition != null) {
            spriteTransition.setOnFinalStageReady(this::onFinalStageReady);
        }
    }

    private void onFinalStageReady() {
        System.out.println("\n=================================");
        System.out.println("💀 GAME OVER! YOU LOST! 💀");
        System.out.println("The word was: " + secretWord);
        System.out.println("Wrong attempts: " + wrongCount + "/" + rules.getMaxWrongAttempts());
        System.out.println("=================================\n");
        gameResultPage.setGameResult(false, secretWord, wrongCount, rules.getMaxWrongAttempts());
        cardLayout.show(cardPanel, "Game Result Page");
        resetRound();
    }

    /**
     * Makes a guess and updates game state.
     * @param letter The letter guessed by the player
     * @return true if guess was correct, false otherwise
     */
    public boolean makeGuess(String letter) {
        letter = letter.toUpperCase();

        System.out.println(letter + " is clicked");

        if (status != GameStatus.RUNNING) {
            System.out.println("Game is already over. Cannot make more guesses.");
            return false;
        }

        if (!rules.validateGuess(letter, guessed)) {
            return false;
        }

        guessed.add(letter);

        if (secretWord.contains(letter)) {
            System.out.println(letter + " is CORRECT! It's in the word.");
            updateStatus();
            return true;
        } else {
            wrongCount++;
            damageLevel++;
            // next() triggers: transition GIF → stage GIF → (if final stage) linger → onFinalStageReady
            spriteTransition.next();
            System.out.println(letter + " is WRONG! Not in the word. (" + wrongCount + "/" + rules.getMaxWrongAttempts() + " wrong attempts)");
            // Only update status for WIN check here.
            // LOST navigation is handled by onFinalStageReady() after the GIF sequence completes.
            updateStatus();
            return false;
        }
    }

    /**
     * Checks if the word is completely guessed.
     */
    public boolean isWordComplete() {
        for (char c : secretWord.toCharArray()) {
            if (!guessed.contains(String.valueOf(c))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates game status.
     * For LOST: sets status but does NOT navigate — navigation is deferred to onFinalStageReady().
     * For WON: navigates immediately.
     */
    private void updateStatus() {
        if (isWordComplete()) {
            status = GameStatus.WON;
            announceWin();
        } else if (wrongCount >= rules.getMaxWrongAttempts()) {
            // Mark as lost so no more guesses are accepted, but do NOT navigate yet.
            // SpriteTransition will call onFinalStageReady() after the GIF sequence finishes.
            status = GameStatus.LOST;
            System.out.println("[GameSession] Game lost. Waiting for SpriteTransition to complete before showing Game Over.");
        } else {
            status = GameStatus.RUNNING;
        }
    }

    /**
     * Handles a WIN immediately — no GIF delay needed for winning.
     */
    private void announceWin() {
        System.out.println("\n=================================");
        System.out.println("🎉 CONGRATULATIONS! YOU WON! 🎉");
        System.out.println("The word was: " + secretWord);
        System.out.println("=================================\n");
        gameResultPage.setGameResult(true, secretWord, wrongCount, rules.getMaxWrongAttempts());
        cardLayout.show(cardPanel, "Game Result Page");
        resetRound();
    }

    /**
     * Resets the round for a new game.
     */
    public void resetRound() {
        this.secretWord = "HANGMAN";  // TODO: Get from WordProvider
        this.guessed.clear();
        this.wrongCount = 0;
        this.status = GameStatus.RUNNING;
        this.damageLevel = 0;
        this.currentRound++;
    }

    public void startNewGame(String difficulty) {
        if (difficulty.equalsIgnoreCase("EASY")) {
            this.word = provider.nextWord(Optional.of(Difficulty.EASY));
        } else if (difficulty.equalsIgnoreCase("MEDIUM")) {
            this.word = provider.nextWord(Optional.of(Difficulty.MEDIUM));
        } else if (difficulty.equalsIgnoreCase("HARD")) {
            this.word = provider.nextWord(Optional.of(Difficulty.HARD));
        } else {
            this.word = provider.nextWord(Optional.empty());
        }
        this.secretWord = word.term();
        this.guessed = new HashSet<>();
        this.wrongCount = 0;
        this.status = GameStatus.RUNNING;
        this.damageLevel = 0;
        this.rules = new GameRules(8);
        this.currentRound = 1;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getSecretWord() { return secretWord; }

    public Set<String> getGuessed() { return new HashSet<>(guessed); }

    public int getWrongCount() { return wrongCount; }

    public GameStatus getStatus() { return status; }

    public int getDamageLevel() { return damageLevel; }

    public int getCurrentRound() { return currentRound; }

    public int getMaxWrongAttempts() { return rules.getMaxWrongAttempts(); }

    public GameRules getRules() { return rules; }

    public String getClue() { return word.clue(); }

    public String getDifficulty() { return word.difficulty().toString(); }

    public String getDisplayWord() {
        StringBuilder display = new StringBuilder();
        for (char c : secretWord.toCharArray()) {
            if (guessed.contains(String.valueOf(c))) {
                display.append(c).append(" ");
            } else {
                display.append("_ ");
            }
        }
        return display.toString().trim();
    }
}