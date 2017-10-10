package se.mulander.cosmos.common.filter.cache;

import se.mulander.cosmos.common.cache.CacheManager;

import javax.ws.rs.container.ContainerResponseContext;

/**
 * @author Marcus Münger
 */
public class SingletonCache {
    public static CacheManager<ContainerResponseContext> cache;
}
