package se.mulander.cosmos.common.cache.impl;

import se.mulander.cosmos.common.cache.CacheManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Marcus Münger
 */
public class MapCacheManager<T> implements CacheManager {
    private Map<String, MapCacheEntry<T>> cache = new HashMap<>();

    protected long ttl;
    protected Function<Long, Boolean> returnOld;

    @Override
    public void put(String key, Object value) {
        cache.put(key, new MapCacheEntry<>((T) value));
    }

    @Override
    public Optional<T> get(String key) {
        if (!cache.containsKey(key))
            return Optional.empty();
        MapCacheEntry entry = cache.get(key);
        if (ttl > 0 && entry.isOlderThan(ttl)) {
            if (returnOld == null) {
                delete(key);
                return Optional.empty();
            }
            if (returnOld.apply(entry.getAge())) {
                return Optional.ofNullable((T) entry.data);
            }
            delete(key);
            return Optional.empty();
        }
        return Optional.ofNullable((T) entry.data);
    }

    @Override
    public void delete(String key) {
        cache.remove(key);
    }

    protected MapCacheManager(long ttl, Function<Long, Boolean> returnOld) {
        this.ttl = ttl;
        this.returnOld = returnOld;
    }
}
