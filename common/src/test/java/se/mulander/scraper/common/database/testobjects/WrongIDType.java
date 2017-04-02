package se.mulander.scraper.common.database.testobjects;

import se.mulander.scraper.common.database.MaxLength;
import se.mulander.scraper.common.database.TableName;

/**
 * Created by marcu on 2017-03-15.
 */
@TableName(name = "testobject")
public class WrongIDType
{
	public String databaseID = "1";
	public int intVal;
	public boolean boolValue;
	@MaxLength(length = 20)
	public String stringValue;

	public WrongIDType(int intValue, boolean boolValue, String stringValue)
	{
		this.intVal = intValue;
		this.boolValue = boolValue;
		this.stringValue = stringValue;
	}

	public WrongIDType()
	{

	}
}