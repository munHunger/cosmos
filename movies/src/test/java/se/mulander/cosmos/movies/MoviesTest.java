package se.mulander.cosmos.movies;

import com.mscharhag.oleaster.runner.OleasterRunner;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.reflect.Whitebox;
import se.mulander.cosmos.common.model.movies.Genre;
import se.mulander.cosmos.common.model.movies.GenreList;
import se.mulander.cosmos.common.model.movies.Movie;
import se.mulander.cosmos.common.model.movies.tmdb.TMDBResponse;
import se.mulander.cosmos.common.model.movies.tmdb.TMDBResponseResult;
import se.mulander.cosmos.common.settings.DatabaseSettings;
import se.mulander.cosmos.movies.impl.Movies;
import se.mulander.cosmos.movies.util.Settings;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;
import static org.mockito.Matchers.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author Marcus Münger
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(OleasterRunner.class)
@PrepareForTest({Movies.class, Settings.class, DatabaseSettings.class, ClientBuilder.class})
public class MoviesTest
{
    private Response recommendations;
    {
        describe("MovieTest", () -> {
            describe("Getting recommendations", () -> {
                beforeEach(() -> {
                    mockStatic(Settings.class);
                    mockStatic(DatabaseSettings.class);
                    when(Settings.class, "getSettingsValue", any()).thenReturn(Optional.of("api_url"));

                    mockStatic(Movies.class);
                    doNothing().when(Movies.class, "saveListInDatabase", any());
                    doCallRealMethod().when(Movies.class, "getRecommendations");
                    doCallRealMethod().when(Movies.class, "clearExtended", any());
                    doCallRealMethod().when(Movies.class, "getGenres", any(), anyString(), anyString());
                    doCallRealMethod().when(Movies.class, "getTopMovies", any(), anyString(), anyString());
                    doCallRealMethod().when(Movies.class, "tmdbToInternal", any(), any(), any(), any(), any());
                    doNothing().when(Movies.class, "addCast", any(), any(), any(), any(), any());

                    TMDBResponse tmdbResponse = new TMDBResponse();
                    tmdbResponse.results = new TMDBResponseResult[]{
                            new TMDBResponseResult("",false,"","1984",Arrays.asList(0),1,"","movie","","","",10f,10,false,10f),
                            new TMDBResponseResult("",false,"","1984",Arrays.asList(0),1,"","movie","","","",5f,10,false,5f),
                            new TMDBResponseResult("",false,"","1984",Arrays.asList(0),1,"","movie","","","",7f,10,false,7f)};
                    Response mockResponse = mockResponse();
                    when(mockResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
                    when(mockResponse.readEntity(GenreList.class)).thenReturn(new GenreList(new Genre(0, "g")));
                    when(mockResponse.readEntity(TMDBResponse.class)).thenReturn(tmdbResponse);
                    recommendations = Movies.getRecommendations();
                });
                it("saves everything in the database", () -> {
                    verifyStatic(times(1));
                    Whitebox.invokeMethod(Movies.class, "saveListInDatabase", any());
                });
                it("returns a 200 OK", () -> {
                    Assert.assertEquals(Response.Status.OK.getStatusCode(), recommendations.getStatus());
                });
                it("returns an ordered list, by the first rating ascending", () -> {
                    final MutableDouble last = new MutableDouble(Double.MAX_VALUE);
                    List<Movie> movies = (List<Movie>) recommendations.getEntity();
                    movies.forEach(movie -> {
                        if(movie.rating.get(0).rating > last.doubleValue())
                            Assert.fail("List is not ordered");
                        last.setValue(movie.rating.get(0).rating);
                    });
                });
                it("returns a list without extended object", () -> {
                    List<Movie> movies = (List<Movie>) recommendations.getEntity();
                    movies.forEach(movie -> {
                        if(movie.extendedMovie != null)
                            Assert.fail("movie had an ExtendedMovie object");
                    });
                });
            });
        });
    }

    //TODO: Where to put this so that everyone can access it?
    private static Response mockResponse() throws Exception
    {
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
        return res;
    }
}
