package se.munhunger.folderscraper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by marcu on 2017-02-16.
 */
public class Database
{
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
}
