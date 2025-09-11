package com.maoz.Nice_HW.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CancelOrderTask extends AbstractTask{
    private static final Logger logger = LoggerFactory.getLogger(CancelOrderTask.class);
    public CancelOrderTask(){
        this.taskName = "CancelOrderTask";
    }
    @Override
    public void activateTask() {
        logger.info("CancelOrderTask activated");
    }
}
