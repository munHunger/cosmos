package se.mulander.cosmos.folderscraper.model;

/**
 * Created by marcu on 2017-02-18.
 */
public class FileObject {
    public int databaseID;
    public String filePath;
    public String folderDestination;
    public boolean isComplete = false;
    public boolean isMovie = false;
    public boolean isTV = false;
    public int externalID = -1;
    public String title;

    public FileObject() {
    }

    public FileObject(String filePath) {
        this.filePath = filePath;
    }
}
