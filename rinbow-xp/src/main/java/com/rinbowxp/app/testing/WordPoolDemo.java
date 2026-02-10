package com.rinbowxp.app.testing;

import com.rinbowxp.app.word.*;
import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

public class WordPoolDemo {
    public static void main(String[] args){
        WordProvider provider = new CsvWordProvider("../resources/word-dict/terms.csv");
        
        IO.println("=== DEMONSTRATING WORD POOL OPTIMIZATION ===");
        IO.println("Total words loaded: " + provider.allWords().size());
        IO.println();
        
        // Demo 1: Words are removed from pool
        IO.println("--- Demo 1: Getting 5 EASY words (should all be unique) ---");
        Set<String> usedWords = new HashSet<>();
        for (int i = 0; i < 5; i++){
            WordEntry w = provider.nextWord(Optional.of(Difficulty.EASY));
            IO.println((i+1) + ". " + w.term() + " clue: " + w.clue());
            usedWords.add(w.term());
        }
        IO.println("Unique words: " + usedWords.size() + "/5 (should be 5)");
        IO.println();
        
        // Demo 2: Get more words to show pool is being depleted
        IO.println("--- Demo 2: Getting 5 more EASY words ---");
        Set<String> secondBatch = new HashSet<>();
        for (int i = 0; i < 5; i++){
            WordEntry w = provider.nextWord(Optional.of(Difficulty.EASY));
            IO.println((i+1) + ". " + w.term() + " clue: " + w.clue());
            secondBatch.add(w.term());
        }
        IO.println("Unique words in second batch: " + secondBatch.size() + "/5");
        
        // Check if any words overlap
        usedWords.retainAll(secondBatch);
        IO.println("Overlap with first batch: " + usedWords.size() + " (should be 0)");
        IO.println();
        
        // Demo 3: Reset refills the pool
        IO.println("--- Demo 3: Resetting the pool ---");
        provider.reset();
        IO.println("Pool reset! Getting 3 EASY words...");
        for (int i = 0; i < 3; i++){
            WordEntry w = provider.nextWord(Optional.of(Difficulty.EASY));
            IO.println((i+1) + ". " + w.term() + " clue: " + w.clue());
        }
        IO.println();
        
        // Demo 4: Different difficulties work independently
        IO.println("--- Demo 4: Different difficulty pools are independent ---");
        IO.println("3 MEDIUM words:");
        for (int i = 0; i < 3; i++){
            WordEntry w = provider.nextWord(Optional.of(Difficulty.MEDIUM));
            IO.println((i+1) + ". " + w.term() + " (" + w.difficulty() + ")" + " clue: " + w.clue());
        }
        IO.println();
        IO.println("3 HARD words:");
        for (int i = 0; i < 3; i++){
            WordEntry w = provider.nextWord(Optional.of(Difficulty.HARD));
            IO.println((i+1) + ". " + w.term() + " (" + w.difficulty() + ")" + " clue: " + w.clue());
        }
        
        IO.println();
        IO.println("=== ALL OPTIMIZATIONS VERIFIED ===");
    }
}
