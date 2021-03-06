package org.daven.infinispan;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.marshall.core.JBossMarshaller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static org.infinispan.client.hotrod.impl.ConfigurationProperties.DEFAULT_HOTROD_PORT;

@Component
@Slf4j
public class Runner implements CommandLineRunner {

    @Value("${cache.name}")
    private String cacheName;
    @Value("${cache.secondary}")
    private String cacheSecondary;

    public void run(String... strings) throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.marshaller(new JBossMarshaller());
        builder.addServer().host("127.0.0.1").port(DEFAULT_HOTROD_PORT);
        // Connect to the server
        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        RemoteCache<String, String> cache = cacheManager.getCache(cacheName);
        RemoteCache<String, String> cache2 = cacheManager.getCache(cacheSecondary);
        // fixture
        fixtureCache(cache);
        cache2.clear();
        // Run the script on the server, passing in the parameters
        Object result = cache.execute("MyFirstTask", Collections.emptyMap());
        log.info("Result = {}", result);
        log.info(cache2.get("1"));
        log.info(cache2.get("2"));
        cacheManager.stop();
    }

    private void fixtureCache(RemoteCache<String, String> cache) {
        cache.clear();
        // Introduce some values
        cache.put("1", "one");
        cache.put("2", "two");
        //
        log.info(cache.get("1"));
        log.info(cache.get("2"));
    }
}
