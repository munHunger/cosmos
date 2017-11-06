package se.mulander.cosmos.common.model.movies.movie;

import java.util.List;

/**
 * An interface for movie data access object
 */

public interface MovieDao {

    /**
     * Fetches a list with all the movie objects from the database
     *
     * @return A list with all the movie objects
     * @throws Exception if the database fails to return for some reason
     */
    List<Movie> getAllMovies() throws Exception;

    /**
     * Fetches a list with movie objects from the database, with specified status
     *
     * @param status the status to look for
     * @return A list with movie objects, with specified status
     * @throws Exception if the database fails to return for some reason
     */
    List<Movie> getMoviesByStatus(String status) throws Exception;

    /**
     * Fetches a movie object from the database, with specified id
     *
     * @param id the id to look for
     * @return A movie object, with specified id
     * @throws Exception if the database fails to return for some reason
     */
    Movie getMovieById(String id) throws Exception;

    /**
     * Fetches a movie object from the database, with specified title
     *
     * @param title the title to look for
     * @return A movie object, with specified title
     * @throws Exception if the database fails for some reason
     */
    Movie getMovieByTitle(String title) throws Exception;

    /**
     * Updates a specified movie object in the database
     *
     * @param movie the movie object to update
     */
    void updateMovie(Movie movie);

    /**
     * Deletes a specified movie object in the database
     *
     * @param movie the movie object to delete
     * @throws Exception if the database fails for some reason
     */
    void deleteMovie(Movie movie) throws Exception;

    void saveOrUpdateMovie(Movie movie);

    /**
     * Saves a specified movie object in the database
     *
     * @param movie the movie object to save
     */
    void saveMovie(Movie movie);

}