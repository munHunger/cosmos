package se.mulander.cosmos.common.cache.prefetch.impl;

import se.mulander.cosmos.common.cache.prefetch.PrefetchCacheManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * @author Marcus Münger
 */
public class PrefetchMapCacheManager<T> implements PrefetchCacheManager {

    private Map<String, PrefetchMapCacheEntry<T>> cache = new HashMap<>();
    ExecutorService threadPool;

    protected PrefetchMapCacheManager(ExecutorService pool) {
        this.threadPool = pool;
    }

    @Override
    public void put(String key, long ttl, long prefetchTTL, Function fetchFunction, Map param) {
        cache.put(key, new PrefetchMapCacheEntry<>(fetchFunction.apply(param), ttl, prefetchTTL, param, fetchFunction));
    }

    @Override
    public Optional get(String key) {
        if (!cache.containsKey(key))
            return Optional.empty();
        PrefetchMapCacheEntry entry = cache.get(key);
        if (entry.isOlderThanTTL()) {
            entry.update(); //TODO: Don't do this when updating
            return Optional.ofNullable(entry.data);
        } else {
            if (entry.isOlderThanPrefetchTTL() && !entry.isUpdating) {
                entry.isUpdating = true;
                threadPool.submit(() -> entry.update());
            }
            return Optional.ofNullable(entry.data);
        }
    }

    @Override
    public void delete(String key) {
        cache.remove(key);
    }
}
