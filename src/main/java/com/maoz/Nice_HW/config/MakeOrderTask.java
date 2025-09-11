package com.maoz.Nice_HW.config;

import com.maoz.Nice_HW.dto.SuggestTaskRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a concrete task for making a new order.
 */
public class MakeOrderTask extends AbstractTask{
    private static final Logger logger = LoggerFactory.getLogger(MakeOrderTask.class);

    /**
     * Constructor initializes the task with its name.
     */
    public MakeOrderTask(){
        this.taskName = "MakeOrderTask";
    }

    /**
     * Activates the task.
     * In this example, it simply logs that the task was activated.
     */
    @Override
    public void activateTask(SuggestTaskRequestDTO request) {
        logger.info("MakeOrderTask activated");
    }
}
