package se.mulander.cosmos.common.database.testobjects;

import se.mulander.cosmos.common.database.MaxLength;
import se.mulander.cosmos.common.database.TableName;

/**
 * Created by marcu on 2017-03-15.
 */
@TableName(name = "testobject")
public class ValidObject
{
	public int databaseID = -1;
	public int intVal;
	public boolean boolValue;
	@MaxLength(length = 20)
	public String stringValue;

	public ValidObject(int intVal, boolean boolValue, String stringValue)
	{
		this.intVal = intVal;
		this.boolValue = boolValue;
		this.stringValue = stringValue;
	}

	public ValidObject()
	{
	}
}