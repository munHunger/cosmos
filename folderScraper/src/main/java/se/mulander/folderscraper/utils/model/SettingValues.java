package se.mulander.folderscraper.utils.model;

import se.mulander.scraper.common.database.MaxLength;
import se.mulander.scraper.common.database.TableName;

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
