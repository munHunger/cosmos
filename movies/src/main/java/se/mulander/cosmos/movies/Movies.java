package se.mulander.cosmos.movies;

import com.google.gson.Gson;
import se.mulander.cosmos.folderscraper.utils.model.OMDBResponse;
import se.mulander.cosmos.movies.utils.model.TMDBResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcu on 2017-04-02.
 */
@Path("/movies")
public class Movies
{
	@GET
	@Path("/recomendations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecomendations()
	{
		try
		{
			return Response.ok(new Gson().toJson(se.mulander.cosmos.movies.utils.business.Movies.getRecomendations())).build();
		}
		catch(Exception e)
		{
			return Response.serverError().build();
		}
	}
}
