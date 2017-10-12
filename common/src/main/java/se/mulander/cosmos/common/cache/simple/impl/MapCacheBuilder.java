package se.mulander.cosmos.common.cache.simple.impl;

import se.mulander.cosmos.common.cache.simple.CacheManager;

import java.util.function.Function;

/**
 * @author Marcus Münger
 */
public class MapCacheBuilder<T> {
    private long ttl = -1;
    private Function<Long, Boolean> shouldReturn;

    /**
     * Creates a builder to later produce a MapCacheManager of the class type.
     *
     * @param c   The class of the object that the cache should work with
     * @param <T> The type of the object that the cache should work with
     * @return A CacheBuilder that will create teh cache
     */
    public static <T> MapCacheBuilder createOfType(Class<T> c) {
        return new MapCacheBuilder<T>();
    }

    private MapCacheBuilder() {
    }

    /**
     * Sets a time to live for all entries in the cache.
     * The cache will not clean itself while running, instead it throws away elements when it retrieves an old one
     *
     * @param ttl the time to live for a cache element in milliseconds
     * @return The CacheBuilder
     */
    public MapCacheBuilder setTTL(long ttl) {
        this.ttl = ttl;
        return this;
    }

    /**
     * Sets a return policy. This policy is what determines what to do when an old element has been retrieved.
     * The function takes in the age of the element in milliseconds and produces a boolean, which if true, tells the
     * CacheManager to return the cache, otherwise it returns null.
     * This can be useful to see if the cache is so old that it is of zero use, or if it is just slightly old and a
     * new cache update can be executed.
     * If it returns false, then null will be returned by the cache and the entry will be deleted. As long as it
     * returns true, the old element will stay
     *
     * @param shouldReturn A function determining what to do with an old cache
     * @return The CacheBuilder
     */
    public MapCacheBuilder setReturnPolicy(Function<Long, Boolean> shouldReturn) {
        this.shouldReturn = shouldReturn;
        return this;
    }

    /**
     * Builds the CacheManager with all settings from the builder
     *
     * @return A new CacheManager
     */
    public CacheManager<T> build() {
        MapCacheManager<T> cacheManager = new MapCacheManager<>(ttl, shouldReturn);
        return cacheManager;
    }
}
