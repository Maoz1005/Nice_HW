package com.maoz.Nice_HW.sharedSuggestTask;

import com.maoz.Nice_HW.sharedSuggestTask.TaskDictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TaskDictionaryTest {

    private TaskDictionary dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new TaskDictionary();
        dictionary.loadDictionary();
        dictionary.getDictionary().clear();
        dictionary.getDictionary().put("ResetPasswordTask", new java.util.ArrayList<>(List.of("reset", "password")));
    }

    @Test
    void updateDictionary_validSynonym_added() {
        dictionary.updateDictionary("reset my pass", "ResetPasswordTask");
        List<String> synonyms = dictionary.getDictionary().get("ResetPasswordTask");

        assertTrue(synonyms.contains("reset my pass"));
    }

    @Test
    void updateDictionary_duplicateSynonym_notAddedTwice() {
        dictionary.updateDictionary("reset", "ResetPasswordTask");
        List<String> synonyms = dictionary.getDictionary().get("ResetPasswordTask");

        long count = synonyms.stream().filter(s -> s.equals("reset")).count();
        assertEquals(1, count);
    }

    @Test
    void updateDictionary_invalidInput_rejected() {
        dictionary.updateDictionary("", "ResetPasswordTask");
        dictionary.updateDictionary("  ", "ResetPasswordTask");
        dictionary.updateDictionary("reset", "");

        List<String> synonyms = dictionary.getDictionary().get("ResetPasswordTask");
        assertEquals(2, synonyms.size());
    }

    @Test
    void updateDictionary_taskNotExists_rejected() {
        dictionary.updateDictionary("syn", "NonExistingTask");
        Map<String, List<String>> dict = dictionary.getDictionary();

        assertFalse(dict.containsKey("NonExistingTask"));
    }
}
