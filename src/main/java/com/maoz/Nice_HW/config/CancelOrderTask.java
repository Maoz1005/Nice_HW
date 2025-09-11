package com.maoz.Nice_HW.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a concrete task for cancelling an order.
 */
public class CancelOrderTask extends AbstractTask{
    private static final Logger logger = LoggerFactory.getLogger(CancelOrderTask.class);

    /**
     * Constructor initializes the task with its name.
     */
    public CancelOrderTask(){
        this.taskName = "CancelOrderTask";
    }

    /**
     * Activates the task.
     * In this example, it simply logs that the task was activated.
     */
    @Override
    public void activateTask() {
        logger.info("CancelOrderTask activated");
    }
}
