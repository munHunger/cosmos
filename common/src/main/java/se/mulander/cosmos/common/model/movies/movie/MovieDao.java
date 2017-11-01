package se.mulander.cosmos.common.model.movies.movie;

import io.swagger.annotations.ApiModel;
import java.util.List;

@ApiModel(description = "An interface for movie data access object")
public interface MovieDao {

    public List<Movie> getAllMovies() throws Exception;
    public List<Movie> getMoviesByStatus(String status) throws Exception;
    public Movie getMovieById(String id) throws Exception;
    public Movie getMovieByTitle(String title) throws Exception;
    public void updateMovie(Movie movie);
    public void deleteMovie(Movie movie) throws Exception;
    public void saveMovie(Movie movie);

}