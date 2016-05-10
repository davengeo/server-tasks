package org.daven.infinispan;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static org.infinispan.client.hotrod.impl.ConfigurationProperties.DEFAULT_HOTROD_PORT;

@Component
public class Runner implements CommandLineRunner {

    public void run(String... strings) throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host("127.0.0.1").port(DEFAULT_HOTROD_PORT);
        // Connect to the server
        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        // Obtain the remote cache
        RemoteCache<String, Integer> cache = cacheManager.getCache();
        // Run the script on the server, passing in the parameters
        Object result = cache.execute("MyFirstTask", null);
        // Print the result
        System.out.printf("Result = %s\n", result);
        // Stop the cache manager and release resources
        cacheManager.stop();
    }
}
