package com.maoz.Nice_HW.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Vocabulary represents a simple Bag-of-Words model for text processing.
 *
 * This class builds a mapping from unique tokens (words) to integer indices,
 * and provides a method to convert sentences into fixed-size numerical vectors.
 */
public class Vocabulary {
    /** Maps each unique token to its index in the feature vector */
    private final Map<String, Integer> wordToIndex = new HashMap<>();

    /**
     * Updates the vocabulary with all tokens from the given sentence.
     * New words are added and assigned incremental indices.
     *
     * @param sentence the sentence to process (tokens are split on whitespace)
     */
    public void addSentence(String sentence) {
        for (String token : sentence.toLowerCase().split("\\s+")) {
            wordToIndex.putIfAbsent(token, wordToIndex.size());
        }
    }

    /**
     * Converts a sentence into a fixed-size feature vector using the current vocabulary.
     * The vector length equals the vocabulary size. Each position counts the occurrences
     * of the corresponding token in the sentence. Tokens not in the vocabulary are ignored.
     *
     * @param sentence the sentence to vectorize
     * @return a feature vector representing the Bag-of-Words encoding of the sentence
     */
    public double[] toVector(String sentence) {
        double[] vector = new double[wordToIndex.size()];
        for (String token : sentence.toLowerCase().split("\\s+")) {
            Integer index = wordToIndex.get(token);
            if (index != null) {
                vector[index]++;
            }
        }
        return vector;
    }
}
