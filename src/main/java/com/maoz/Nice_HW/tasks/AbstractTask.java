package com.maoz.Nice_HW.tasks;

import com.maoz.Nice_HW.dto.SuggestTaskRequestDTO;

/**
 * Abstract class for tasks.
 */
public abstract class AbstractTask {
    String taskName;
    public abstract void activateTask(SuggestTaskRequestDTO request);
    public String getTaskName() { return this.taskName; }
}
