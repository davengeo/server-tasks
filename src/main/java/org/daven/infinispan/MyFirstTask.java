package org.daven.infinispan;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.Cache;
import org.infinispan.stream.CacheCollectors;
import org.infinispan.tasks.ServerTask;
import org.infinispan.tasks.TaskContext;
import org.infinispan.tasks.TaskExecutionMode;
import org.infinispan.util.function.SerializableBiConsumer;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

//at least in JDG7.0.0 beta the cache should have
// the compability flag.
@SuppressWarnings("unchecked")
@Slf4j
public class MyFirstTask implements ServerTask<String> {

    private TaskContext taskContext;


    public String call() throws Exception {

        final Cache<String, String> daddress1 = getCacheByName("DADDRESS");

        getCacheByName("DCUSTOMERS")
        .entrySet()
          .stream()
          .map(entry -> {
              log.info("{}:{}", entry.getKey(), entry.getValue());
              entry.setValue(entry.getValue()+"modified");
              return entry;
          })
          .forEach((SerializableBiConsumer<Cache<Object, Object>, Map.Entry<String, String>>) (myCache, entry) -> {
              myCache.put(entry.getKey(), entry.getValue());
          });

        final Long number = getCacheByName("DCUSTOMERS")
          .entrySet()
          .stream()
          .map(entry -> {
              log.info("{}:{}", entry.getKey(), entry.getValue());
              return entry;
          })
          .collect(CacheCollectors.serializableCollector(Collectors::counting));

        taskContext.getCache().get().getAdvancedCache().getCacheManager().executor().submit(() -> {
            log.info("different process");
        });

        return "Success - " + number;
    }

    private String getValue(String key) {
        return getCacheByName("DADDRESS").get(key);
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

    public interface SerialCache<K, V> extends Cache<K, V>, Serializable {

    }
}
