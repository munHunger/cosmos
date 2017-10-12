package se.mulander.cosmos.common.cache.simple;

import java.util.Optional;

/**
 * A cache manager keeps track of the entire cache and all entries in it.
 * This includes configuring TTL if such functionality is available for any given implementation
 * <p>
 * The cache manager implementations can decide if they implement a in memory cache or a database cache.
 *
 * @author Marcus Münger
 */
public interface CacheManager<T> {
    /**
     * Puts a value into the cache.
     * The way it does this depends on implementation. It can vary in ways of how long it lives or in terms of what
     * it does with duplicate entries
     *
     * @param key   The key to associate the value with
     * @param value The value to put in the cache.
     */
    void put(String key, T value);

    /**
     * Fetches a value from the cache.
     * It searches the cache using the given key.
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
