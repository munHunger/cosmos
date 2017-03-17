package se.munhunger.folderscraper;

import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by marcu on 2017-02-16.
 */
@Path("/scraper")
public class Scraper
{
	@Path("/folderstatus")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentFolderStatus() throws Exception
	{
		return Response.ok(new Gson().toJson(new se.munhunger.folderscraper.utils.business.Scraper().getFolderStatus())).build();
	}
}
