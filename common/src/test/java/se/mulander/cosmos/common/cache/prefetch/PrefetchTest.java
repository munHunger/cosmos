package se.mulander.cosmos.common.cache.prefetch;

import com.mscharhag.oleaster.runner.OleasterRunner;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import se.mulander.cosmos.common.cache.prefetch.impl.PrefetchMapCacheBuilder;
import se.mulander.cosmos.common.cache.prefetch.impl.PrefetchMapCacheManager;

import java.util.HashMap;
import java.util.Map;

import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Marcus Münger
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(OleasterRunner.class)
@PrepareForTest(System.class)
public class PrefetchTest {
    private static PrefetchMapCacheManager<String> underTest;

    static {
        describe("Has a full prefetchCache built from builder", () ->
        {
            beforeEach(() ->
                       {
                           PrefetchMapCacheBuilder<String> builder = PrefetchMapCacheBuilder.createOfType(String.class);
                           underTest = (PrefetchMapCacheManager<String>) builder.setFixedPool(1).build();
                           mockStatic(System.class);
                           when(System.currentTimeMillis()).thenReturn(0l);
                       });

            describe("Has a new entry in the cache", () ->
            {
                beforeEach(() ->
                           {
                               Map<String, String> argMap = new HashMap();
                               argMap.put("input", "value");
                               underTest.put("key", 10, 5, args ->
                               {
                                   return ((Map<String, Object>) args).get("input");
                               }, argMap);
                           });
                describe("The entry has been deleted", () ->
                {
                    beforeEach(() ->
                               {
                                   underTest.delete("key");
                               });
                    it("Returns an empty optional", () ->
                    {
                        Assert.assertFalse(underTest.get("key").isPresent());
                    });
                });
                describe("The prefetch ttl has not been reached", () ->
                {
                    beforeEach(() ->
                               {
                                   when(System.currentTimeMillis()).thenReturn(1l);
                               });
                    it("Returns a non-empty optional", () ->
                    {
                        Assert.assertTrue(underTest.get("key").isPresent());
                    });
                    it("Returns an optional wrapping the value", () ->
                    {
                        Assert.assertEquals("value", underTest.get("key").get());
                    });
                });
            });
        });
    }
}
