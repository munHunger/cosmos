package se.mulander.cosmos.movies.utils.model;

import se.mulander.cosmos.common.database.MaxLength;
import se.mulander.cosmos.common.database.TableName;

/**
 * Created by marcu on 2017-02-18.
 */
@TableName(name = "settings")
public class SettingValues
{
	public int databaseID = 1;
	public boolean autoDownload = false;
}
