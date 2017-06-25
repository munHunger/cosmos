package se.mulander.cosmos.folderscraper.utils.business;

import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.common.database.Database;
import se.mulander.cosmos.folderscraper.Settings;
import se.mulander.cosmos.folderscraper.utils.model.FileObject;
import se.mulander.cosmos.folderscraper.utils.model.OMDBResponse;

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

	/**
	 * Creates a new Thread that watches the watchFolder and updates the status of the FileObjects.
	 * Note that it is an infinite thread that during normal operations never terminates.
	 *
	 * @return a new thread that watches the watchPath for new files.
	 * @see #getFolderStatus()
	 */
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

	/**
	 * Checks if the watcher is running.
	 * If the watchThread is null, a new Thread will be created.
	 * Note that the new Thread will not be started.
	 *
	 * @return true iff the thread is running
	 */
	public static boolean isRunningWatcher()
	{
		if(watchThread == null)
			watchThread = createWatcher();
		return watchThread.isAlive();
	}

	/**
	 * Starts the watcher thread.
	 *
	 * @throws IllegalStateException If the watchThread is already running
	 */
	public static void startWatcher() throws IllegalStateException
	{
		if(isRunningWatcher())
			throw new IllegalStateException("Watcher already running");
		watchThread.start();
	}


	/**
	 * Stops the watcher thread if is running.
	 * If watchThread is null, nothing happens.
	 *
	 * @throws IllegalStateException If watchThread is not running or null
	 */
	public static void stopWatcher() throws IllegalStateException
	{
		if(!isRunningWatcher())
			throw new IllegalStateException("Watcher is not running");
		watchThread.interrupt();
		watchThread = null;
	}

	/**
	 * Searches the watchPath as found in the Settings for files and directories.
	 * All top level files and directories will be converted to FileObjects and searched for using the OMDB api.
	 * If a FileObject is found that is completed, it will attempt to move it to it's corresponding location.
	 *
	 * @return a list of all FileObjects found in the watchPath.
	 * @throws Exception
	 * @see #searchMetaData(FileObject) {@link #moveObject(FileObject)}
	 */
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

	/**
	 * Moves the object to the corresponding location as noted by the Settings variables.
	 *
	 * @param o The FileObject to move.
	 * @return The new path for the FileObject
	 * @throws IOException              If the move fails.
	 * @throws IllegalArgumentException If the object is not completed, both a movie and a tv-show or neither.
	 */
	public String moveObject(FileObject o) throws IOException, IllegalArgumentException
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

	/**
	 * Checks if the FileObject is done downloading.
	 * This is determined by searching for files ending with .part
	 * i.e. the downloader must be configured to use .part files for incomplete downloads.
	 *
	 * @param o the FileObject to check
	 * @return true iff the FileObject is done downloading. i.e. no .part files found.
	 */
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

	/**
	 * Searches OMDB for the item represented by the FileObject.
	 * It uses the path in the FileObject to get an idea of what the title might be.
	 * Once found the FileObject will be updated with corresponding flags from the OMDB response
	 *
	 * @param o The file object to use in search.
	 * @return The object o, edited only if an OMDB response was found, otherwise unedited.
	 * Note that the return is the same object as the param.
	 * @throws Exception
	 */
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
				OMDBResponse res = (OMDBResponse) HttpRequest.getRequest(search, OMDBResponse.class).data;
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
