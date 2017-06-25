package se.mulander.cosmos.common.database;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database handler that saves and loads objects.
 * It maps object to sql dynamically based on object signature
 * Created by marcu on 2017-02-16.
 */
@Deprecated
public class Database
{

	private String dbPath = "database.db";

	/**
	 * Sets the path to the local database to use.
	 * Note that if pointed to a database file that doesn't exist one will be created.
	 *
	 * @param dbPath
	 */
	public void setDbPath(String dbPath)
	{
		this.dbPath = dbPath;
	}

	/**
	 * Gets the path to the local database
	 *
	 * @return
	 */
	public String getDbPath()
	{
		return dbPath;
	}

	/**
	 * Gets a connection to the database
	 *
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
		return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
	}

	/**
	 * Gets all objects of type c that matches the where statement
	 *
	 * @param c              the type to search for
	 * @param whereStatement an sql statement begining after the "where"
	 * @return
	 * @throws Exception
	 */
	public List<Object> getObject(Class c, String whereStatement) throws Exception
	{
		List<Object> result = new ArrayList<>();
		Field[] fields = c.getDeclaredFields();
		TableName tableAnnotation = (TableName) c.getAnnotation(TableName.class);
		if(tableAnnotation == null)
			throw new IllegalArgumentException("Input object must have a TableName annotion");
		String tableName = tableAnnotation.name();
		try(Connection conn = getConnection())
		{
			if(!tableExists(conn, tableName))
				throw new IllegalStateException("The supplied object does not yet have a table attached. Run createTable before get");
			String sql = "SELECT *, ROWID FROM " + tableName + ((whereStatement == null || whereStatement.isEmpty()) ? "" : (" WHERE " + whereStatement));
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet res = stmt.executeQuery();
			while(res.next())
			{
				Object o = c.getConstructor().newInstance();
				for(Field f : fields)
				{
					if(!f.getName().equals("databaseID"))
					{
						String className = f.getType().getName();
						if(className.equals("java.lang.String"))
							f.set(o, res.getString(f.getName()));
						else if(className.equals("int"))
							f.setInt(o, res.getInt(f.getName()));
						else if(className.equals("boolean"))
							f.setBoolean(o, res.getBoolean(f.getName()));
						else
							throw new IllegalArgumentException(String.format("Input was of type:%s. Only String, Integer, and Boolean are accepted", f.getType().getName()));
					}
					else
						f.setInt(o, res.getInt("ROWID"));
				}
				result.add(o);
			}
		}
		return result;
	}

	/**
	 * Creates a table in the database based on the class structure.
	 * The class must have a #TableName annotation and it should have a databaseID int.
	 *
	 * @param c
	 * @return true iff a new table was created, false otherwise
	 * @throws Exception
	 */
	public boolean createTable(Class c) throws Exception
	{
		Field[] fields = c.getDeclaredFields();
		TableName tableAnnotation = (TableName) c.getAnnotation(TableName.class);
		if(tableAnnotation == null)
			throw new IllegalArgumentException("Input object must have a TableName annotion");

		try
		{
			c.getConstructor();
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Input class must have an empty constructor");
		}
		try
		{
			if(c.getDeclaredField("databaseID").getType().getName() != "int")
				throw new IllegalArgumentException("databaseID must be of type int");
		}
		catch(NoSuchFieldException e)
		{
			throw new IllegalArgumentException("Class must have have a databaseID field of type int");
		}
		String tableName = tableAnnotation.name();
		try(Connection conn = getConnection())
		{
			if(tableExists(conn, tableName))
				return false;

			StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE ").append(tableName).append("(");
			for(Field f : fields)
				if(!f.getName().equals("databaseID"))
				{
					String className = f.getType().getName();
					if(className.equals("java.lang.String"))
					{
						MaxLength lengthAnnotation = (MaxLength) f.getAnnotation(MaxLength.class);
						if(lengthAnnotation == null)
							throw new IllegalArgumentException("Input object must have a MaxLength annotation on all string fields, with the exception of databaseTable");
						int fieldLength = lengthAnnotation.length();
						sqlBuilder.append(f.getName()).append(" VARCHAR(").append("" + fieldLength).append("),");
					}
					else if(className.equals("int"))
						sqlBuilder.append(f.getName()).append(" INT,");
					else if(className.equals("boolean"))
						sqlBuilder.append(f.getName()).append(" BOOLEAN,");
					else
						throw new IllegalArgumentException(String.format("Input was of type:%s. Only String, Integer, and Boolean are accepted", f.getType().getName()));
				}
			String sql = sqlBuilder.toString();
			sql = sql.substring(0, sql.length() - 1) + ");";
			conn.prepareStatement(sql).execute();
		}
		return true;
	}

	/**
	 * Inserts an object into the database.
	 * Note that the table noted by the #TableName must exist before this call
	 * This function will update the databaseID of the object to reflect the ID of the newly created one
	 *
	 * @param o the object to create
	 * @return true iff successfull
	 * @throws Exception
	 */
	public boolean insertObject(Object o) throws Exception
	{
		Field[] fields = o.getClass().getDeclaredFields();
		TableName tableAnnotation = (TableName) o.getClass().getAnnotation(TableName.class);
		if(tableAnnotation == null)
			throw new IllegalArgumentException("Input object must have a TableName annotion");
		String tableName = tableAnnotation.name();
		try(Connection conn = getConnection())
		{
			if(!tableExists(conn, tableName))
				throw new IllegalStateException("The supplied object does not yet have a table attached. Run createTable before insert");
			if(o.getClass().getField("databaseID").getInt(o) >= 0)
			{
				PreparedStatement stmt = conn.prepareStatement("SELECT ROWID FROM " + tableName);
				if(stmt.executeQuery().next())
					return false;
			}
			StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(tableName).append("(");
			boolean first = true;
			for(Field f : fields)
				if(!f.getName().equals("databaseID"))
				{
					sqlBuilder.append((first ? "" : ",") + f.getName());
					first = false;
				}
			sqlBuilder.append(") VALUES(");
			first = true;
			for(Field f : fields)
				if(!f.getName().equals("databaseID"))
				{
					sqlBuilder.append(first ? "" : ",").append("?");
					first = false;
				}
			sqlBuilder.append(")");
			PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString());
			int index = 1;
			for(Field f : fields)
				if(!f.getName().equals("databaseID"))
				{
					String className = f.getType().getName();
					if(className.equals("java.lang.String"))
						stmt.setString(index, f.get(o) != null ? f.get(o).toString() : "");
					else if(className.equals("int"))
						stmt.setInt(index, f.getInt(o));
					else if(className.equals("boolean"))
						stmt.setBoolean(index, f.getBoolean(o));
					else
						throw new IllegalArgumentException(String.format("Input was of type:%s. Only String, Integer, and Boolean are accepted", f.getType().getName()));
					index++;
				}
			boolean updated = stmt.executeUpdate() > 0;
			ResultSet idSet = conn.prepareStatement("SELECT MAX(ROWID) FROM " + tableName).executeQuery();
			if(idSet.next())
				o.getClass().getField("databaseID").setInt(o, idSet.getInt(1));
			return updated;
		}
	}

	/**
	 * Saves an already existing object in the database.
	 * It will use the databaseID int of the object to match it against a ROWID in the database to update.
	 *
	 * @param o
	 * @return true if successfull
	 * @throws Exception
	 */
	public boolean saveObject(Object o) throws Exception
	{
		Field[] fields = o.getClass().getDeclaredFields();
		TableName tableAnnotation = (TableName) o.getClass().getAnnotation(TableName.class);
		if(tableAnnotation == null)
			throw new IllegalArgumentException("Input object must have a TableName annotion");
		String tableName = tableAnnotation.name();
		int rowID = -1;
		try
		{
			rowID = o.getClass().getDeclaredField("databaseID").getInt(o);
			if(rowID == -1)
				throw new IllegalStateException("Object is not in database and cannot be saved.");
		}
		catch(NoSuchFieldException e)
		{
			throw new IllegalArgumentException("Input object cannot be saved to database, because it is missing a databaseID: int field");
		}
		try(Connection conn = getConnection())
		{
			if(!tableExists(conn, tableName))
				throw new IllegalStateException("The supplied object does not yet have a table attached. Run createTable and insert before save");
			StringBuilder sqlBuilder = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
			boolean first = true;
			for(Field f : fields)
				if(!f.getName().equals("databaseID"))
				{
					sqlBuilder.append(first ? "" : ",").append(String.format("%s=?", f.getName()));
					first = false;
				}
			sqlBuilder.append(" WHERE ROWID = ?");
			PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString());
			int index = 1;
			for(Field f : fields)
				if(!f.getName().equals("databaseID"))
				{
					String className = f.getType().getName();
					if(className.equals("java.lang.String"))
						stmt.setString(index, f.get(o) != null ? f.get(o).toString() : "");
					else if(className.equals("int"))
						stmt.setInt(index, f.getInt(o));
					else if(className.equals("boolean"))
						stmt.setBoolean(index, f.getBoolean(o));
					else
						throw new IllegalArgumentException(String.format("Input was of type:%s. Only String, Integer, and Boolean are accepted", f.getType().getName()));
					index++;
				}
			stmt.setInt(index, rowID);
			return stmt.executeUpdate() > 0;
		}
	}

	/**
	 * Checks the database metadata to check if the table exists
	 *
	 * @param conn      a connection to the database
	 * @param tableName the tablename to check
	 * @return true iff the table exists in the database the connection is connected to
	 * @throws Exception
	 */
	private boolean tableExists(Connection conn, String tableName) throws Exception
	{
		DatabaseMetaData md = conn.getMetaData();
		ResultSet rs = md.getTables(null, null, tableName, null);
		return rs.next();
	}
}
