package com.rinbowxp.app.game_logic;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import com.rinbowxp.app.word.*;

import com.rinbowxp.app.word.CsvWordProvider;
import com.rinbowxp.app.word.WordProvider;

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
    
    /**
     * Constructor initializes a new game session
     */
    public GameSession() {
        this.provider = new CsvWordProvider("../resources/word-dict/terms.csv");
        this.word = provider.nextWord(Optional.of(Difficulty.EASY));
        this.secretWord = word.term();
        this.guessed = new HashSet<>();
        this.wrongCount = 0;
        this.status = GameStatus.RUNNING;
        this.damageLevel = 0;
        this.rules = new GameRules(8);
        this.currentRound = 1;
    }
    
    /**
     * Makes a guess and updates game state
     * @param letter The letter guessed by the player
     * @return true if guess was correct, false otherwise
     */
    public boolean makeGuess(String letter) {
        letter = letter.toUpperCase();
        
        if (!rules.validateGuess(letter, guessed)) {
            return false;  // Invalid or already guessed
        }
        
        guessed.add(letter);
        
        if (secretWord.contains(letter)) {
            updateStatus();
            return true;
        } else {
            wrongCount++;
            damageLevel++;
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
        } else if (wrongCount >= rules.getMaxWrongAttempts()) {
            status = GameStatus.LOST;
        } else {
            status = GameStatus.RUNNING;
        }
    }
    
    /**
     * Resets the round for a new game
     */
    public void resetRound() {
        this.secretWord = "HANGMAN";  // TODO: Get from WordProvider
        this.guessed.clear();
        this.wrongCount = 0;
        this.status = GameStatus.RUNNING;
        this.damageLevel = 0;
        this.currentRound++;
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
    
    public int getMaxWrongAttempts() {
        return rules.getMaxWrongAttempts();
    }
    
    public GameRules getRules() {
        return rules;
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
