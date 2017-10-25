package se.mulander.cosmos.common.model.movies.Movie;

import java.util.ArrayList;
import java.util.List;

public interface MovieDao {

    public List<Movie> getAllMovies();
    public Movie getMovieById(String id);
    public Movie getMovieByTitle(String title);
    public void updateMovie(Movie movie);
    public void deleteMovie(Movie movie);

}
