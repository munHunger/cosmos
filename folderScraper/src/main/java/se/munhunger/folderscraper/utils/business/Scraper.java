package se.munhunger.folderscraper.utils.business;

import se.munhunger.folderscraper.Settings;
import se.munhunger.folderscraper.utils.database.Database;
import se.munhunger.folderscraper.utils.model.FileObject;
import se.munhunger.folderscraper.utils.model.OMDBResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcu on 2017-03-17.
 */
public class Scraper
{
	public static Thread watchThread;

	private static Thread createWatcher()
	{
		return new Thread(() ->
		{
			while(true)
			{
				try
				{
					new Scraper().getFolderStatus();
					Thread.sleep(Settings.values.updateDelay * 1000);
				}
				catch(Exception e)
				{
					System.err.println("Error while running watcher");
					e.printStackTrace();
				}
			}
		});
	}

	public static boolean isRunningWatcher()
	{
		if(watchThread == null)
			watchThread = createWatcher();
		return watchThread.isAlive();
	}

	public static void startWatcher()
	{
		if(watchThread != null && watchThread.isAlive())
			throw new IllegalArgumentException("Watcher already running");
		watchThread = createWatcher();
		watchThread.start();
	}

	public static void stopWatcher()
	{
		watchThread.interrupt();
		watchThread = null;
	}

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
			if(o.isComplete && (o.isTV || o.isMovie))
				moveObject(o);
			searchMetaData(o);
			db.saveObject(o);
		}
		return result;
	}

	public String moveObject(FileObject o) throws IOException
	{
		if(!o.isComplete)
			throw new IllegalArgumentException("FileObject must be completed before moving");
		String destination;
		if(o.isMovie && o.isTV)
			throw new IllegalArgumentException("FileObject is both movie and tv-show");
		else if(o.isMovie)
			destination = Settings.values.moviePath;
		else if(o.isTV)
			destination = Settings.values.tvPath;
		else
			throw new IllegalArgumentException("FileObject has not been classified as either a TV-Show or a Movie");
		destination = destination + o.filePath.substring(Math.max(0, Math.max(o.filePath.lastIndexOf("\\"), o.filePath.lastIndexOf("/"))));
		Files.move(Paths.get(o.filePath), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
		return destination;
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
		String name = o.filePath.substring(Math.max(0, Math.max(o.filePath.lastIndexOf("\\"), o.filePath.lastIndexOf("/"))), o.filePath.length());
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
				OMDBResponse res = (OMDBResponse) HttpRequest.getRequest(search, OMDBResponse.class);
				if(res.Response.toUpperCase().equals("TRUE"))
				{
					o.isTV = res.Type.toUpperCase().equals("SERIES");
					o.isMovie = res.Type.toUpperCase().equals("MOVIE");
					return o;
				}
			}
		}
		return o;
	}
}
