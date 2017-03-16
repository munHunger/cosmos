package se.munhunger.folderscraper.utils.database.testobjects;

import se.munhunger.folderscraper.utils.database.MaxLength;
import se.munhunger.folderscraper.utils.database.TableName;

/**
 * Created by marcu on 2017-03-15.
 */
@TableName(name = "testobject")
public class IllegalType
{
	public int databaseID = 1;
	public int intVal;
	@MaxLength(length = 20)
	public String stringValue;
	public Object boolValue;

	public IllegalType(int intValue, boolean boolValue, String stringValue)
	{
		this.intVal = intValue;
		this.stringValue = stringValue;
	}

	public IllegalType()
	{

	}
}