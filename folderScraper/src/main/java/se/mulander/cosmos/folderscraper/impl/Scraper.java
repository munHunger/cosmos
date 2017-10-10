package se.mulander.cosmos.folderscraper.impl;

import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.common.database.Database;
import se.mulander.cosmos.common.model.movies.tmdb.TMDBResponse;
import se.mulander.cosmos.folderscraper.model.FileObject;
import se.mulander.cosmos.folderscraper.util.Settings;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Business end of the folder scraper.
 * This manages getting statuses of the watched folder, moving completed downloads and starting/stopping the watchthread.
 * <p>
 * Created by marcu on 2017-03-17.
 */
public class Scraper
{
    private static Thread watchThread;

    /**
     * Creates a new Thread that watches the watchFolder and updates the status of the FileObjects.
     * Note that it is an infinite thread that during normal operations never terminates.
     *
     * @return a new thread that watches the watchPath for new files.
     * @see #getFolderStatus()
     */
    private static Thread createWatcher()
    {
        return new Thread(() -> {
            while (true)
            {
                try
                {
                    new Scraper().getFolderStatus();
                    Thread.sleep(Integer.parseInt(Settings.getSettingsValue("screaper.update_delay")) * 1000);
                } catch (Exception e)
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
        if (watchThread == null) watchThread = createWatcher();
        return watchThread.isAlive();
    }

    /**
     * Starts the watcher thread.
     *
     * @throws IllegalStateException If the watchThread is already running
     */
    public static void startWatcher() throws IllegalStateException
    {
        if (isRunningWatcher()) throw new IllegalStateException("Watcher already running");
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
        if (!isRunningWatcher()) throw new IllegalStateException("Watcher is not running");
        watchThread.interrupt();
        watchThread = null;
    }

    /**
     * Moves a directory and any sub files/directories
     *
     * @param sourceDir
     * @param targetDir
     * @throws IOException
     */
    private static void copyDirectory(File sourceDir, File targetDir) throws IOException
    {
        if (sourceDir.isDirectory())
        {
            copyDirectoryRecursively(sourceDir, targetDir);
        }
        else if (sourceDir.exists())
        {
            Files.move(sourceDir.toPath(), targetDir.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Copies a directory recursively
     *
     * @param source
     * @param target
     * @throws IOException
     */
    private static void copyDirectoryRecursively(File source, File target) throws IOException
    {
        if (!target.exists())
        {
            target.mkdir();
        }

        for (String child : source.list())
        {
            copyDirectory(new File(source, child), new File(target, child));
        }
    }

    /**
     * Searches the watchPath as found in the Settings for files and directories.
     * All top level files and directories will be converted to FileObjects and searched for using the OMDB api.
     * If a FileObject is found that is completed, it will attempt to move it to it's corresponding location.
     *
     * @return a list of all FileObjects found in the watchPath.
     * @throws Exception if shit goes wrong
     * @see #searchMetaData(FileObject) {@link #moveObject(FileObject)}
     */
    public List<FileObject> getFolderStatus() throws Exception
    {
        return getFolderStatus(Settings.getSettingsValue("scraper.folders.downloads"));
    }

    /**
     * Searches the supplied folder path for files and directories.
     * All top level files and directories will be converted to FileObjects and searched for using the OMDB api.
     * If a FileObject is found that is completed, it will attempt to move it to it's corresponding location.
     *
     * @return a list of all FileObjects found in the folder
     * @throws Exception if shit goes wrong
     * @see #searchMetaData(FileObject) {@link #moveObject(FileObject)}
     */
    public List<FileObject> getFolderStatus(String folder) throws Exception
    {
        Database db = new Database();
        File[] files = new File(folder).listFiles();
        db.createTable(FileObject.class);
        List<FileObject> result = new ArrayList<>();
        if (files != null)
        {
            for (File f : files)
            {
                if (f.isDirectory())
                {
                    cleanDirectory(f);
                    continue;
                }
                if (f.getPath().endsWith("ignore"))
                {
                    if (!f.delete()) throw new Exception("Could not delete ignore file:" + f.getPath());
                    continue;
                }
                if (f.getPath().endsWith("part")) continue;
                List<Object> dbStatus = db.getObject(FileObject.class,
                                                     String.format("filePath = '%s'",
                                                                   f.getPath().replaceAll("'", "/'")));
                if (dbStatus.size() > 0) result.add((FileObject) dbStatus.get(0));
                else
                {
                    FileObject newObject = new FileObject();
                    newObject.filePath = f.getPath();
                    db.insertObject(newObject);
                    result.add(newObject);
                }
            }
        }
        for (FileObject o : result)
        {
            o.isComplete = isDone(o);
            searchMetaData(o);
            if (o.isComplete && (o.isTV || o.isMovie)) moveObject(o);
            db.saveObject(o);
        }
        return result;
    }

    /**
     * Cleans a directory.
     * i.e. recursively removes all .ignore files and empty directories
     *
     * @param directory the directory to clean
     */
    private void cleanDirectory(File directory)
    {
        if (!directory.isDirectory()) throw new IllegalArgumentException("Argument was not a directory");
        for (File sub : directory.listFiles())
        {
            if (sub.isDirectory()) cleanDirectory(sub);
            else if (sub.getPath().endsWith("ignore")) sub.delete();
        }
        if (directory.listFiles().length == 0) directory.delete();
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
        if (!o.isComplete) throw new IllegalArgumentException("FileObject must be completed before moving");
        String destination;
        if (o.isMovie && o.isTV) throw new IllegalArgumentException("FileObject is both movie and tv-show");
        else if (!o.isMovie && !o.isTV)
            throw new IllegalArgumentException("FileObject has not been classified as either a TV-Show or a Movie");

        destination = o.isMovie ? Settings.getSettingsValue("scraper.folders.movies") : (Settings.getSettingsValue(
                "scraper.folders.tv") + "\\" + o.title);

        destination = destination + o.filePath.substring(Math.max(0,
                                                                  Math.max(o.filePath.lastIndexOf("\\"),
                                                                           o.filePath.lastIndexOf("/"))));
        copyDirectory(new File(o.filePath), new File(destination));
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
        if (new File(o.filePath).isDirectory())
        {
            File[] subFiles = new File(o.filePath).listFiles();
            for (File f : subFiles)
                if (f.getPath().endsWith("part")) return false;
            return true;
        }
        else return !new File(o.filePath + ".part").exists();
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
        String theMovieDbURL = Settings.getSettingsValue("movies.movie_db_api_uri");
        String apiKey = Settings.getSettingsValue("movies.movie_db_api_key");
        StringBuilder urlBuilder = new StringBuilder(theMovieDbURL).append("/3/search/multi");
        String name = o.filePath.substring(Math.max(0,
                                                    Math.max(o.filePath.lastIndexOf("\\"),
                                                             o.filePath.lastIndexOf("/"))), o.filePath.length());
        String[] nameComponents = name.split("\\ |\\.");
        for (int i = nameComponents.length; i > 0; i--)
        {
            for (int n = nameComponents.length - i; n > 0; n--)
            {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < i; j++)
                {
                    if (!nameComponents[n + j].isEmpty()) builder.append(nameComponents[n + j]).append(".");
                }
                urlBuilder.append("?api_key=").append(apiKey);
                urlBuilder.append("&include_adult=false");
                urlBuilder.append("&query=")
                          .append(URLEncoder.encode(builder.toString(), StandardCharsets.UTF_8.toString()));
                TMDBResponse res = (TMDBResponse) HttpRequest.getRequest(urlBuilder.toString(),
                                                                         TMDBResponse.class).data;
                if (res.results.length > 0)
                {
                    TMDBResponse.Result firstResult = res.results[0];
                    o.isTV = firstResult.media_type.toUpperCase().equals("TV");
                    o.isMovie = firstResult.media_type.toUpperCase().equals("MOVIE");
                    o.title = firstResult.title;
                    return o;
                }
            }
        }
        return o;
    }
}
