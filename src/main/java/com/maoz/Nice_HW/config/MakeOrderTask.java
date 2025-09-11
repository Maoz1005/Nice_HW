package com.maoz.Nice_HW.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MakeOrderTask extends AbstractTask{
    private static final Logger logger = LoggerFactory.getLogger(MakeOrderTask.class);
    public MakeOrderTask(){
        this.taskName = "MakeOrderTask";
    }
    @Override
    public void activateTask() {
        logger.info("MakeOrderTask activated");
    }
}
