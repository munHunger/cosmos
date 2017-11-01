package se.mulander.cosmos.common.model.movies.movie;

import io.swagger.annotations.ApiModel;
import se.mulander.cosmos.common.database.jpa.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel(description = "The implementation of movie data access object")
public class MovieDaoImpl implements se.mulander.cosmos.common.model.movies.movie.MovieDao {

    @Override
    public List<Movie> getAllMovies() throws Exception {
        List<Movie> result = new ArrayList<>();
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
        try {
            result = Database.getObjects("from Movie WHERE extendedMovie.status = :status", param);
        } catch (Exception e){
            throw e;
        }

        return result;
    }

    @Override
    public Movie getMovieById(String id) throws Exception {
        List<Movie> result = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        try {
            result = Database.getObjects("from Movie WHERE internalID = :id", param);
        } catch (Exception e) {
            throw e;
        }
        return result.get(0);
    }

    @Override
    public Movie getMovieByTitle(String title) throws Exception {
        List<Movie> result = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("title", title);
        try {
            result = Database.getObjects("from Movie WHERE title = :title", param);
        } catch (Exception e) {
            throw e;
        }
        return result.get(0);
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
    public void saveMovie(Movie movie) {
        Database.saveObject(movie);
    }
}