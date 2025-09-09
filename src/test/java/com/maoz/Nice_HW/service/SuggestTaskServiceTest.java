package com.maoz.Nice_HW.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SuggestTaskServiceTest {
    private SuggestTaskService service;

    @BeforeEach
    void setup() {
        service = new SuggestTaskService();
    }

    @Test
    void testSuggestTask_found() {
        assertEquals("ResetPasswordTask", service.suggestTask("reset password"));
        assertEquals("CheckOrderStatusTask", service.suggestTask("check my order"));
        assertEquals("CancelOrderTask", service.suggestTask("cancel my order"));
        assertEquals("MakeOrderTask", service.suggestTask("I want to place an order"));
    }

    @Test
    void testSuggestTask_notFound() {
        assertEquals("NoTaskFound", service.suggestTask("unknown command"));
        assertEquals("NoTaskFound", service.suggestTask(""));
        assertEquals("NoTaskFound", service.suggestTask(null));
    }

    @Test
    void testUpdateTaskDictionary() {
        service.updateTaskDictionary("new task", "CustomTask");
        assertEquals("CustomTask", service.suggestTask("new task"));

        // עדכון מפתח קיים
        service.updateTaskDictionary("reset password", "CustomResetTask");
        assertEquals("CustomResetTask", service.suggestTask("reset password"));
    }

    @Test
    void testUpdateTaskDictionary_invalidInputs() {
        service.updateTaskDictionary("", "Task"); // לא מוסיף
        service.updateTaskDictionary("key", ""); // לא מוסיף
        service.updateTaskDictionary(null, "Task"); // לא מוסיף
        service.updateTaskDictionary("key", null); // לא מוסיף

        // הערכים המקוריים נשארים
        assertEquals("ResetPasswordTask", service.suggestTask("reset password"));
    }
}
