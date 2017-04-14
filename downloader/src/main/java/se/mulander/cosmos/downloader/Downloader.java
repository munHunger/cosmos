package se.mulander.cosmos.downloader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by marcu on 2017-04-14.
 */
@Path("/downloader")
public class Downloader
{
	@Path("/download/{title}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response downloadItem(@PathParam("title") String title) throws Exception
	{
		boolean found = new se.mulander.cosmos.downloader.utils.business.Downloader().downloadItem(title);
		if(found)
			return Response.noContent().build();
		else
			return Response.status(404).build();
	}
}
