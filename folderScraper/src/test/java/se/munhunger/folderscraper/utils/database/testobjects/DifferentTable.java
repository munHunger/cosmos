package se.munhunger.folderscraper.utils.database.testobjects;

import se.munhunger.folderscraper.utils.database.MaxLength;
import se.munhunger.folderscraper.utils.database.TableName;

/**
 * Created by marcu on 2017-03-15.
 */
@TableName(name = "differentTable")
public class DifferentTable
{
	public int databaseID = 1;
	public int intVal;
	public boolean boolValue;
	@MaxLength(length = 20)
	public String stringValue;

	public DifferentTable(int intValue, boolean boolValue, String stringValue)
	{
		this.intVal = intValue;
		this.boolValue = boolValue;
		this.stringValue = stringValue;
	}

	public DifferentTable()
	{
	}
}