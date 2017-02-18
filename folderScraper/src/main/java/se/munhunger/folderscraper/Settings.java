package se.munhunger.folderscraper;


import com.google.gson.Gson;
import se.munhunger.folderscraper.utils.SettingValues;
import se.munhunger.folderscraper.utils.database.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by marcu on 2017-02-16.
 */
@Path("/scraper/settings")
public class Settings
{
	private static SettingValues values = new SettingValues();

	static
	{
		try
		{
			Database.createTable(SettingValues.class);
			List<Object> settingList = Database.getObject(SettingValues.class, null);
			if(settingList.size() > 0)
				values = (SettingValues) settingList.get(0);
			else
				Database.insertObject(values);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get()
	{
		return Response.ok(new Gson().toJson(values)).build();
	}

	@PUT()
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(String json) throws Exception
	{
		values = new Gson().fromJson(json, SettingValues.class);
		Database.saveObject(values);
		return Response.noContent().build();
	}
}
