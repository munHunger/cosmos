package se.mulander.cosmos.common.cache.prefetch.impl;

import java.util.Map;
import java.util.function.Function;

/**
 * @author Marcus Münger
 */
public class PrefetchMapCacheEntry<T> {
    private long timeCreated;
    private long ttl;
    private long prefetchTTL;
    public T data;
    public boolean isUpdating = false; //TODO: change to mutex lock?

    private Map<String, Object> parameters;
    private Function<Map<String, Object>, T> updateFunction;


    public PrefetchMapCacheEntry(T data, long ttl, long prefetchTTL, Map<String, Object> parameters,
                                 Function<Map<String, Object>, T> updateFunction) {
        this.data = data;
        this.timeCreated = System.currentTimeMillis();
        this.ttl = ttl;
        this.prefetchTTL = prefetchTTL;
        this.parameters = parameters;
        this.updateFunction = updateFunction;
    }

    public boolean isOlderThanPrefetchTTL() {
        return System.currentTimeMillis() - timeCreated > prefetchTTL;
    }

    public boolean isOlderThanTTL() {
        return System.currentTimeMillis() - timeCreated > ttl;
    }

    public T update() {
        data = updateFunction.apply(parameters);
        timeCreated = System.currentTimeMillis();
        isUpdating = false;
        return data;
    }
}
