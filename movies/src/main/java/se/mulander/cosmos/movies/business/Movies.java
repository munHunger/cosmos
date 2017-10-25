package se.mulander.cosmos.movies.business;

import io.swagger.annotations.*;
import se.mulander.cosmos.common.filter.cache.annotations.Cached;
import se.mulander.cosmos.common.model.ErrorMessage;
import se.mulander.cosmos.common.model.movies.Movie.Movie;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by marcu on 2017-04-02.
 */
@Path("/movies")
@Api(value = "Movies", description = "Endpoints for finding new movies and managing wishlists")
public class Movies {

    @GET
    @Path("/recomendations")
    @Cached(86400000) //24Hours
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get recomendations", notes = "Gets recomendations based on new releases.")
    @ApiResponses({@ApiResponse(code = HttpServletResponse.SC_OK,
                                message = "A list of movie recomendations",
                                responseContainer = "Array",
                                response = Movie.class)})
    public Response getRecomendations() {
        return se.mulander.cosmos.movies.impl.Movies.getRecommendations();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get a full movie object", notes = "Gets the full movie object associated with the id")
    @ApiResponses({@ApiResponse(code = HttpServletResponse.SC_OK,
                                message = "The full movie object",
                                response = Movie.class), @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND,
                                                                      message = "The given ID could not be found in " +
                                                                              "the database",
                                                                      response = ErrorMessage.class)})
    public Response getMovieObject(@ApiParam(value = "The id to search for") @PathParam("id") String id) {
        return se.mulander.cosmos.movies.impl.Movies.getMovie(id);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get a list of movie objects", notes = "Gets a list with movies associated with the status")
    @ApiResponses({@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND,
            message = "The given ID could not be found in the database",
            response = ErrorMessage.class),
            @ApiResponse(code = HttpServletResponse.SC_OK,
                                message = "The list of movies",
                                responseContainer = "Array",
                                response = Movie.class)})
    public Response getMoviesWithStatus(@ApiParam(value = "The status to search for") @QueryParam("status") String status) {
        return se.mulander.cosmos.movies.impl.Movies.getMoviesWithStatus(status);
    }

    @POST
    @Path("/{id}")
    @ApiOperation(value = "Sets the status of a certain movie object")
    @ApiResponses({@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND,
            message = "The given ID could not be found in the database",
            response = ErrorMessage.class),
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "")})
    public Response setMovieObjectStatus(@ApiParam(value = "The id to search for") @PathParam("id") String id,
                                   @ApiParam(value = "The status to be set for the movie") @FormParam("status")
                                           String status) {
        return se.mulander.cosmos.movies.impl.Movies.setMovieStatus(id, status);
    }

    @GET
    @Path("/search")
    @ApiOperation(value = "Finds a movie by title", notes = "Searches in database/external library for movies matching query")
    @ApiResponses({@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND,
            message = "Could not find movie based on query",
            response = ErrorMessage.class),
            @ApiResponse(code = HttpServletResponse.SC_OK,
                    message = "The movie/movies found",
                    responseContainer = "Array",
                    response = Movie.class)})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovie(@ApiParam(value = "The query to search for") @QueryParam("query") String query) throws Exception {
        return se.mulander.cosmos.movies.impl.Movies.findMovie(query);
    }
}

