package org.daven.infinispan;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.Cache;
import org.infinispan.commons.util.SimpleImmutableEntry;
import org.infinispan.stream.CacheCollectors;
import org.infinispan.tasks.ServerTask;
import org.infinispan.tasks.TaskContext;
import org.infinispan.tasks.TaskExecutionMode;

import java.util.stream.Collectors;

//at least in JDG7.0.0 beta the caches to use here
// should have the compatibility flag in order to debug.
//
@SuppressWarnings("unchecked")
@Slf4j
public class MyFirstTask implements ServerTask<String> {

  private static final String DCUSTOMERS = "DCUSTOMERS";
  private static final String DADDRESS = "DADDRESS";
  private TaskContext taskContext;


  public String call() throws Exception {

    getCacheByName(DCUSTOMERS)
      .entrySet()
      .parallelStream()
      .map(
        entry -> new SimpleImmutableEntry(entry.getKey(),
                                          String.format("%s-modified", entry.getValue())))
      .forEach((cache, entry) -> {
        cache
          .getCacheManager()
          .getCache(DADDRESS)
          .put(entry.getKey(),
               entry.getValue());
      });

    final Long number = getCacheByName(DADDRESS)
      .entrySet()
      .parallelStream()
      .map(entry -> {
        log.info("{}:{}:{}", DADDRESS, entry.getKey(), entry.getValue());
        return entry;
      })
      .collect(CacheCollectors.serializableCollector(Collectors::counting));

    taskContext.getCache()
      .get()
      .getAdvancedCache()
      .getCacheManager()
      .executor()
      .submit(() -> log.info("different process"));

    return "Success - " + number;
  }

  private Cache<String, String> getCacheByName(String cacheName) {
    return taskContext
      .getCache()
      .orElseThrow(() -> new TechnicalException(
        String.format("Cache %snot found", cacheName)))
      .getCacheManager()
      .getCache(cacheName, false);
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
