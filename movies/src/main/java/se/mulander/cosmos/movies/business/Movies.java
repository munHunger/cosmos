package se.mulander.cosmos.movies.business;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Component;
import se.mulander.cosmos.movies.model.Movie;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by marcu on 2017-04-02.
 */
@Path("/movies")
@Component
@Api(value = "Movies", description = "Endpoints for finding new movies and managing wishlists")
public class Movies
{
	@GET
	@Path("/recomendations")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get recomendations", notes = "Gets recomendations based on new releases.")
	@ApiResponses({@ApiResponse(code = HttpServletResponse.SC_OK,
								message = "A list of movie recomendations",
								responseContainer = "Array",
								response = Movie.class)})
	public Response getRecomendations()
	{
		try
		{
			return Response.ok(se.mulander.cosmos.movies.util.Movies.getRecomendations()).build();
		}
		catch(Exception e)
		{
			return Response.serverError().build();
		}
	}
}
