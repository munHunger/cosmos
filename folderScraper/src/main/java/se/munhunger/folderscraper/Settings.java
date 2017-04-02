package se.munhunger.folderscraper;


import com.google.gson.Gson;
import se.munhunger.folderscraper.utils.database.Database;
import se.munhunger.folderscraper.utils.model.SettingValues;

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
	public static SettingValues values = new SettingValues();
	private static Database db = new Database();

	static
	{
		try
		{
			db.createTable(SettingValues.class);
			List<Object> settingList = db.getObject(SettingValues.class, null);
			if(settingList.size() > 0)
				values = (SettingValues) settingList.get(0);
			else
				db.insertObject(values);
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
		db.saveObject(values);
		return Response.noContent().build();
	}
}
