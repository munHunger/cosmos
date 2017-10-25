package se.mulander.cosmos.common.model.movies.Movie;

import se.mulander.cosmos.common.database.jpa.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDaoImpl implements MovieDao {



    public List<Movie> getAllMovies() {
        List<Movie> result = new ArrayList<>();
        try {
            result.add(new Movie("bla bla", "ho ho ho", 54));

            //result = Database.getObjects(HÄR BEHÖVS EN VETTIG QUERY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Object movie : result) {
            System.out.println(((Movie)movie).title);
        }

        return result;
    }

    @Override
    public Movie getMovieById(String id) {
        return null;
    }

    @Override
    public Movie getMovieByTitle(String title) {
        return null;
    }

    @Override
    public void updateMovie(Movie movie) {

    }

    @Override
    public void deleteMovie(Movie movie) {

    }
}
