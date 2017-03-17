package se.munhunger.folderscraper.utils.business;

import com.google.gson.Gson;
import se.munhunger.folderscraper.Settings;
import se.munhunger.folderscraper.utils.database.Database;
import se.munhunger.folderscraper.utils.model.FileObject;
import se.munhunger.folderscraper.utils.model.OMDBResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcu on 2017-03-17.
 */
public class Scraper
{
	public List<FileObject> getFolderStatus() throws Exception
	{
		Database db = new Database();
		File[] files = new File(Settings.values.watchPath).listFiles();
		db.createTable(FileObject.class);
		List<FileObject> result = new ArrayList<>();
		for(File f : files)
		{
			if(f.getPath().endsWith("ignore") || f.getPath().endsWith("part"))
				continue;
			List<Object> dbStatus = db.getObject(FileObject.class, String.format("filePath = '%s'", f.getPath().replaceAll("'", "/'")));
			if(dbStatus.size() > 0)
				result.add((FileObject) dbStatus.get(0));
			else
			{
				FileObject newObject = new FileObject();
				newObject.filePath = f.getPath();
				db.insertObject(newObject);
				result.add(newObject);
			}
		}
		for(FileObject o : result)
		{
			o.isComplete = isDone(o);
			searchMetaData(o);
			db.saveObject(o);
		}
		return result;
	}

	public boolean isDone(FileObject o)
	{
		if(new File(o.filePath).isDirectory())
		{
			File[] subFiles = new File(o.filePath).listFiles();
			for(File f : subFiles)
				if(f.getPath().endsWith("part"))
					return false;
			return true;
		}
		else
			return !new File(o.filePath + ".part").exists();
	}

	public FileObject searchMetaData(FileObject o) throws Exception
	{
		String url = "http://www.omdbapi.com/?t=";
		String name = o.filePath.substring(o.filePath.lastIndexOf("\\"), o.filePath.length());
		String[] nameComponents = name.split("\\ |\\.");
		for(int i = nameComponents.length; i > 0; i--)
		{
			for(int n = nameComponents.length - i; n > 0; n--)
			{
				StringBuilder builder = new StringBuilder();
				for(int j = 0; j < i; j++)
				{
					if(!nameComponents[n + j].isEmpty())
						builder.append(nameComponents[n + j]).append(".");
				}
				String search = url + builder.toString();
				HttpURLConnection con = (HttpURLConnection) new URL(search).openConnection();
				con.setRequestMethod("GET");
				if(con.getResponseCode() == 200)
				{
					try(BufferedReader in = new BufferedReader(
							new InputStreamReader(con.getInputStream())))
					{
						String inputLine;
						StringBuffer response = new StringBuffer();

						while((inputLine = in.readLine()) != null)
						{
							response.append(inputLine);
						}
						OMDBResponse res = new Gson().fromJson(response.toString(), OMDBResponse.class);
						if(res.Response.equals("True"))
						{
							o.isTV = res.Type.equals("series");
							return o;
						}
					}
				}
			}
		}
		return o;
	}
}
