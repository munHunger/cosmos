package se.mulander.cosmos.downloader.business;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "Downloader",
	 description = "Endpoint for managing all downloads. i.e. search for magnets and send them to transmission")
public class Downloader
{
	@GET
	@Path("/download")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Download an item", notes = "Searches for the item and downloads the first suitable match")
	public Response downloadItem(@ApiParam(value = "The name to search for") @QueryParam("title") String title) throws
			Exception
	{
		boolean found = new se.mulander.cosmos.downloader.util.Downloader().downloadItem(title);
		if(found)
			return Response.noContent().build();
		else
			return Response.status(404).build();
	}
}
