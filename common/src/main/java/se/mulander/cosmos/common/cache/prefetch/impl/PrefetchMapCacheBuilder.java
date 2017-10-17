package se.mulander.cosmos.common.cache.prefetch.impl;

import se.mulander.cosmos.common.cache.prefetch.PrefetchCacheManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Marcus Münger
 */
public class PrefetchMapCacheBuilder<T> {

    private ExecutorService threadPool;

    /**
     * Creates a builder to later produce a PrefetchMapCacheManager of the class type.
     *
     * @param c   The class of the object that the cache should work with
     * @param <T> The type of the object that the cache should work with
     * @return A PrefetchCacheBuilder that will create the cache
     */
    public static <T> PrefetchMapCacheBuilder createOfType(Class<T> c) {
        return new PrefetchMapCacheBuilder<T>();
    }

    private PrefetchMapCacheBuilder() {
    }

    /**
     * Sets the thread pool to be a fixed thread pool of size poolSize.
     * This thread pool is what will be used when updating the cache.
     *
     * @param poolSize the amount of threads to create in the pool
     * @return The builder
     */
    public PrefetchMapCacheBuilder<T> setFixedPool(int poolSize) {
        threadPool = Executors.newFixedThreadPool(poolSize);
        return this;
    }

    /**
     * Builds the PrefetchCacheManager with all settings from the builder
     *
     * @return A new PrefetchCacheManager
     */
    public PrefetchCacheManager<T> build() {
        if (threadPool == null)
            threadPool = Executors.newFixedThreadPool(2);
        PrefetchMapCacheManager<T> cacheManager = new PrefetchMapCacheManager<>(threadPool);
        return cacheManager;
    }
}
