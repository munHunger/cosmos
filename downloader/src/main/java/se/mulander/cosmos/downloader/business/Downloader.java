package se.mulander.cosmos.downloader.business;

import io.swagger.annotations.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
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
	@POST
	@Path("/download")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Download an item", notes = "Searches for the item and downloads the first suitable match")
	public Response downloadItem(@ApiParam(value = "The name to search for") @FormParam("title") String title) throws
			Exception
	{
		boolean found = new se.mulander.cosmos.downloader.util.Downloader().downloadItem(title);
		if(found)
			return Response.noContent().build();
		else
			return Response.status(HttpServletResponse.SC_NOT_FOUND).build();
	}

	@GET
	@Path("/status")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get download status of an item",
				  notes = "Searches for the item and checks the status of it.")
	@ApiResponses({@ApiResponse(code = HttpServletResponse.SC_OK,
								message = "Item was found"), @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND,
																		  message = "Could not find the item")})
	public Response getStatus(@ApiParam(value = "Id of the created torrent") @QueryParam("id") String id)
	{
		return Response.status(HttpServletResponse.SC_NOT_IMPLEMENTED).build();
	}
}
