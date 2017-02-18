package se.munhunger.folderscraper.utils.database;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcu on 2017-02-16.
 */
public class Database
{
	/**
	 * Gets a connection to the database
	 *
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:database.db");
		}
		catch(ClassNotFoundException | SQLException e)
		{
			throw e;
		}
	}

	public static List<Object> getObject(Class c, String whereStatement) throws Exception
	{
		List<Object> result = new ArrayList<Object>();
		Field[] fields = c.getDeclaredFields();
		TableName tableAnnotation = (TableName) c.getAnnotation(TableName.class);
		if(tableAnnotation == null)
			throw new IllegalArgumentException("Input object must have a TableName annotion");
		String tableName = tableAnnotation.name();
		try(Connection conn = getConnection())
		{
			PreparedStatement stmt = conn.prepareStatement("SELECT *, ROWID FROM " + tableName + ((whereStatement == null || whereStatement.isEmpty()) ? "" : (" WHERE " + whereStatement)));
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
						else if(className.equals("int") || className.equals("java.lang.Integer"))
							f.setInt(o, res.getInt(f.getName()));
						else if(className.equals("boolean") || className.equals("java.lang.Boolean"))
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
	 * @return true if a new table was created, false otherwise
	 * @throws Exception
	 */
	public static boolean createTable(Class c) throws Exception
	{
		Field[] fields = c.getDeclaredFields();
		TableName tableAnnotation = (TableName) c.getAnnotation(TableName.class);
		if(tableAnnotation == null)
			throw new IllegalArgumentException("Input object must have a TableName annotion");
		String tableName = tableAnnotation.name();
		try(Connection conn = getConnection())
		{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, tableName, null);
			if(rs.next())
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
					else if(className.equals("int") || className.equals("java.lang.Integer"))
						sqlBuilder.append(f.getName()).append(" INT,");
					else if(className.equals("boolean") || className.equals("java.lang.Boolean"))
						sqlBuilder.append(f.getName()).append(" BOOLEAN,");
					else
						throw new IllegalArgumentException(String.format("Input was of type:%s. Only String, Integer, and Boolean are accepted", f.getType().getName()));
				}
			String sql = sqlBuilder.toString();
			sql = sql.substring(0, sql.length() - 1) + ");";
			System.out.println(sql);
			return conn.prepareStatement(sql).execute();
		}
	}

	/**
	 * Inserts an object into the database.
	 * Note that the table noted by the #TableName must exist before this call
	 *
	 * @param o the object to create
	 * @return true iff successfull
	 * @throws Exception
	 */
	public static boolean insertObject(Object o) throws Exception
	{
		Field[] fields = o.getClass().getDeclaredFields();
		TableName tableAnnotation = (TableName) o.getClass().getAnnotation(TableName.class);
		if(tableAnnotation == null)
			throw new IllegalArgumentException("Input object must have a TableName annotion");
		String tableName = tableAnnotation.name();
		try(Connection conn = getConnection())
		{
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
						stmt.setString(index, f.get(o).toString());
					else if(className.equals("int") || className.equals("java.lang.Integer"))
						stmt.setInt(index, f.getInt(o));
					else if(className.equals("boolean") || className.equals("java.lang.Boolean"))
						stmt.setBoolean(index, f.getBoolean(o));
					else
						throw new IllegalArgumentException(String.format("Input was of type:%s. Only String, Integer, and Boolean are accepted", f.getType().getName()));
					index++;
				}
			return stmt.execute();
		}
		catch(Exception e)
		{
			throw e;
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
	public static boolean saveObject(Object o) throws Exception
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
		}
		catch(NoSuchFieldException e)
		{
			throw new IllegalArgumentException("Input object cannot be saved to database, because it is missing a databaseID: int field");
		}
		try(Connection conn = getConnection())
		{
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
						stmt.setString(index, f.get(o).toString());
					else if(className.equals("int") || className.equals("java.lang.Integer"))
						stmt.setInt(index, f.getInt(o));
					else if(className.equals("boolean") || className.equals("java.lang.Boolean"))
						stmt.setBoolean(index, f.getBoolean(o));
					else
						throw new IllegalArgumentException(String.format("Input was of type:%s. Only String, Integer, and Boolean are accepted", f.getType().getName()));
					index++;
				}
			stmt.setInt(index, rowID);
			return stmt.execute();
		}
		catch(Exception e)
		{
			throw e;
		}
	}
}
