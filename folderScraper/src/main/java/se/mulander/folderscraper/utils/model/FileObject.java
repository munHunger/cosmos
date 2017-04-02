package se.mulander.folderscraper.utils.model;

import se.mulander.scraper.common.database.MaxLength;
import se.mulander.scraper.common.database.TableName;

/**
 * Created by marcu on 2017-02-18.
 */
@TableName(name = "file")
public class FileObject
{
	public int databaseID;
	@MaxLength(length = 256)
	public String filePath;
	@MaxLength(length = 256)
	public String folderDestination;
	public boolean isComplete = false;
	public boolean isMovie = false;
	public boolean isTV = false;
	public int externalID = -1;

	public FileObject()
	{
	}

	public FileObject(String filePath)
	{
		this.filePath = filePath;
	}
}
