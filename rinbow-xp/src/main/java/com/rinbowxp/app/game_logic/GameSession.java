package com.rinbowxp.app.game_logic;

import java.util.HashSet;
import java.util.Set;

public class GameSession {
    // Temporary placeholders - will be replaced with actual classes later
    private String secretWord;  // TODO: Replace with WordProvider
    private Set<String> guessed;
    private int wrongCount;
    private String status;  // TODO: Replace with GameStatus enum (RUNNING, WON, LOST)
    private int damageLevel;  // TODO: Replace with DamageStatus
    private int maxWrongAttempts;  // TODO: Replace with GameRules
    private int currentRound;  // TODO: Replace with GameRound
    
    /**
     * Constructor initializes a new game session
     */
    public GameSession() {
        this.secretWord = "HANGMAN";  // Temporary hardcoded word
        this.guessed = new HashSet<>();
        this.wrongCount = 0;
        this.status = "RUNNING";
        this.damageLevel = 0;
        this.maxWrongAttempts = 6;
        this.currentRound = 1;
    }
    
    /**
     * Makes a guess and updates game state
     * @param letter The letter guessed by the player
     * @return true if guess was correct, false otherwise
     */
    public boolean makeGuess(String letter) {
        letter = letter.toUpperCase();
        
        if (guessed.contains(letter)) {
            return false;  // Already guessed
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
            status = "WON";
        } else if (wrongCount >= maxWrongAttempts) {
            status = "LOST";
        } else {
            status = "RUNNING";
        }
    }
    
    /**
     * Resets the round for a new game
     */
    public void resetRound() {
        this.secretWord = "HANGMAN";  // TODO: Get from WordProvider
        this.guessed.clear();
        this.wrongCount = 0;
        this.status = "RUNNING";
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
    
    public String getStatus() {
        return status;
    }
    
    public int getDamageLevel() {
        return damageLevel;
    }
    
    public int getCurrentRound() {
        return currentRound;
    }
    
    public int getMaxWrongAttempts() {
        return maxWrongAttempts;
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
