package se.mulander.cosmos.common.filter.cache;

import se.mulander.cosmos.common.filter.cache.annotations.Cached;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Marcus Münger
 */
@Provider
@Priority(Priorities.USER)
public class CachePostFilter implements ContainerResponseFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Context
    HttpServletRequest httpRequest;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws
                                                                                                         IOException {
        Method method = resourceInfo.getResourceMethod();
        if (method.isAnnotationPresent(Cached.class)) {
            if (!SingletonCache.cache.get(method.getName()).isPresent())
                SingletonCache.cache.put(method.getName(), responseContext);
        }
    }
}
