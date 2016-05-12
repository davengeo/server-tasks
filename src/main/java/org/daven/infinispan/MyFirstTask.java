package org.daven.infinispan;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.Cache;
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

  private TaskContext taskContext;


  public String call() throws Exception {

    getCacheByName("DCUSTOMERS")
      .entrySet()
      .stream()
      .map(
        entry -> {
          log.info("dcustomers: {}:{}", entry.getKey(), entry.getValue());
          entry.setValue(entry.getValue() + "modified");
          return entry;
        })
      .forEach((cache, entry) -> {
        getOtherCache(cache, "DADDRESS")
          .put(entry.getKey(),
               entry.getValue());
      });

    final Long number = getCacheByName("DADDRESS")
      .entrySet()
      .stream()
      .map(entry -> {
        log.info("cacheName {}:{}", entry.getKey(), entry.getValue());
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

  private Cache<Object, Object> getOtherCache(Cache<Object, Object> cache, String cacheName) {
    return cache
      .getCacheManager()
      .getCache(cacheName);
  }

  private Cache<String, String> getCacheByName(String cacheName) {
    return taskContext
      .getCache()
      .orElseThrow(() -> new TechnicalException(
        String.format("Cache %snot found", cacheName)))
      .getCacheManager().getCache(cacheName, true);
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
