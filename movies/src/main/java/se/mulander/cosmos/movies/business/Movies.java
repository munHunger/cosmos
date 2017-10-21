package se.mulander.cosmos.movies.business;

import io.swagger.annotations.*;
import se.mulander.cosmos.common.filter.cache.annotations.Cached;
import se.mulander.cosmos.common.model.ErrorMessage;
import se.mulander.cosmos.common.model.movies.Movie;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by marcu on 2017-04-02.
 */
@Path("/movies")
@Api(value = "Movies", description = "Endpoints for finding new movies and managing wishlists")
public class Movies {

    @GET
    @Path("/cache")
    @Cached
    @Produces(MediaType.TEXT_PLAIN)
    public Response testCache() {
        return Response.ok(System.currentTimeMillis()).build();
    }

    @GET
    @Path("/recomendations")
    @Cached
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get recomendations", notes = "Gets recomendations based on new releases.")
    @ApiResponses({@ApiResponse(code = HttpServletResponse.SC_OK,
                                message = "A list of movie recomendations",
                                responseContainer = "Array",
                                response = Movie.class)})
    public Response getRecomendations() {
        return se.mulander.cosmos.movies.impl.Movies.getRecomendations();
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
}

