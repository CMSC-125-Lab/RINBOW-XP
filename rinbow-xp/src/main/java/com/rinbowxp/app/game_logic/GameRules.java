package com.rinbowxp.app.game_logic;

import java.util.Set;

/**
 * Defines and enforces the rules of the game.
 */
public class GameRules {
    private final int maxWrongAttempts;
    
    /**
     * Creates game rules with the specified maximum wrong attempts.
     * @param maxWrongAttempts The maximum number of wrong guesses allowed
     */
    public GameRules(int maxWrongAttempts) {
        this.maxWrongAttempts = maxWrongAttempts;
    }
    
    /**
     * Creates game rules with default settings (6 wrong attempts).
     */
    public GameRules() {
        this(8);
    }
    
    /**
     * Gets the maximum number of wrong attempts allowed.
     * @return The maximum wrong attempts
     */
    public int getMaxWrongAttempts() {
        return maxWrongAttempts;
    }
    
    /**
     * Validates whether a guess is acceptable.
     * @param guess The letter guessed by the player
     * @param alreadyGuessed Set of letters already guessed
     * @return true if the guess is valid, false otherwise
     */
    public boolean validateGuess(String guess, Set<String> alreadyGuessed) {
        if (guess == null || guess.isEmpty()) {
            return false;
        }
        
        // Only single letters allowed
        if (guess.length() != 1) {
            return false;
        }
        
        // Must be a letter
        if (!Character.isLetter(guess.charAt(0))) {
            return false;
        }
        
        // Can't guess the same letter twice
        if (alreadyGuessed.contains(guess.toUpperCase())) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Applies a hint by revealing a letter from the secret word.
     * @param secretWord The secret word
     * @param guessed Set of already guessed letters
     * @return A letter from the secret word that hasn't been guessed yet, or null if all are guessed
     */
    public String applyHint(String secretWord, Set<String> guessed) {
        for (char c : secretWord.toCharArray()) {
            String letter = String.valueOf(c).toUpperCase();
            if (!guessed.contains(letter)) {
                return letter;
            }
        }
        return null; // All letters already guessed
    }
}
