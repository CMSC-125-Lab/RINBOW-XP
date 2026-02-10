package com.rinbowxp.app.testing;

import com.rinbowxp.app.word.*;
import java.util.Optional;

public class CsvProviderTest {
    public static void main(String[] args){
        WordProvider provider = new CsvWordProvider("../resources/word-dict/terms.csv");
        
        IO.println("Loaded words: " + provider.allWords().size());
        IO.println("----- SAMPLE WORDS -----");

        for (int i = 0; i < 5; i++){
            WordEntry w = provider.nextWord(Optional.of(Difficulty.EASY));
            IO.println(w.term() + " -> " + w.clue());
        }
    }
}
