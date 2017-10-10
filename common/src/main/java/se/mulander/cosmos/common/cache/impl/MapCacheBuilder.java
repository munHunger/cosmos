package se.mulander.cosmos.common.cache.impl;

import se.mulander.cosmos.common.cache.CacheManager;

import java.util.function.Function;

/**
 * @author Marcus Münger
 */
public class MapCacheBuilder<T> {
    private long ttl = -1;
    private Function<Long, Boolean> shouldReturn;

    public static <T> MapCacheBuilder createOfType(Class<T> c) {
        return new MapCacheBuilder<T>();
    }

    private MapCacheBuilder() {
    }

    public MapCacheBuilder setTTL(long ttl) {
        this.ttl = ttl;
        return this;
    }

    public MapCacheBuilder setReturnPolicy(Function<Long, Boolean> shouldReturn) {
        this.shouldReturn = shouldReturn;
        return this;
    }

    public CacheManager<T> build() {
        MapCacheManager<T> cacheManager = new MapCacheManager<>(ttl, shouldReturn);
        return cacheManager;
    }
}
