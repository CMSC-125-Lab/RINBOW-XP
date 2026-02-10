package com.rinbowxp.app.word;

public enum Difficulty {
    EASY, MEDIUM, HARD;

    public static Difficulty fromCsv(String raw){
        if (raw == null) throw new IllegalArgumentException("Difficulty is null");
        return switch(raw.trim().toUpperCase()){
            case "EASY" -> EASY;
            case "MEDIUM" -> MEDIUM;
            case "HARD" -> HARD;
            default -> throw new IllegalArgumentException("Unknown difficulty" + raw);
        };
    }
}
