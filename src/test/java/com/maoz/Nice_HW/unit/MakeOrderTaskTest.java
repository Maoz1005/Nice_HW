package com.maoz.Nice_HW.unit;

import com.maoz.Nice_HW.dto.SuggestTaskRequestDTO;
import com.maoz.Nice_HW.tasks.MakeOrderTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MakeOrderTaskTest {

    @Test
    void getTaskName_returnsCorrectName() {
        MakeOrderTask task = new MakeOrderTask();
        assertEquals("MakeOrderTask", task.getTaskName());
    }

    @Test
    void activateTask_runsWithoutException() {
        MakeOrderTask task = new MakeOrderTask();

        SuggestTaskRequestDTO dto = new SuggestTaskRequestDTO(
                "i want to place an order",
                "user123",
                "session456",
                "2025-09-09T14:10:32+03:00"
        );

        assertDoesNotThrow(() -> task.activateTask(dto));
    }
}
