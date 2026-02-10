package com.rinbowxp.app.word;

import java.util.List;
import java.util.Optional;

public interface WordProvider {
    WordEntry nextWord(Optional<Difficulty> difficulty);
    List<WordEntry> allWords();
    void reset();
}