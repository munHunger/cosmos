package se.mulander.cosmos.folderscraper.utils.model;

import se.mulander.cosmos.common.database.MaxLength;
import se.mulander.cosmos.common.database.TableName;

/**
 * Created by marcu on 2017-02-18.
 */
@TableName(name = "settings")
public class SettingValues
{
	public int databaseID = 1;
	@MaxLength(length = 256)
	public String watchPath = "/";
	@MaxLength(length = 256)
	public String moviePath = "/";
	@MaxLength(length = 256)
	public String tvPath = "/";
	public int updateDelay = 10;
}
