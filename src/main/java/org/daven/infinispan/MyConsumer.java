/*
 * Copyright (c) 2016 Proximus.
 * me@davengeo.com
 */
package org.daven.infinispan;

import org.infinispan.Cache;
import org.infinispan.stream.CacheAware;
import org.infinispan.util.function.SerializableBiConsumer;

import java.util.Map;

public class MyConsumer implements SerializableBiConsumer<Cache<String, String>, Map.Entry<String, String>>,
                                   CacheAware<String, String> {

    Cache<String, String> cache;

    @Override
    public void accept(Cache<String, String> inCache, Map.Entry<String, String> entry) {
        cache.put(entry.getKey(), entry.getValue());
    }

    @Override
    public void injectCache(Cache<String, String> cache) {
        this.cache = cache;
    }
}
