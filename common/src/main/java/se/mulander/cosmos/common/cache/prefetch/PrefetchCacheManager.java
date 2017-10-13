package se.mulander.cosmos.common.cache.prefetch;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * A cache manager keeps track of the entire cache and all entries in it.
 * This includes configuring TTL if such functionality is available for any given implementation
 * <p>
 * The cache manager implementations can decide if they implement a in memory cache or a database cache.
 * This cache should implement a prefetch functionality that makes it so that if there is a cache miss, the old cache
 * is returned and the cache will be updated in the background
 *
 * @author Marcus Münger
 */
public interface PrefetchCacheManager<T> {
    /**
     * Puts a value into the cache.
     * The way it does this depends on implementation. It can vary in ways of how long it lives or in terms of what
     * it does with duplicate entries
     *
     * @param key           The key to associate the value with
     * @param ttl           The amount of time after which the cache entry will be thrown away.
     * @param prefetchTTL   The amount of time after which the cache will be returned, but the fetchFunction will be
     *                      executed
     * @param fetchFunction The function to call for updating the cache entry
     * @param param         The parameters used to create this cache entry
     */
    void put(String key, long ttl, long prefetchTTL, Function<Map<String, Object>, T> fetchFunction,
             Map<String, Object> param);

    /**
     * Fetches a value from the cache.
     * It searches the cache using the given key.
     * If there is an entry that is older than TTL, then this function will be blocking while it updates. if the
     * cache is not that old, but older than prefetch, it will be updated asynchronously.
     *
     * @param key the key to search with
     * @return an Optional that might contain the value.
     */
    Optional<T> get(String key);

    /**
     * Deletes an entry from the cache.
     *
     * @param key The key associated with the value to remove from the cache
     */
    void delete(String key);
}
