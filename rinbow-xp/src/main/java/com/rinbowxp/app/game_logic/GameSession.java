package com.rinbowxp.app.game_logic;

import java.awt.CardLayout;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.swing.JPanel;

import com.rinbowxp.app.GameResultPage;
import com.rinbowxp.app.SoundManager;
import com.rinbowxp.app.SpriteTransition;
import com.rinbowxp.app.word.*;

public class GameSession {
    // Temporary placeholders - will be replaced with actual classes later
    private WordProvider provider; 
    private WordEntry word;
    private String secretWord;  
    private Set<String> guessed;
    private int wrongCount;
    private GameStatus status;
    private int damageLevel;  // TODO: Replace with DamageStatus
    private GameRules rules;
    private int currentRound;  // TODO: Replace with GameRound
    private static final int MAX_ROUNDS = 5;  // 5-round system
    
    private CardLayout cardLayout;  // For UI navigation
    private JPanel cardPanel;        // For UI navigation
    private GameResultPage gameResultPage;  // To show results after game ends
    private SpriteTransition spriteTransition;
    private Difficulty currentDifficulty;  // Track the current difficulty for loading next rounds
    private Runnable onNextRoundReady;  // Callback when next round is ready to display
    
    /**
     * Constructor initializes a new game session
     */
    public GameSession(CardLayout cardLayout, JPanel cardPanel, GameResultPage gameResultPage, SpriteTransition spriteTransition) {
        this.provider = new CsvWordProvider("/com/rinbowxp/app/resources/word-dict/terms.csv");
        this.word = provider.nextWord(Optional.of(Difficulty.EASY));
        this.secretWord = word.term();
        this.guessed = new HashSet<>();
        this.wrongCount = 0;
        this.status = GameStatus.RUNNING;
        this.damageLevel = 0;
        this.rules = new GameRules(8);
        this.currentRound = 1;
        this.currentDifficulty = Difficulty.EASY;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.gameResultPage = gameResultPage;
        this.spriteTransition = spriteTransition;
    }
    /**
     * Makes a guess and updates game state
     * @param letter The letter guessed by the player
     * @return true if guess was correct, false otherwise
     */
    public boolean makeGuess(String letter) {
        letter = letter.toUpperCase();
        
        System.out.println(letter + " is clicked");
        
        // Check if game is already over
        if (status != GameStatus.RUNNING) {
            System.out.println("Game is already over. Cannot make more guesses.");
            return false;
        }
        
        if (!rules.validateGuess(letter, guessed)) {
            return false;  // Invalid or already guessed
        }
        
        guessed.add(letter);
        
        if (secretWord.contains(letter)) {
            System.out.println(letter + " is CORRECT! It's in the word.");
            SoundManager.getInstance().playSFX(SoundManager.SFX.CORRECT_GUESS);
            updateStatus();
            return true;
        } else {
            wrongCount++;
            damageLevel++;
            spriteTransition.next();
            SoundManager.getInstance().playSFX(SoundManager.SFX.EXPLOSION);
            System.out.println(letter + " is WRONG! Not in the word. (" + wrongCount + "/" + rules.getMaxWrongAttempts() + " wrong attempts)");
            updateStatus();
            return false;
        }
    }
    
    /**
     * Checks if the word is completely guessed
     * @return true if all letters are guessed
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
     * Updates game status based on current state
     */
    private void updateStatus() {
        if (isWordComplete()) {
            status = GameStatus.WON;
            announceResult();
        } else if (wrongCount >= rules.getMaxWrongAttempts()) {
            // Immediate game over on word failure - don't proceed to next round
            status = GameStatus.LOST;
            announceResult();
        } else {
            status = GameStatus.RUNNING;
        }
    }
    
    /**
     * Announces the game result in the terminal
     */
    private void announceResult() {
        if (status == GameStatus.WON) {
            SoundManager.getInstance().playSFX(SoundManager.SFX.WIN);
            System.out.println("\n=================================");
            System.out.println("🎉 ROUND " + currentRound + " WON! 🎉");
            System.out.println("The word was: " + secretWord);
            System.out.println("=================================\n");

            gameResultPage.setRoundResult(secretWord, wrongCount, rules.getMaxWrongAttempts(), currentRound);
            cardLayout.show(cardPanel, "Game Result Page");
            // Check if all 5 rounds are complete
            if (currentRound >= MAX_ROUNDS) {
                System.out.println("🏆 GAME COMPLETE! You won all 5 rounds! 🏆");
                gameResultPage.setGameResult(true, secretWord, wrongCount, rules.getMaxWrongAttempts());
                cardLayout.show(cardPanel, "Game Result Page");
                resetRound();
            } else {
                // Load next round automatically without showing result page
                loadNextRound();
            }
        } else if (status == GameStatus.LOST) {
            System.out.println("\n=================================");
            System.out.println("💀 ROUND " + currentRound + " LOST! GAME OVER! 💀");
            System.out.println("The word was: " + secretWord);
            System.out.println("Wrong attempts: " + wrongCount + "/" + rules.getMaxWrongAttempts());
            System.out.println("=================================\n");
            SoundManager.getInstance().playSFX(SoundManager.SFX.GAME_OVER);
            // Defer showing Game Over panel until SpriteTransition's linger completes
            spriteTransition.setOnFinalStageReady(new Runnable() {
                @Override
                public void run() {
                    gameResultPage.setGameResult(false, secretWord, wrongCount, rules.getMaxWrongAttempts());
                    cardLayout.show(cardPanel, "Game Result Page");
                    resetRound();
                }
            });
        }
    }
    
    /**
     * Loads the next round without showing the result page
     */
    private void loadNextRound() {
        // Load the next word with the same difficulty
        this.word = provider.nextWord(Optional.of(currentDifficulty));
        this.secretWord = word.term();
        this.guessed.clear();
        this.wrongCount = 0;
        this.status = GameStatus.RUNNING;
        this.damageLevel = 0;
        this.currentRound++;
        
        // Reset sprite transition for new round
        spriteTransition.reset();
        
        
        // Notify GamePanel to update the display
        if (onNextRoundReady != null) {
            onNextRoundReady.run();
        }
    }
    
    /**
     * Resets the round for a new game or moves to the next round
     */
    public void resetRound() {
        this.secretWord = "HANGMAN";  // TODO: Get from WordProvider
        this.guessed.clear();
        this.wrongCount = 0;
        this.status = GameStatus.RUNNING;
        this.damageLevel = 0;
        this.currentRound++;
        
        // If we've completed all 5 rounds, do not increment further
        if (currentRound > MAX_ROUNDS) {
            this.currentRound = 1;  // Reset for new game
        }
    }

    public void startNewGame(String difficulty) {
        if (difficulty.equalsIgnoreCase("EASY")) {
            this.word = provider.nextWord(Optional.of(Difficulty.EASY));
            this.currentDifficulty = Difficulty.EASY;
        } else if (difficulty.equalsIgnoreCase("MEDIUM")) {
            this.word = provider.nextWord(Optional.of(Difficulty.MEDIUM));
            this.currentDifficulty = Difficulty.MEDIUM;
        } else if (difficulty.equalsIgnoreCase("HARD")) {
            this.word = provider.nextWord(Optional.of(Difficulty.HARD));
            this.currentDifficulty = Difficulty.HARD;
        } else {
            this.word = provider.nextWord(Optional.empty());  // Get next word with any difficulty
            this.currentDifficulty = this.word.difficulty();
        }
        this.secretWord = word.term();
        this.guessed = new HashSet<>();
        this.wrongCount = 0;
        this.status = GameStatus.RUNNING;
        this.damageLevel = 0;
        this.rules = new GameRules(8);
        this.currentRound = 1;
    }
    
    // Getters for UI to read state
    public String getSecretWord() {
        return secretWord;
    }
    
    public Set<String> getGuessed() {
        return new HashSet<>(guessed);  // Return copy for safety
    }
    
    public int getWrongCount() {
        return wrongCount;
    }
    
    public GameStatus getStatus() {
        return status;
    }
    
    public int getDamageLevel() {
        return damageLevel;
    }
    
    public int getCurrentRound() {
        return currentRound;
    }
    
    public int getMaxRounds() {
        return MAX_ROUNDS;
    }
    
    /**
     * Sets the callback for when the next round is ready to display
     */
    public void setOnNextRoundReady(Runnable callback) {
        this.onNextRoundReady = callback;
    }
    
    public int getMaxWrongAttempts() {
        return rules.getMaxWrongAttempts();
    }
    
    public GameRules getRules() {
        return rules;
    }
    
    public String getClue() {
        return word.clue();
    }
    
    public String getDifficulty() {
        return word.difficulty().toString();
    }
    
    /**
     * Gets the word to display with guessed letters revealed
     * @return String with guessed letters and underscores for unknown letters
     */
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
