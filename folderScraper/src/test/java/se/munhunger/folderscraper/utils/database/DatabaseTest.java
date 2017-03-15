package se.munhunger.folderscraper.utils.database;

import com.mscharhag.oleaster.matcher.matchers.ExceptionMatcher;
import com.mscharhag.oleaster.runner.OleasterRunner;
import org.junit.runner.RunWith;
import se.munhunger.folderscraper.utils.database.testobjects.*;

import java.io.File;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;

/**
 * Created by marcu on 2017-03-15.
 */
@RunWith(OleasterRunner.class)
public class DatabaseTest
{
	{
		describe("Database", () ->
		{
			Database db = new Database();
			before(() ->
			{
				db.setDbPath("testDatabase.db");
				new File("testDatabase.db").delete();
			});
			after(() ->
			{
				new File("testDatabase.db").delete();
			});
			describe("Database path", () ->
			{
				describe("Can set path", () ->
				{
					String dbPath = "test";
					before(() ->
					{
						db.setDbPath(dbPath);
					});
					it("Path should be updated", () ->
					{
						expect(db.getDbPath()).toEqual(dbPath);
					});
				});
			});
			describe("CRUD operations", () ->
			{
				describe("Can create table", () ->
				{
					describe("Throws exceptions on faulty tables", () ->
					{
						it("Missing databaseID field", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.createTable(MissingID.class);
							}).toThrow(IllegalArgumentException.class);
						});
						it("databaseID field is not int", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.createTable(WrongIDType.class);
							}).toThrow(IllegalArgumentException.class);
						});
						it("Missing MaxLength attribute", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.createTable(MissingLength.class);
							}).toThrow(IllegalArgumentException.class);
						});
						it("Missing TableName attribute", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.createTable(MissingTableName.class);
							}).toThrow(IllegalArgumentException.class);
						});
					});
					boolean wasCreated = db.createTable(ValidObject.class);
					it("Table was created", () ->
					{
						expect(wasCreated).toBeTrue();
					});
					describe("Table is not repeatedly created", () ->
					{
						it("Table was not created", () ->
						{
							expect(db.createTable(ValidObject.class)).toBeFalse();
						});
					});
					describe("Can insert object", () ->
					{
						ValidObject testObject = new ValidObject(53, true, "testvalue");
						boolean wasInserted = db.insertObject(testObject);
						it("Object was created", () ->
						{
							expect(wasInserted).toBeTrue();
						});
						describe("Object is not repeatedly created", () ->
						{
							it("Object was not created", () ->
							{
								expect(db.insertObject(testObject)).toBeFalse();
							});
						});
					});
				});
			});
		});
	}
}