package se.mulander.cosmos.movies;

import com.mscharhag.oleaster.runner.OleasterRunner;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.reflect.Whitebox;
import se.mulander.cosmos.common.model.movies.tmdb.TMDBResponse;
import se.mulander.cosmos.movies.impl.Movies;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author Marcus Münger
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(OleasterRunner.class)
@PrepareForTest({Movies.class})
public class MoviesTest {

    {
        describe("MovieTest", () ->
        {
            describe("Getting recomendations", () ->
            {
                beforeEach(() ->
                           {
                               mockStatic(Movies.class);
                               doReturn(null).when(Movies.class, "getGenres",
                                                   any(Client.class), anyString(), anyString());
                               TMDBResponse tmdbResponse = new TMDBResponse();
                               doReturn(tmdbResponse).when(Movies.class, "getTopMovies", any(), anyString(),
                                                           anyString());
                               doNothing().when(Movies.class, "saveListInDatabase", any());
                               Movies.getRecomendations();
                           });
                it("fetches a list of genres", () ->
                {
                    verifyStatic(times(1));
                    Whitebox.invokeMethod(Movies.class, "getGenres", new Object[]{null, "", ""});
                });
            });
        });
    }

    //TODO: Where to put this so that everyone can access it?
    private static <T> void mockResponse(Class<T> type, T response) throws Exception {
        mockStatic(ClientBuilder.class);
        Client client = mock(Client.class);
        when(ClientBuilder.class, "newClient").thenReturn(client);
        WebTarget webTarget = mock(WebTarget.class);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.queryParam(any(), any())).thenReturn(webTarget);
        Invocation.Builder invocationBuilder = mock(Invocation.Builder.class);
        when(webTarget.request()).thenReturn(invocationBuilder);
        Invocation invocation = mock(Invocation.class);
        when(invocationBuilder.buildGet()).thenReturn(invocation);

        Response res = mock(Response.class);
        when(invocation.invoke()).thenReturn(res);
        when(res.readEntity(type)).thenReturn(response);
    }
}
