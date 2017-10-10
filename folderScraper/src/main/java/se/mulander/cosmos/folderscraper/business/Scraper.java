package se.mulander.cosmos.folderscraper.business;

import com.google.gson.Gson;
import se.mulander.cosmos.common.database.Database;
import se.mulander.cosmos.folderscraper.model.FileObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by marcu on 2017-02-16.
 */
@Path("/scraper")
public class Scraper
{
	@Path("/folderdb")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFolderDBStatus() throws Exception
	{
		return Response.ok(new Gson().toJson(new Database().getObject(FileObject.class, null))).build();
	}

	@Path("/folderstatus")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentFolderStatus() throws Exception
	{
		return Response.ok(new Gson().toJson(new se.mulander.cosmos.folderscraper.impl.Scraper().getFolderStatus())).build();
	}

	@Path("/folderstatus/{folder}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSpecificFolderStatus(@PathParam("folder") String folder) throws Exception
	{
		return Response.ok(new Gson().toJson(new se.mulander.cosmos.folderscraper.impl.Scraper().getFolderStatus(folder))).build();
	}

	@Path("/watcherstatus")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWatcherStatus() throws Exception
	{
		return Response.ok("{\"status\":" + (se.mulander.cosmos.folderscraper.impl.Scraper.isRunningWatcher() ? "\"running\"" : "\"stopped\"") + "}").build();
	}

	@Path("/startwatcher")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response startWatcher() throws Exception
	{
		se.mulander.cosmos.folderscraper.impl.Scraper.startWatcher();
		return Response.noContent().build();
	}

	@Path("/stopwatcher")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response stopWatcher() throws Exception
	{
		se.mulander.cosmos.folderscraper.impl.Scraper.stopWatcher();
		return Response.noContent().build();
	}
}
