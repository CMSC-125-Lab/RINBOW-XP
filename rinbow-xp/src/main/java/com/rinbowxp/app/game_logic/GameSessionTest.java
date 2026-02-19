package com.rinbowxp.app.game_logic;

import java.util.Scanner;

public class GameSessionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameSession game = new GameSession(null, null, null, null);
        
        System.out.println("=== HANGMAN GAME ===");
        System.out.println("Guess the word letter by letter!");
        System.out.println("You have " + game.getMaxWrongAttempts() + " wrong attempts allowed.");
        System.out.println(game.getStatus());
        
        // Game loop
        while (game.getStatus().toString().equals("RUNNING")) {
            System.out.println("Word: " + game.getDisplayWord());
            System.out.println("Wrong attempts: " + game.getWrongCount() + "/" + game.getMaxWrongAttempts());
            System.out.println("Guessed letters: " + game.getGuessed());
            System.out.println();
            
            System.out.print("Enter a letter: ");
            String input = scanner.nextLine().trim().toUpperCase();
            
            // Validate input
            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.println("Please enter a single letter!");
                System.out.println();
                continue;
            }
            
            // Check if already guessed
            if (game.getGuessed().contains(input)) {
                System.out.println("You already guessed that letter!");
                System.out.println();
                continue;
            }
            
            // Make guess
            boolean correct = game.makeGuess(input);
            
            if (correct) {
                System.out.println("✓ Correct!");
            } else {
                System.out.println("✗ Wrong! Damage level: " + game.getDamageLevel());
            }
            System.out.println();
        }
        
        // Ending
        if (game.getStatus().toString().equals("WON")) {
            System.out.println("🎉 Congratulations! You WON!");
        } else {
            System.out.println("💀 Game Over! You LOST!");
        }

        System.out.println(game.getSecretWord());
        
        System.out.println("Wrong attempts: " + game.getWrongCount() + "/" + game.getMaxWrongAttempts());
        
        scanner.close();
    }
}
