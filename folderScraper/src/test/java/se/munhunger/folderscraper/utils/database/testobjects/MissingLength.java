package se.munhunger.folderscraper.utils.database.testobjects;

import se.munhunger.folderscraper.utils.database.TableName;

/**
 * Created by marcu on 2017-03-15.
 */
@TableName(name = "testobject")
public class MissingLength
{
	public int databaseID = 1;
	public int intVal;
	public boolean boolValue;
	public String stringValue;

	public MissingLength(int intValue, boolean boolValue, String stringValue)
	{
		this.intVal = intValue;
		this.boolValue = boolValue;
		this.stringValue = stringValue;
	}
}
