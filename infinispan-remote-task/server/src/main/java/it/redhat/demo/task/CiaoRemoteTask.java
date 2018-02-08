package it.redhat.demo.task;

import org.infinispan.tasks.ServerTask;
import org.infinispan.tasks.TaskContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Massimo Ercoli
 */
public class CiaoRemoteTask implements ServerTask<String> {

    private static final Logger log = LoggerFactory.getLogger( CiaoRemoteTask.class );

    @Override
    public void setTaskContext(TaskContext taskContext) {

    }

    @Override
    public String getName() {

        return CiaoRemoteTask.class.getSimpleName();

    }

    @Override
    public String call() throws Exception {

        log.info("ciao from CiaoRemoteTask");
        return "ciao";

    }

}
