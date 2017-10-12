package se.mulander.cosmos.common.cache.simple.impl;

/**
 * @author Marcus Münger
 */
public class MapCacheEntry<T> {
    private long timeCreated;
    public T data;

    public MapCacheEntry(T data) {
        this.data = data;
        this.timeCreated = System.currentTimeMillis();
    }

    public boolean isOlderThan(long ttl) {
        return ttl < System.currentTimeMillis() - timeCreated;
    }

    public long getAge() {
        return System.currentTimeMillis() - timeCreated;
    }
}
