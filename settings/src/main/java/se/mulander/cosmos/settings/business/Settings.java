package se.mulander.cosmos.settings.business;

import io.swagger.annotations.*;
import org.springframework.stereotype.Component;
import se.mulander.cosmos.common.database.jpa.Database;
import se.mulander.cosmos.common.model.ErrorMessage;
import se.mulander.cosmos.settings.model.Setting;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by marcu on 2017-04-02.
 */
@Path("/settings")
@Component
@Api(value = "Settings", description = "Endpoints for setting and getting settings related to all different endpoints")
public class Settings
{
	private AsyncResponse asyncResponse;

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get specific setting category",
				  notes = "Get all settings that are under the specified category group. for example all downloader settings")
	@ApiResponses({@ApiResponse(code = HttpServletResponse.SC_OK,
								message = "A object full of the settings related to the category group")})
	public Response getSettingsGroup(
			@ApiParam(value = "The setting id", example = "9f970027-8a06-4145-9f6d-57f2898efc0b") @QueryParam("id")
					String id) throws
			Exception
	{
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		List setting = Database.getObjects("from Setting WHERE id = :id", param);
		if(setting.isEmpty())
			return Response.status(HttpServletResponse.SC_NOT_FOUND).entity(new ErrorMessage("Could not get group", "There is no group with that ID")).build();
		Setting s = (Setting) setting.get(0);
		if(s.type.toUpperCase().equals("GROUP"))
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(new ErrorMessage("Could not get group", "The setting with the supplied ID was not a group type")).build();
		return Response.ok(s).build();
	}

	@GET
	@Path("/structure/poll")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Long polls the structure endpoint", notes = "Replies like the /structure endpoint if some settng is updated")
	@ApiResponses({@ApiResponse(code = HttpServletResponse.SC_OK, message = "An object describing the settings tree and how to parse it")})
	public void pollStructure(@Suspended final AsyncResponse asyncResponse) throws Exception
	{
		asyncResponse.setTimeout(30, TimeUnit.SECONDS);
		this.asyncResponse = asyncResponse;
	}

	@GET
	@Path("/structure/update")
	public Response updateStructure()
	{
		if(this.asyncResponse != null)
			this.asyncResponse.resume("{\"message\":\"Hello World!\"");
		return Response.ok().build();
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
		return Response.ok(Database.getObjects("from Setting WHERE parent = null")).build();
	}

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Registers a new setting", notes = "Registers a new setting and how to parse it")
	public Response createSetting(Setting s) //TODO: create registration object
	{
		setParent(s);
		Database.saveObject(s);
		return Response.status(HttpServletResponse.SC_NOT_IMPLEMENTED).build();
	}

	private void setParent(Setting parent)
	{
		if(parent.children != null)
		{
			for(Setting child : parent.children)
			{
				child.parent = parent;
				setParent(child);
			}
		}
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Updates the settings value", notes = "Updates and sets the setting value")
	public Response updateSetting(
			@ApiParam(value = "The identifier of the setting to update") @PathParam("id") String id,
			@ApiParam(value = "The value to set on the setting") @FormParam("value") String value) throws Exception
	{
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		List dbRes = Database.getObjects("from Setting WHERE id = :id", param);
		if(dbRes.isEmpty())
			return Response.status(HttpServletResponse.SC_NOT_FOUND).entity(new ErrorMessage("Could not update setting", "No setting with that ID was found")).build();

		Setting s = (Setting) dbRes.get(0);
		String regex = s.regex;
		if(s.type.toUpperCase().equals("GROUP"))
			return Response.status(HttpServletResponse.SC_NOT_MODIFIED).entity(new ErrorMessage("Settings not updated", "Setting is a group. Groups cannot change value")).build();
		else if(s.type.toUpperCase().equals("BOOLEAN"))
			regex = "(TRUE)|(FALSE)";
		else if(s.type.toUpperCase().equals("NUMBER"))
			regex = "\\d+";
		if(!value.matches(regex))
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(new ErrorMessage("Did not update settings", "Input did not fit pattern")).build();
		s.value = value;
		Database.updateObject(s);
		return Response.status(HttpServletResponse.SC_NO_CONTENT).build();
	}
}
