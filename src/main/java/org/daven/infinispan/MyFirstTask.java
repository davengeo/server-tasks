package org.daven.infinispan;

import org.infinispan.tasks.ServerTask;
import org.infinispan.tasks.TaskContext;
import org.infinispan.tasks.TaskExecutionMode;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;


public class MyFirstTask implements ServerTask<String> {

    private TaskContext taskContext;

    public String call() throws Exception {
        return "Hello¿¿¿¿¿";
    }

    public void setTaskContext(TaskContext taskContext) {
        this.taskContext = taskContext;
    }

    public String getName() {
        return "MyFirstTask";
    }

    public TaskExecutionMode getExecutionMode() {
        return TaskExecutionMode.ALL_NODES;
    }

    public Optional<String> getAllowedRole() {
        return Optional.empty();
    }

    public Set<String> getParameters() {
        return taskContext
                .getParameters()
                .orElse(new HashMap<>())
                .keySet();
    }
}
