package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.service.model.User;

import java.util.concurrent.TimeUnit;

public class CacheUserStore {
    public Cache<String, User> cache;

    public CacheUserStore(int expiry, TimeUnit timeUnit) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public User get(String key) {
        return cache.getIfPresent(key);
    }

    public void evict(String key) {
        if (key != null) {
            cache.invalidate(key);
        }
    }

    public void add(String key, User value) {
        if (key != null) {
            cache.put(key, value);
        }
    }
}
