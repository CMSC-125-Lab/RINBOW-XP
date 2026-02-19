package com.rinbowxp.app.testing;

import com.rinbowxp.app.word.*;
import java.util.Optional;

public class CsvProviderTest {
    public static void main(String[] args){
        WordProvider provider = new CsvWordProvider("../resources/word-dict/terms.csv");
        
        System.out.println("Loaded words: " + provider.allWords().size());
        System.out.println("----- SAMPLE WORDS -----");

        for (int i = 0; i < 5; i++){
            WordEntry w = provider.nextWord(Optional.of(Difficulty.EASY));
            System.out.println(w.term() + " -> " + w.clue());
        }
    }
}
