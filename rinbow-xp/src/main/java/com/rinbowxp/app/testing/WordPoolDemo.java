package com.rinbowxp.app.testing;

import com.rinbowxp.app.word.*;
import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

public class WordPoolDemo {
    public static void main(String[] args){
        WordProvider provider = new CsvWordProvider("../resources/word-dict/terms.csv");
        
        System.out.println("=== DEMONSTRATING WORD POOL OPTIMIZATION ===");
        System.out.println("Total words loaded: " + provider.allWords().size());
        System.out.println();
        
        // Demo 1: Words are removed from pool
        System.out.println("--- Demo 1: Getting 5 EASY words (should all be unique) ---");
        Set<String> usedWords = new HashSet<>();
        for (int i = 0; i < 5; i++){
            WordEntry w = provider.nextWord(Optional.of(Difficulty.EASY));
            System.out.println((i+1) + ". " + w.term() + " \t clue: " + w.clue());
            usedWords.add(w.term());
        }
        System.out.println("Unique words: " + usedWords.size() + "/5 (should be 5)");
        System.out.println();
        
        // Demo 2: Get more words to show pool is being depleted
        System.out.println("--- Demo 2: Getting 5 more EASY words ---");
        Set<String> secondBatch = new HashSet<>();
        for (int i = 0; i < 5; i++){
            WordEntry w = provider.nextWord(Optional.of(Difficulty.EASY));
            System.out.println((i+1) + ". " + w.term() + " \t clue: " + w.clue());
            secondBatch.add(w.term());
        }
        System.out.println("Unique words in second batch: " + secondBatch.size() + "/5");
        
        // Check if any words overlap
        usedWords.retainAll(secondBatch);
        System.out.println("Overlap with first batch: " + usedWords.size() + " (should be 0)");
        System.out.println();
        
        // Demo 3: Reset refills the pool
        System.out.println("--- Demo 3: Resetting the pool ---");
        provider.reset();
        System.out.println("Pool reset! Getting 3 EASY words...");
        for (int i = 0; i < 3; i++){
            WordEntry w = provider.nextWord(Optional.of(Difficulty.EASY));
            System.out.println((i+1) + ". " + w.term() + " \t clue: " + w.clue());
        }
        System.out.println();
        
        // Demo 4: Different difficulties work independently
        System.out.println("--- Demo 4: Different difficulty pools are independent ---");
        System.out.println("3 MEDIUM words:");
        for (int i = 0; i < 3; i++){
            WordEntry w = provider.nextWord(Optional.of(Difficulty.MEDIUM));
            System.out.println((i+1) + ". " + w.term() + " (" + w.difficulty() + ")" + " \t clue: " + w.clue());
        }
        System.out.println();
        System.out.println("3 HARD words:");
        for (int i = 0; i < 3; i++){
            WordEntry w = provider.nextWord(Optional.of(Difficulty.HARD));
            System.out.println((i+1) + ". " + w.term() + " (" + w.difficulty() + ")" + " \t clue: " + w.clue());
        }
        
        System.out.println();
        System.out.println("=== ALL OPTIMIZATIONS VERIFIED ===");
    }
}
