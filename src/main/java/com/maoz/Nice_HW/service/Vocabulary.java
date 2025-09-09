package com.maoz.Nice_HW.service;

import java.util.HashMap;
import java.util.Map;

public class Vocabulary {
    private Map<String, Integer> wordToIndex = new HashMap<>();

    public void addSentence(String sentence) {
        for (String token : sentence.toLowerCase().split("\\s+")) {
            wordToIndex.putIfAbsent(token, wordToIndex.size());
        }
    }

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
