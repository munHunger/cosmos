package se.mulander.cosmos.settings.business;

import io.swagger.annotations.*;
import org.springframework.stereotype.Component;
import se.mulander.cosmos.common.database.jpa.Database;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by marcu on 2017-04-02.
 */
@Path("/settings")
@Component
@Api(value = "Settings", description = "Endpoints for setting and getting settings related to all different endpoints")
public class Settings
{
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get specific setting category",
				  notes = "Get all settings that are under the specified category group. for example all downloader settings")
	@ApiResponses({@ApiResponse(code = HttpServletResponse.SC_OK,
								message = "A object full of the settings related to the category group")})
	public Response getSettingsGroup(
			@ApiParam(value = "The group to search for", example = "download") @QueryParam("group") String group)
	{
		return Response.status(HttpServletResponse.SC_NOT_IMPLEMENTED).build();
	}

	@GET
	@Path("/structure")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets the settings structure",
				  notes = "Gets an object structure that identifies what all settings are and how to validate them")
	@ApiResponses({@ApiResponse(code = HttpServletResponse.SC_OK,
								message = "An object describing the settings tree and how to parse it")})
	public Response getStructure() throws Exception
	{
		return Response.ok(Database.getObjects("from Setting")).build();
	}

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Registers a new setting", notes = "Registers a new setting and how to parse it")
	public Response createSetting() //TODO: create registration object
	{
		return Response.status(HttpServletResponse.SC_NOT_IMPLEMENTED).build();
	}

	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Updates the settings value", notes = "Updates and sets the setting value")
	public Response updateSetting()
	{
		return Response.status(HttpServletResponse.SC_NOT_IMPLEMENTED).build();
	}
}
