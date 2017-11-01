package se.mulander.cosmos.common.model.movies.movie;

import io.swagger.annotations.ApiModel;
import java.util.List;
import java.util.Optional;

@ApiModel(description = "An interface for movie data access object")
public interface MovieDao {

    public List<Optional> getAllMovies() throws Exception;
    public List<Optional> getMoviesByStatus(String status) throws Exception;
    public Optional getMovieById(String id) throws Exception;
    public Optional getMovieByTitle(String title) throws Exception;
    public void updateMovie(Movie movie);
    public void deleteMovie(Movie movie) throws Exception;
    public void saveMovie(Movie movie);

}