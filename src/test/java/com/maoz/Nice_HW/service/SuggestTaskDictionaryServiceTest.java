package com.maoz.Nice_HW.service;

import com.maoz.Nice_HW.config.Constants;
import com.maoz.Nice_HW.sharedSuggestTask.TaskDictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SuggestTaskDictionaryServiceTest {

    private TaskDictionary taskDictionary;
    private SuggestTaskDictionaryService dictionaryService;

    @BeforeEach
    void setUp() {
        taskDictionary = new TaskDictionary() {
            @Override
            public void loadDictionary() {
                // ניגש למפה דרך getter ומכניס ערכים mutable
                getDictionary().put("ResetPasswordTask", new ArrayList<>(List.of("reset password")));
                getDictionary().put("CheckOrderStatusTask", new ArrayList<>(List.of("check order")));
                getDictionary().put("MakeOrderTask", new ArrayList<>(List.of("make order")));
                getDictionary().put("CancelOrderTask", new ArrayList<>(List.of("cancel order")));
            }
        };

        taskDictionary.loadDictionary();
        dictionaryService = new SuggestTaskDictionaryService(taskDictionary);
    }

    @Test
    void suggestTask_knownUtterances_returnsCorrectTask() {
        assertEquals("ResetPasswordTask", dictionaryService.suggestTask("please reset password"));
        assertEquals("MakeOrderTask", dictionaryService.suggestTask("make order please"));
        assertEquals("CheckOrderStatusTask", dictionaryService.suggestTask("check order now"));
        assertEquals("CancelOrderTask", dictionaryService.suggestTask("cancel order please"));
    }

    @Test
    void suggestTask_unknownUtterance_returnsNoTaskFound() {
        assertEquals(Constants.NO_TASK_FOUND, dictionaryService.suggestTask("do something else"));
    }

    @Test
    void suggestTask_emptyOrNull_returnsNoTaskFound() {
        assertEquals(Constants.NO_TASK_FOUND, dictionaryService.suggestTask(""));
        assertEquals(Constants.NO_TASK_FOUND, dictionaryService.suggestTask("   "));
        assertEquals(Constants.NO_TASK_FOUND, dictionaryService.suggestTask(null));
    }

    @Test
    void updateDictionary_addsNewSynonym() {
        String[] symAndTask = {"reset my password", "ResetPasswordTask"};
        dictionaryService.updateDictionary(symAndTask);

        List<String> synonyms = taskDictionary.getDictionary().get("ResetPasswordTask");
        assertTrue(synonyms.contains("reset my password"));
    }

    @Test
    void updateDictionary_ignoresNonExistingTask() {
        String[] symAndTask = {"new synonym", "NonExistingTask"};
        dictionaryService.updateDictionary(symAndTask);

        assertFalse(taskDictionary.getDictionary().containsKey("NonExistingTask"));
    }

    @Test
    void updateDictionary_ignoresInvalidEntries() {
        // synonym null
        dictionaryService.updateDictionary(new String[]{null, "ResetPasswordTask"});
        // synonym empty
        dictionaryService.updateDictionary(new String[]{"", "ResetPasswordTask"});
        // task null
        dictionaryService.updateDictionary(new String[]{"reset something", null});
        // task empty
        dictionaryService.updateDictionary(new String[]{"reset something", ""});

        List<String> synonyms = taskDictionary.getDictionary().get("ResetPasswordTask");
        assertFalse(synonyms.contains(null));
        assertFalse(synonyms.contains(""));
    }
}
