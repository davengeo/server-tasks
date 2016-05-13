package org.daven.infinispan;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.Cache;
import org.infinispan.commons.util.SimpleImmutableEntry;
import org.infinispan.stream.CacheCollectors;
import org.infinispan.tasks.ServerTask;
import org.infinispan.tasks.TaskContext;
import org.infinispan.tasks.TaskExecutionMode;

import java.util.function.Function;
import java.util.stream.Collectors;

//
// At least in JDG7.0.0 beta the caches to use here
// should have the compatibility flag to log the value correctly.
//
@SuppressWarnings("unchecked")
@Slf4j
public class MyFirstTask implements ServerTask<String> {

  private static final String DCUSTOMERS = "DCUSTOMERS";
  private static final String DADDRESS = "DADDRESS";
  private TaskContext taskContext;

  Function<String, Cache<String, String>> getCache = s ->
    taskContext.getCache().get().getCacheManager().getCache(s, false);

  public String call() throws Exception {

    getCache.apply(DCUSTOMERS)
      .entrySet()
      .parallelStream()
      .map(
        entry ->
          new SimpleImmutableEntry(entry.getKey(),
                                   String.format("%s-modified", entry.getValue())))
      .forEach((cache, entry) -> {
        cache
          .getCacheManager()
          .getCache(DADDRESS)
          .put(entry.getKey(),
               entry.getValue());
      });

    final Long number = getCache.apply(DADDRESS)
      .entrySet()
      .parallelStream()
      .peek(entry ->
              log.info("{}:{}:{}", DADDRESS, entry.getKey(), entry.getValue()))
      .collect(CacheCollectors.serializableCollector(Collectors::counting));

    taskContext.getCache()
      .get()
      .getCacheManager()
      .executor()
      .submit(() -> log.info("different process"));

    return "Success - " + number;
  }

  public void setTaskContext(TaskContext taskContext) {
    this.taskContext = taskContext;
  }

  public String getName() {
    return this.getClass().getSimpleName();
  }

  public TaskExecutionMode getExecutionMode() {
    return TaskExecutionMode.ALL_NODES;
  }

}
