package se.mulander.cosmos.movies.business;

import io.swagger.annotations.*;
import se.mulander.cosmos.common.model.ErrorMessage;
import se.mulander.cosmos.common.model.movies.Movie;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
        try {
            Movie m = se.mulander.cosmos.movies.impl.Movies.getMovie(id);
            if (m != null)
                return Response.ok(m).build();
            return Response.status(HttpServletResponse.SC_NOT_FOUND)
                           .entity(new ErrorMessage("Not found",
                                                    "Could not find an object in the database with the ID:" + id))
                           .build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }
}
