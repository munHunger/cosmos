package se.mulander.cosmos.common.database.testobjects;

import se.mulander.cosmos.common.database.MaxLength;
import se.mulander.cosmos.common.database.TableName;

/**
 * Created by marcu on 2017-03-15.
 */
@TableName(name = "testobject")
public class MissingConstructor
{
	public int databaseID = -1;
	public int intVal;
	public boolean boolValue;
	@MaxLength(length = 20)
	public String stringValue;

	public MissingConstructor(int intValue, boolean boolValue, String stringValue)
	{
		this.intVal = intValue;
		this.boolValue = boolValue;
		this.stringValue = stringValue;
	}
}