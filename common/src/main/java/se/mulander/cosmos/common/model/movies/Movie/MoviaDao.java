package se.mulander.cosmos.common.model.movies.Movie;

import java.util.List;

public interface MoviaDao {

    public List<Movie> getAllMovies();
    public Movie getMovie(String string);
    public void updateMovie(Movie movie);
    public void deleteMovie(Movie movie);

}
