package it.redhat.demo.task;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.infinispan.tasks.ServerTask;
import org.infinispan.tasks.TaskContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Massimo Ercoli
 */
public class MyRemoteTask implements ServerTask<String> {

    private static final Logger log = LoggerFactory.getLogger( MyRemoteTask.class );
    public static final String TASK_NAME = "myRemoteTask";

    @Override
    public void setTaskContext(TaskContext taskContext) {

    }

    @Override
    public String getName() {

        return TASK_NAME;

    }

    @Override
    public String call() throws Exception {

        log.info("ciao from {}", TASK_NAME);
        return "ciao";

    }

}
