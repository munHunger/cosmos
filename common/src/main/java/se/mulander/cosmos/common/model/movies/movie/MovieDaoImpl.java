package se.mulander.cosmos.common.model.movies.movie;

import se.mulander.cosmos.common.database.jpa.Database;

import java.util.*;

/**
 * The implementation of movie data access object
 */

public class MovieDaoImpl implements se.mulander.cosmos.common.model.movies.movie.MovieDao {

    @Override
    public List<Movie> getAllMovies() throws Exception {
        List<Movie> result;
        try {
            result = Database.getObjects("from Movie");
        } catch (Exception e) {
            throw e;
        }

        return result;
    }

    @Override
    public List<Movie> getMoviesByStatus(String status) throws Exception {
        List<Movie> result;
        Map<String, Object> param = new HashMap<>();
        param.put("status", status);
        result = Database.getObjects("from Movie WHERE extendedMovie.status = :status", param);
        return result;
    }

    @Override
    public Movie getMovieById(String id) throws Exception {
        List<Movie> result;
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        try {
            result = Database.getObjects("from Movie WHERE internalID = :id", param);
        } catch (Exception e) {
            throw e;
        }
        if(!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public Movie getMovieByTitle(String title) throws Exception {
        Movie movie;
        Map<String, Object> param = new HashMap<>();
        param.put("title", title);
        try {
            movie = (Movie)Database.getObjects("from Movie WHERE title = :title", param).get(0);
            if(movie != null) {
                return movie;
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }

    @Override
    public void updateMovie(Movie movie) { Database.updateObject(movie); }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
        try {
            Database.deleteObject(movie);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void saveOrUpdateMovie(Movie movie) {

        Map<String, Object> param = new HashMap<>();
        param.put("title", movie.title);
        param.put("year", movie.year);
        try {
            List dbMovies = Database.getObjects("from Movie WHERE title = :title AND year = :year",
                    param);
            if (dbMovies.isEmpty()) saveMovie(movie);
            else {
                Movie oldMovie = (Movie) dbMovies.get(0);
                movie.setID(oldMovie.internalID);
                updateMovie(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveMovie(Movie movie) {
        Database.saveObject(movie);
    }
}