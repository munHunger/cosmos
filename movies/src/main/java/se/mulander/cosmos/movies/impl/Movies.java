package se.mulander.cosmos.movies.impl;

import se.mulander.cosmos.common.database.jpa.Database;
import se.mulander.cosmos.common.model.ErrorMessage;
import se.mulander.cosmos.common.model.exception.APIException;
import se.mulander.cosmos.common.model.movies.ExtendedMovie;
import se.mulander.cosmos.common.model.movies.GenreList;
import se.mulander.cosmos.common.model.movies.Movie;
import se.mulander.cosmos.common.model.movies.Rating;
import se.mulander.cosmos.common.model.movies.tmdb.TMDBCastResponse;
import se.mulander.cosmos.common.model.movies.tmdb.TMDBResponse;
import se.mulander.cosmos.movies.util.Settings;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by marcu on 2017-03-17.
 */
public class Movies {

    /**
     * Gets a list of recomended movies.
     * The recomended movies are the most popular for the current year.
     * This function will query the movie database and save the response in cosmos database
     * Note that it will fetch and save extended objects, but this function only returns simple movie objects
     *
     * @return A response object with either a 200 OK and a list of popular movie objects or a 500 with an error message
     */
    public static Response getRecomendations() {
        String theMovieDbURL = Settings.getSettingsValue("movies.movie_db_api_uri");
        String apiKey = Settings.getSettingsValue("movies.movie_db_api_key");

        final Client client = ClientBuilder.newClient();
        try {
            GenreList genreList = getGenres(client, theMovieDbURL, apiKey);
            TMDBResponse tmdbResponse = getTopMovies(client, theMovieDbURL, apiKey);

            List<Movie> result = Arrays.stream(tmdbResponse.results)
                                       .map(tmdb -> tmdbToInternal(tmdb, client, theMovieDbURL, apiKey, genreList))
                                       .sorted((m1, m2) -> new Double(m2.rating.get(0).rating).compareTo(
                                               m1.rating.get(0).rating))
                                       .collect(Collectors.toList());
            saveListInDatabase(result);

            return Response.ok(clearExtended(result)).build();
        } catch (APIException e) {
            return Response.serverError().entity(e.toErrorMessage()).build();
        } finally {
            client.close();
        }
    }

    /**
     * Gets the most popular movies with a primary release year of today.
     *
     * @param client        The client to make HTTP requests with
     * @param theMovieDbURL The movie database URL to query
     * @param apiKey        The key to authenticate with against the movieDB
     * @return the response object of the movie database
     * @throws APIException if the movie db did not reply with a 200 OK.
     */
    private static TMDBResponse getTopMovies(Client client, String theMovieDbURL, String apiKey) throws APIException {
        Response res = client.target(theMovieDbURL)
                             .path("/3/discover/movie")
                             .queryParam("api_key", apiKey)
                             .queryParam("sort_by", "popularity.desc")
                             .queryParam("include_adult", false)
                             .queryParam("include_video", false)
                             .queryParam("page", 1)
                             .queryParam("primary_release_year", 2017)
                             .request()
                             .buildGet()
                             .invoke();
        try {
            if (res.getStatus() != HttpServletResponse.SC_OK)
                throw new APIException("Could not get recommendations",
                                       "Response from movie database was not 200 OK. code was:" + res
                                               .getStatus());
            return res.readEntity(TMDBResponse.class);
        } finally {
            res.close();
        }
    }

    /**
     * Saves each movie in the database.
     * If the movie is already in the list (noted by title and year) then it will update the existing
     *
     * @param movies a list of movies to save
     */
    private static void saveListInDatabase(List<Movie> movies) {
        movies.forEach(m ->
                       {
                           Map<String, Object> param = new HashMap<>();
                           param.put("title", m.title);
                           param.put("year", m.year);
                           try {
                               List dbMovies = Database.getObjects(
                                       "from Movie WHERE title = :title AND year = :year", param);
                               if (dbMovies.isEmpty()) Database.saveObject(m);
                               else {
                                   Movie oldMovie = (Movie) dbMovies.get(0);
                                   m.setID(oldMovie.internalID);
                                   Database.updateObject(m);
                               }
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                       });
    }

    /**
     * Sets the extended movie objects to null for all movies in the list.
     * This is usefull for when you don't want to send too much data down to the user
     *
     * @param movies A list of movies to clear the extended movie object of
     * @return The supplied list but with the extended movie objects set to null
     */
    private static List<Movie> clearExtended(List<Movie> movies) {
        return movies.stream().map(m ->
                                   {
                                       m.extendedMovie = null;
                                       return m;
                                   }).collect(Collectors.toList());
    }

    /**
     * Maps a movie database object to an internal representation of a movie.
     * This will fix poster paths, add extended movie object and cast for everyone who worked on the movie
     *
     * @param tmdb          the raw tmdb result movie to map
     * @param client        A client to use when adding more data. Note that this client will not be closed.
     * @param theMovieDbURL the base url to the movie database api
     * @param apiKey        the API key to the movie database.
     * @param genreList     A list of genres used to map the tmdb genre int to a full genre name.
     * @return a fully expanded and mapped object
     */
    private static Movie tmdbToInternal(TMDBResponse.Result tmdb, Client client, String theMovieDbURL, String apiKey,
                                        GenreList genreList) {

        String year = tmdb.release_date.trim().substring(0, 4);
        Movie m = new Movie(
                "https://image.tmdb.org/t/p/w185/" + tmdb.poster_path,
                tmdb.title,
                year.matches("\\d+") ? Integer.parseInt(
                        year) : -1).addRating(
                new Rating("The Movie Database",
                           tmdb.vote_average,
                           10,
                           tmdb.vote_count));
        tmdb.genre_ids.stream()
                      .map(id -> genreList.genres.stream()
                                                 .filter(genre -> genre.id ==
                                                         id)

                                                 .findFirst()
                                                 .get().name)
                      .forEach(name -> m.addGenre(name));
        ExtendedMovie exMovie = new ExtendedMovie(tmdb.overview,
                                                  "https://image.tmdb" + "" +
                                                          ".org/t/p/w1920" +
                                                          tmdb
                                                                  .backdrop_path);
        m.setExtended(exMovie);

        addCast(client, tmdb, exMovie,
                theMovieDbURL, apiKey);

        return m;
    }

    /**
     * Adds a cast to a given movie object.
     *
     * @param client        The client to create the movie API request with
     * @param tmdb          the tmdb response for the movie object.
     * @param exMovie       the extended movie object to further expand with cast
     * @param theMovieDbURL url to the movie database
     * @param apiKey        the api key for the movie database
     * @throws APIException if tmdb couldn't respond with a 200 OK
     */
    private static void addCast(Client client, TMDBResponse.Result tmdb,
                                ExtendedMovie exMovie,
                                String theMovieDbURL,
                                String apiKey) throws
                                               APIException

    {
        Response castResponse = client.target(theMovieDbURL)
                                      .path("/3/movie")
                                      .path("/" + tmdb.id)
                                      .path("/credits")
                                      .queryParam("api_key", apiKey)
                                      .request()
                                      .buildGet()
                                      .invoke();
        try {

            if (castResponse.getStatus() != HttpServletResponse.SC_OK)
                throw new APIException(
                        "Could not get cast for movie " + tmdb.title,
                        "Response from movie database was not 200 " +
                                "OK. code was:" + castResponse
                                .getStatus());

            TMDBCastResponse
                    castList = castResponse.readEntity(TMDBCastResponse.class);
            castList.cast
                    .forEach(exMovie::addCastMember);
            castList.crew
                    .forEach(exMovie::addCastMember);
            exMovie.cast.forEach(c ->
                                 {
                                     c.generateID();
                                     if (c.profileURL != null && !c
                                             .profileURL
                                             .toUpperCase()
                                             .equals("NULL"))
                                         c.profileURL = "https://image"
                                                 + ".tmdb.org/t/p/w185"
                                                 + c.profileURL;
                                 });
        } finally {
            castResponse.close();
        }
    }

    /**
     * Fetches a movie object from the local database
     *
     * @param id the ID to look for
     * @return a response object with status 200 and the movie if it was found.
     */
    public static Response getMovie(String id) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("id", id);
            List result = Database.getObjects("from Movie WHERE internalID = :id", param);
            if (result.isEmpty()) return Response.status(HttpServletResponse.SC_NOT_FOUND)
                                                 .entity(new ErrorMessage("Could not fetch movie",
                                                                          "The movie with ID " + id + " was not found" +
                                                                                  " in the database"))
                                                 .build();
            return Response.ok(result.get(0)).build();
        } catch (Exception e) {
            return Response.serverError()
                           .entity(new ErrorMessage("Could not fetch movie",
                                                    "An exception was thrown from the database"))
                           .build();
        }
    }

    /**
     * Fetches a list of genres from the movie database.
     *
     * @param client        A client to make requests with
     * @param theMovieDbURL The base url to the movie database
     * @param apiKey        The api key to authenticate with against the movie database
     * @return A GenreList object
     * @throws APIException If the movie database responded with something other than 200 OK
     */
    private static GenreList getGenres(Client client, String theMovieDbURL, String apiKey) throws APIException {
        Response res = client.target(theMovieDbURL)
                             .path("/3/genre/movie/list")
                             .queryParam("api_key", apiKey)
                             .request()
                             .buildGet()
                             .invoke();
        try {
            if (res.getStatus() != HttpServletResponse.SC_OK)
                throw new APIException("Could not get genre list",
                                       "Response from movie database was not 200 OK. code was:" + res.getStatus());
            return res.readEntity(GenreList.class);
        } finally {
            res.close();
        }
    }
}
