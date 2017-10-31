package se.mulander.cosmos.common.model.movies.movie;

import se.mulander.cosmos.common.database.jpa.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDaoImpl implements se.mulander.cosmos.common.model.movies.movie.MovieDao {

    @Override
    public List<Movie> getAllMovies() {
        List<Movie> result = new ArrayList<>();
        try {
            result = Database.getObjects("from Movie");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Movie> getMoviesByStatus(String status) {
        List<Movie> result = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("status", status);
        try {
            result = Database.getObjects("from Movie WHERE extendedMovie.status = :status", param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Movie getMovieById(String id) {
        List<Movie> result = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        try {
            result = Database.getObjects("from Movie WHERE internalID = :id", param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.get(0);
    }

    @Override
    public Movie getMovieByTitle(String title) {
        List<Movie> result = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("title", title);
        try {
            result = Database.getObjects("from Movie WHERE title = :title", param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.get(0);
    }

    @Override
    public void updateMovie(Movie movie) {
        Database.updateObject(movie);
    }

    @Override
    public void deleteMovie(Movie movie) {
        try {
            Database.deleteObject(movie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveMovie(Movie movie) {
        Database.saveObject(movie);
    }
}