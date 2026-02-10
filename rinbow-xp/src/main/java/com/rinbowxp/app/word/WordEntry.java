package com.rinbowxp.app.word;

import java.util.Objects;

public class WordEntry {
    private final Difficulty difficulty;
    private final String term;
    private final String clue;

    public WordEntry(Difficulty difficulty, String term, String clue){
        this.difficulty = Objects.requireNonNull(difficulty, "difficulty");
        this.term = normalizeTerm(term);
        this.clue = normalizeClue(clue);
    }

    public Difficulty difficulty() {return difficulty;}
    public String term() { return term; }
    public String clue() { return clue; }
    
    private static String normalizeTerm(String raw){
        if (raw == null) throw new IllegalArgumentException("term is null");
        String t = raw.trim().toUpperCase();

        // this regex is to only allow valid words
        if (!t.matches("[A-Z]+")){
            throw new IllegalArgumentException("Invalid term " + raw);
        }
        if (t.isBlank()) throw new IllegalArgumentException("term is blank");
            return t;
    }

    private static String normalizeClue(String raw){
        if (raw == null) return "";
        return raw.trim();
    }

    @Override
    public String toString() {
        return "WordEntry{difficulty=" + difficulty + ", term='" + term + "'}";
    }
 }
