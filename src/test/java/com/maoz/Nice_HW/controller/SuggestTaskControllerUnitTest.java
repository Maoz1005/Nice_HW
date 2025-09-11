package com.maoz.Nice_HW.controller;

import com.maoz.Nice_HW.SuggestTaskController;
import com.maoz.Nice_HW.config.Constants;
import com.maoz.Nice_HW.config.DevUsers;
import com.maoz.Nice_HW.dto.SuggestTaskRequestDTO;
import com.maoz.Nice_HW.service.SuggestTaskClassifierService;
import com.maoz.Nice_HW.service.SuggestTaskDictionaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SuggestTaskControllerUnitTest {

    private SuggestTaskDictionaryService dictionaryService;
    private SuggestTaskClassifierService classifierService;
    private DevUsers devUsers;
    private SuggestTaskController controller;

    @BeforeEach
    void setUp() {
        dictionaryService = mock(SuggestTaskDictionaryService.class);
        classifierService = mock(SuggestTaskClassifierService.class);
        devUsers = mock(DevUsers.class);

        // Dev user
        when(devUsers.getUsers()).thenReturn(List.of("11111"));

        controller = new SuggestTaskController(dictionaryService, classifierService, devUsers);
    }

    // ------------------ getTaskName tests ------------------

    @Test
    void getTaskName_dictionaryMode_returnsCorrectTask() throws Exception {
        SuggestTaskRequestDTO req = new SuggestTaskRequestDTO("check my order","userX", "sess1",  "2025-09-11T10:00:00Z");
        when(dictionaryService.suggestTask("check my order")).thenReturn("CheckOrderStatusTask");

        Method method = controller.getClass().getDeclaredMethod("getTaskName", SuggestTaskRequestDTO.class, String.class);
        method.setAccessible(true);
        String taskName = (String) method.invoke(controller, req, "suggest");

        assertEquals("CheckOrderStatusTask", taskName);
        verify(dictionaryService).suggestTask("check my order");
        verifyNoInteractions(classifierService);
    }

    @Test
    void getTaskName_classifierMode_returnsCorrectTask() throws Exception {
        SuggestTaskRequestDTO req = new SuggestTaskRequestDTO("reset my password","12345", "sess1",  "2025-09-11T10:00:00Z");
        when(classifierService.suggestTask("reset my password")).thenReturn("ResetPasswordTask");

        Method method = controller.getClass().getDeclaredMethod("getTaskName", SuggestTaskRequestDTO.class, String.class);
        method.setAccessible(true);
        String taskName = (String) method.invoke(controller, req, "classifier");

        assertEquals("ResetPasswordTask", taskName);
        verify(classifierService).suggestTask("reset my password");
        verifyNoInteractions(dictionaryService);
    }

    @Test
    void getTaskName_devUpdateDictionary_returnsNoTaskFound() throws Exception {
        SuggestTaskRequestDTO req = new SuggestTaskRequestDTO(
                "update dictionary - foo,ResetPasswordTask",
                "11111",
                "sess1",
                "2025-09-11T10:00:00Z"
        );

        Method method = controller.getClass().getDeclaredMethod("getTaskName", SuggestTaskRequestDTO.class, String.class);
        method.setAccessible(true);
        String taskName = (String) method.invoke(controller, req, "suggest");

        assertEquals(Constants.NO_TASK_FOUND, taskName);
        verify(dictionaryService).updateDictionary(argThat(arr ->
                arr.length == 2 &&
                        "foo".equals(arr[0]) &&
                        "ResetPasswordTask".equals(arr[1])
        ));
        verifyNoInteractions(classifierService);
    }

    @Test
    void getSymAndTask_validInput_parsesCorrectly() throws Exception {
        Method method = controller.getClass().getDeclaredMethod("getSymAndTask", String.class);
        method.setAccessible(true);

        String[] result = (String[]) method.invoke(controller, "update dictionary - hello,MakeOrderTask");
        assertArrayEquals(new String[]{"hello", "MakeOrderTask"}, result);
    }

    @Test
    void getSymAndTask_invalidInput_returnsArrayWithNulls() throws Exception {
        Method method = controller.getClass().getDeclaredMethod("getSymAndTask", String.class);
        method.setAccessible(true);

        String[] result = (String[]) method.invoke(controller, "invalid string");
        assertEquals(2, result.length);
        assertNull(result[0]);
        assertNull(result[1]);
    }
}
