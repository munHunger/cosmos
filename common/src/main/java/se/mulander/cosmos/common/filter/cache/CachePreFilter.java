package se.mulander.cosmos.common.filter.cache;

import se.mulander.cosmos.common.cache.simple.impl.MapCacheBuilder;
import se.mulander.cosmos.common.filter.cache.annotations.Cached;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author Marcus Münger
 */
@Provider
@Priority(Priorities.USER)
public class CachePreFilter implements ContainerRequestFilter {


    @Context
    private ResourceInfo resourceInfo;

    @Context
    HttpServletRequest httpRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        if (method.isAnnotationPresent(Cached.class)) {
            if (SingletonCache.cache == null) {
                SingletonCache.cache = MapCacheBuilder.createOfType(ContainerResponseContext.class)
                                                      .setTTL(10000)
                                                      .build();
            }
            Optional<ContainerResponseContext> cacheData = SingletonCache.cache.get(method.getName());
            if (cacheData.isPresent()) {
                ContainerResponseContext context = cacheData.get();
                Response.ResponseBuilder builder = Response.status(context.getStatus())
                                                           .entity(context.getEntity());

                MultivaluedMap<String, Object> headers = context.getHeaders();
                for (String key : headers.keySet())
                    for (Object value : headers.get(key))
                        builder.header(key, value);

                requestContext.abortWith(builder.build());
            }
        }
    }
}
