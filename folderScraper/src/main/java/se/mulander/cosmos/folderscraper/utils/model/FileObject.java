package se.mulander.cosmos.folderscraper.utils.model;

import se.mulander.cosmos.common.database.MaxLength;
import se.mulander.cosmos.common.database.TableName;

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
	@MaxLength(length = 256)
	public String title;

	public FileObject()
	{
	}

	public FileObject(String filePath)
	{
		this.filePath = filePath;
	}
}
