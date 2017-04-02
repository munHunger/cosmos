package se.mulander.folderscraper;

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
		return Response.ok(new Gson().toJson(new se.mulander.folderscraper.utils.business.Scraper().getFolderStatus())).build();
	}

	@Path("/watcherstatus")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWatcherStatus() throws Exception
	{
		return Response.ok("{'status':" + (se.mulander.folderscraper.utils.business.Scraper.isRunningWatcher() ? "\"running\"" : "\"stopped\"") + "}").build();
	}

	@Path("/startwatcher")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response startWatcher() throws Exception
	{
		se.mulander.folderscraper.utils.business.Scraper.startWatcher();
		return Response.noContent().build();
	}

	@Path("/stopwatcher")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response stopWatcher() throws Exception
	{
		se.mulander.folderscraper.utils.business.Scraper.stopWatcher();
		return Response.noContent().build();
	}
}
