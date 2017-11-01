package se.mulander.cosmos.movies.impl;

import se.mulander.cosmos.common.database.jpa.Database;
import se.mulander.cosmos.common.model.ErrorMessage;
import se.mulander.cosmos.common.model.exception.APIException;
import se.mulander.cosmos.common.model.movies.*;
import se.mulander.cosmos.common.model.movies.movie.Movie;
import se.mulander.cosmos.common.model.movies.movie.MovieDao;
import se.mulander.cosmos.common.model.movies.movie.MovieDaoImpl;
import se.mulander.cosmos.common.model.movies.tmdb.TMDBCastResponse;
import se.mulander.cosmos.common.model.movies.tmdb.TMDBResponse;
import se.mulander.cosmos.common.model.movies.tmdb.TMDBResponseResult;
import se.mulander.cosmos.movies.util.Settings;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by marcu on 2017-03-17.
 */
public class Movies {

    /**
     * Gets a list of recommended movies.
     * The recommended movies are the most popular for the current year.
     * This function will query the movie database and save the response in cosmos database
     * Note that it will fetch and save extended objects, but this function only returns simple movie objects
     *
     * @return A response object with either a 200 OK and a list of popular movie objects or a 500 with an error message
     */
    public static Response getRecommendations() {
        Optional<String> theMovieDbURL = Settings.getSettingsValue("movies.movie_db_api_uri");
        Optional<String> apiKey = Settings.getSettingsValue("movies.movie_db_api_key");

        if (!theMovieDbURL.isPresent()) return Response.serverError()
                                                       .entity(new ErrorMessage("Could not get recommendations",
                                                                                "Couldn't get the settings for where " +
                                                                                        "to find themoviedb"))
                                                       .build();
        if (!apiKey.isPresent()) return Response.serverError()
                                                .entity(new ErrorMessage("Could not get recommendations",
                                                                         "Couldn't get the settings for the API key"))
                                                .build();
        final Client client = ClientBuilder.newClient();
        try {
            GenreList genreList = getGenres(client, theMovieDbURL.get(), apiKey.get());
            TMDBResponse tmdbResponse = getTopMovies(client, theMovieDbURL.get(), apiKey.get());

            List<Movie> result = Arrays.stream(tmdbResponse.results)
                    .map(tmdb -> tmdbToInternal(tmdb,
                            client,
                            theMovieDbURL.get(),
                            apiKey.get(),
                            genreList))
                    .sorted((m1, m2) -> new Double(m2.rating.get(0).rating).compareTo(
                            m1.rating.get(0).rating))
                    .collect(Collectors.toList());
            saveListInDatabase(result);

            return Response.ok(clearExtended(result)).build();
        } catch (APIException e) {
            return Response.serverError().entity(e.toErrorMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(new ErrorMessage("Nope", "Something went wrong")).build();
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
                             .queryParam("primary_release_year", Calendar.getInstance().get(Calendar.YEAR))
                             .request()
                             .buildGet()
                             .invoke();
        try {
            if (res.getStatus() != HttpServletResponse.SC_OK) throw new APIException("Could not get recommendations",
                                                                                     "Response from movie database " +
                                                                                             "was not 200 OK. code " +
                                                                                             "was:" + res
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
                               List dbMovies = Database.getObjects("from Movie WHERE title = :title AND year = :year",
                                                                   param);
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
     * This is useful for when you don't want to send too much data down to the user
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
    private static Movie tmdbToInternal(TMDBResponseResult tmdb, Client client, String theMovieDbURL, String apiKey,
                                        GenreList genreList) {

        String year = tmdb.releaseDate.trim().substring(0, 4);
        Movie m = new Movie("https://image.tmdb.org/t/p/w185/" + tmdb.posterPath,
                            tmdb.title,
                            year.matches("\\d+") ? Integer.parseInt(year) : -1).addRating(new Rating(
                "The Movie Database",
                tmdb.voteAverage,
                10,
                tmdb.voteCount));
        tmdb.genreIds.stream().map(id -> genreList.genres.stream().filter(genre -> genre.id == id)

                                                         .findFirst().get().name).forEach(name -> m.addGenre(name));
        ExtendedMovie exMovie = new ExtendedMovie(tmdb.overview,
                                                    "https://image.tmdb" + "" + ".org/t/p/w1920" + tmdb.backdropPath,
                                                    Status.DEFAULT,
                                                    tmdb.id);
        m.setExtended(exMovie);
        return m;
    }

    /**
     * Adds a cast to a given movie object.
     *
     * @param client        The client to create the movie API request with
     * @param movie         the movie object to further expand with cast
     * @param theMovieDbURL url to the movie database
     * @param apiKey        the api key for the movie database
     * @throws APIException if tmdb couldn't respond with a 200 OK
     */
    private static void addCast(Client client, Movie movie, String theMovieDbURL,
                                String apiKey) throws APIException

    {
        ExtendedMovie exMovie = movie.extendedMovie;
        Response castResponse = client.target(theMovieDbURL)
                                      .path("/3/movie")
                                      .path("/" + exMovie.tmdbID)
                                      .path("/credits")
                                      .queryParam("api_key", apiKey)
                                      .request()
                                      .buildGet()
                                      .invoke();
        try {

            if (castResponse.getStatus() != HttpServletResponse.SC_OK) throw new APIException(
                    "Could not get cast for movie",
                    "Response from movie database was not 200 " + "OK. code was:" + castResponse.getStatus());

            TMDBCastResponse castList = castResponse.readEntity(TMDBCastResponse.class);
            castList.cast.forEach(exMovie::addCastMember);
            castList.crew.forEach(exMovie::addCastMember);
            exMovie.cast.forEach(c ->
                                 {
                                     c.generateID();
                                     if (c.profileURL != null && !c.profileURL.toUpperCase().equals("NULL"))
                                         c.profileURL = "https://image" + ".tmdb.org/t/p/w185" + c.profileURL;
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
                                                                          "The movie with ID " + id + " was not " +
                                                                                  "found" + " in the database"))
                                                 .build();
            Movie movie = (Movie)result.get(0);
            final Client client = ClientBuilder.newClient();
            Optional<String> theMovieDbURL = Settings.getSettingsValue("movies.movie_db_api_uri");
            if (!theMovieDbURL.isPresent()) return Response.serverError()
                    .entity(new ErrorMessage("Could not get movie",
                            "Couldn't get the settings for where to find themoviedb"))
                    .build();
            Optional<String> apiKey = Settings.getSettingsValue("movies.movie_db_api_key");
            if (!apiKey.isPresent()) return Response.serverError()
                    .entity(new ErrorMessage("Could not get movie",
                            "Couldn't get the settings for the API key"))
                    .build();
            addCast(client, movie, theMovieDbURL.get(), apiKey.get());

            return Response.ok(movie).build();
        } catch (Exception e)
        {
            return Response.serverError()
                           .entity(new ErrorMessage("Could not fetch movie",
                                                    "An exception was thrown from the database"))
                           .build();
        }
    }

    /**
     * Updates the status of a movie object in the local database
     *
     * @param id the ID of the movie
     * @param status the status to be set
     * @return a response object with status 200 if movie found and status set ok.
     */
    public static Response setMovieStatus(String id, String status)
    {
        Response response = getMovie(id);
        if(response.getStatus() != HttpServletResponse.SC_OK) {
            return response;
        }
        Movie movie = (Movie)response.getEntity();
        movie.extendedMovie.status = status;
        Database.updateObject(movie);
        return Response.ok().build();
    }

    /**
     * Fetches all movie objects with certain status
     * @param status the status to filter movies by
     * @return a response object with status 200 and a list of movies with certain status
     */
    public static Response getMoviesWithStatus(String status)
    {
        try {
            List<Optional> result = new MovieDaoImpl().getMoviesByStatus(status);
            if (result.isEmpty()) return Response.status(HttpServletResponse.SC_NOT_FOUND)
                    .entity(new ErrorMessage("Could not fetch movies",
                            "No movies with the status " + status +  " was found in the database"))
                    .build();
            return Response.ok(result).build();
        } catch (Exception e)
        {
            return Response.serverError()
                    .entity(new ErrorMessage("could not fetch movies",
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
        if (theMovieDbURL == null)
            throw new APIException("Could not get genre list", "movieDBurl is null");
        if (apiKey == null)
            throw new APIException("Could not get genre list", "api key is null");
        if (client == null)
            throw new APIException("Could not get genre list", "Client is null");
        Response res = client.target(theMovieDbURL)
                             .path("/3/genre/movie/list")
                             .queryParam("api_key", apiKey)
                             .request()
                             .buildGet()
                             .invoke();
        
        try {
            if (res.getStatus() != HttpServletResponse.SC_OK) throw new APIException("Could not get genre list",
                                                                                     "Response from movie database " +
                                                                                             "was not 200 OK. code " +
                                                                                             "was:" + res
                                                                                             .getStatus());
            return res.readEntity(GenreList.class);
        } finally {
            res.close();
        }
    }
    /**
     * Fetches a list containing all movies in the local database
     *
     * @return a response object with status 200 and a list with all movie objects from local db
     */
    public static Response getAllMoviesInDatabase() {
        MovieDao movies = new MovieDaoImpl();
        try {
            return Response.ok(movies.getAllMovies()).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ErrorMessage("Could not get movies",
                            "Couldn't get the movies from the database"))
                    .build();
        }
    }

    /**
     * Fetches a movie/list of movies from external library if not found in local database
     *
     * @param query the query filtering results
     * @return a response object with status 200 and the movie/movies if it was found.
     */
    public static Response findMovie(String query) throws Exception {
        List<Movie> result = new ArrayList<>();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("title", query);
            result = Database.getObjects("from Movie WHERE title = title", param);
        } catch (Exception e) {}
        if (result.isEmpty()) {
            Optional<String> theMovieDbURL = Settings.getSettingsValue("movies.movie_db_api_uri");
            Optional<String> apiKey = Settings.getSettingsValue("movies.movie_db_api_key");
            if (!theMovieDbURL.isPresent()) return Response.serverError()
                    .entity(new ErrorMessage("Could not get recommendations",
                            "Couldn't get the settings for where " +
                                    "to find themoviedb"))
                    .build();
            if (!apiKey.isPresent()) return Response.serverError()
                    .entity(new ErrorMessage("Could not get recommendations",
                            "Couldn't get the settings for the API key"))
                    .build();
            Client client = ClientBuilder.newClient();
            Response res = client.target(theMovieDbURL.get())
                    .path("/3/search/movie")
                    .queryParam("query", query)
                    .queryParam("api_key", apiKey.get())
                    .queryParam("include_adult", false)
                    .queryParam("page", 1)
                    .queryParam("primary_release_year", Calendar.getInstance().get(Calendar.YEAR))
                    .request()
                    .buildGet()
                    .invoke();
            try {
                if (res.getStatus() != HttpServletResponse.SC_OK) return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                        .entity(new ErrorMessage("Nope", "Something went wrong"))
                        .build();
                TMDBResponse tmdbResponse = res.readEntity(TMDBResponse.class);
                GenreList genreList = getGenres(client, theMovieDbURL.get(), apiKey.get());
                List<Movie> resulting = Arrays.stream(tmdbResponse.results)
                        .map(tmdb -> tmdbToInternal(tmdb,
                                client,
                                theMovieDbURL.get(),
                                apiKey.get(),
                                genreList))
                        .collect(Collectors.toList());
                if (resulting.isEmpty()) return Response.status(HttpServletResponse.SC_NOT_FOUND)
                        .entity(new ErrorMessage("Could not fetch movie",
                                "The movie was not " +
                                        "found in the database or in external library"))
                        .build();
                clearExtended(resulting);
                return Response.ok(resulting).build();
            } finally {
                res.close();
            }
        }
        clearExtended(result);
        return Response.ok(result).build();
    }
}
