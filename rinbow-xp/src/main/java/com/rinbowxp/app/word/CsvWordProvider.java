package com.rinbowxp.app.word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public final class CsvWordProvider implements WordProvider {
    private final List<WordEntry> allWords;
    private final Map<Difficulty, List<WordEntry>> poolsByDifficulty;
    private final Random rng;

    public CsvWordProvider(String resourcePath){
        this(resourcePath, new Random());
    }

    public CsvWordProvider(String resourcePath, Random rng){
        this.rng = Objects.requireNonNull(rng, "rng");
        this.allWords = loadFromResources(resourcePath);
        if (allWords.isEmpty()){
            throw new IllegalStateException("No words loaded from: " + resourcePath);
        }
        this.poolsByDifficulty = new EnumMap<>(Difficulty.class);
        reset();
    }

    @Override
    public WordEntry nextWord(Optional<Difficulty> difficulty){
        List<WordEntry> pool = difficulty
            .map(poolsByDifficulty::get)
            .orElseGet(() -> {
                // If no difficulty specified, get from combined pool
                List<WordEntry> combined = new ArrayList<>();
                for (List<WordEntry> diffPool : poolsByDifficulty.values()) {
                    combined.addAll(diffPool);
                }
                return combined;
            });

        if (pool.isEmpty()){
            throw new IllegalStateException("No words available for difficulty: " + difficulty.orElse(null));
        }
        
        // Select random word and remove it from the pool
        int index = rng.nextInt(pool.size());
        return pool.remove(index);
    }

    @Override
    public List<WordEntry> allWords() {
        return Collections.unmodifiableList(allWords);
    }

    @Override
    public void reset(){
        // Clear all pools
        poolsByDifficulty.clear();
        
        // Rebuild pools for each difficulty
        for (Difficulty diff : Difficulty.values()) {
            List<WordEntry> pool = new ArrayList<>();
            for (WordEntry word : allWords) {
                if (word.difficulty() == diff) {
                    pool.add(word);
                }
            }
            Collections.shuffle(pool, rng);
            poolsByDifficulty.put(diff, pool);
        }
    }

    private static List<WordEntry> loadFromResources(String resourcePath){
        Objects.requireNonNull(resourcePath, "resoucePath");

        InputStream in = CsvWordProvider.class.getResourceAsStream(resourcePath);
        if (in == null){
            throw new IllegalArgumentException("CSV resource not found on classpath: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setTrim(true).build().parse(reader)){
            List<WordEntry> list = new ArrayList<>();
            for (CSVRecord rec : parser){
                Difficulty diff = Difficulty.fromCsv(rec.get("Difficulty"));
                String term = rec.get("Term");
                String clue = rec.get("Clue/Description");
                list.add(new WordEntry(diff, term, clue));
            }
            return list;
        } catch (IOException e){
            throw new RuntimeException("Failed to read CSV resource: " + resourcePath, e);
        }
    }
}