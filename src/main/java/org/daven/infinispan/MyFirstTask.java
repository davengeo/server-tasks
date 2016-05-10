package org.daven.infinispan;

import org.infinispan.Cache;
import org.infinispan.commons.marshall.Marshaller;
import org.infinispan.tasks.ServerTask;
import org.infinispan.tasks.TaskContext;
import org.infinispan.tasks.TaskExecutionMode;


@SuppressWarnings("unchecked")
public class MyFirstTask implements ServerTask<String> {

    private TaskContext taskContext;

    public String call() throws Exception {

        final Marshaller marshaller = taskContext.getMarshaller().get();

        getCacheByName("CUSTOMER")
          .entrySet()
          .stream()
          .forEach(entry -> {
              System.out.println(entry.getKey());
              System.out.println(entry.getValue());
          });

        return "Success";
    }

    private Cache<String, String> getCacheByName(String cacheName) {
        return taskContext.getCache().get().getCacheManager().getCache(cacheName, true);
    }

    public void setTaskContext(TaskContext taskContext) {
        this.taskContext = taskContext;
    }

    public String getName() {
        return "MyFirstTask";
    }

    public TaskExecutionMode getExecutionMode() {
        return TaskExecutionMode.ONE_NODE;
    }
}
