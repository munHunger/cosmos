package se.mulander.cosmos.downloader.business;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by marcu on 2017-04-14.
 */
@Path("/downloader")
@Component
@Api(value = "Downloader")
public class Downloader
{
	@GET
	@Path("/download")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Download an item")
	public Response downloadItem(@QueryParam("title") String title) throws Exception
	{
		boolean found = new se.mulander.cosmos.downloader.util.Downloader().downloadItem(title);
		if(found)
			return Response.noContent().build();
		else
			return Response.status(404).build();
	}
}
