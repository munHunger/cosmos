package se.mulander.cosmos.common.model.movies.movie;


import java.util.List;

public interface MovieDao {

    public List<Movie> getAllMovies();
    public List<Movie> getMoviesByStatus(String status);
    public Movie getMovieById(String id);
    public Movie getMovieByTitle(String title);
    public void updateMovie(Movie movie);
    public void deleteMovie(Movie movie);
    public void saveMovie(Movie movie);

}