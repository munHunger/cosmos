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
			beforeEach(() ->
			{
				db.setDbPath("testDatabase.db");
				new File("testDatabase.db").delete();
			});
			afterEach(() ->
			{
				new File("testDatabase.db").delete();
			});
			describe("Database path", () ->
			{
				describe("Can set path", () ->
				{
					String dbPath = "test";
					beforeEach(() ->
					{
						db.setDbPath(dbPath);
					});
					it("updated a path", () ->
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
						it("throws on missing databaseID field", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.createTable(MissingID.class);
							}).toThrow(IllegalArgumentException.class);
						});
						it("throws on databaseID field not being int", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.createTable(WrongIDType.class);
							}).toThrow(IllegalArgumentException.class);
						});
						it("throws on missing MaxLength attribute", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.createTable(MissingLength.class);
							}).toThrow(IllegalArgumentException.class);
						});
						it("throws on missing TableName attribute", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.createTable(MissingTableName.class);
							}).toThrow(IllegalArgumentException.class);
						});
						it("throws on unsupported field", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.createTable(IllegalType.class);
							}).toThrow(IllegalArgumentException.class);
						});
						it("throws on missing empty constructor", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.createTable(MissingConstructor.class);
							}).toThrow(IllegalArgumentException.class);
						});
					});
					it("created a table", () ->
					{
						expect(db.createTable(ValidObject.class)).toBeTrue();
					});
					describe("Table is not repeatedly created", () ->
					{
						beforeEach(() ->
						{
							db.createTable(ValidObject.class);
						});
						it("did not create table", () ->
						{
							expect(db.createTable(ValidObject.class)).toBeFalse();
						});
					});
				});
				describe("Can insert object", () ->
				{
					describe("Throws exceptions on illegal objects", () ->
					{
						it("throws on objects with unsupported fields", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.insertObject(new IllegalType(-1, true, null));
							}).toThrow(IllegalArgumentException.class);
						});
						it("throws on objects with uninitialized tables", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.insertObject(new DifferentTable(-1, true, null));
							}).toThrow(IllegalStateException.class);
						});
						it("throws on objects without tablename annotation", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.insertObject(new MissingTableName(-1, true, null));
							}).toThrow(IllegalArgumentException.class);
						});
					});
					ValidObject testObject = new ValidObject(53, true, "testvalue");
					beforeEach(() ->
					{
						db.createTable(ValidObject.class);
					});
					it("created an object", () ->
					{
						expect(db.insertObject(testObject)).toBeTrue();
					});
					describe("Object is not repeatedly created", () ->
					{
						beforeEach(() ->
						{
							db.insertObject(testObject);
						});
						it("it did not create object again", () ->
						{
							expect(db.insertObject(testObject)).toBeFalse();
						});
					});
				});
				describe("Can save an object", () ->
				{
					ValidObject testObject = new ValidObject(3, true, null);
					beforeEach(() ->
					{
						db.createTable(testObject.getClass());
						db.insertObject(testObject);
					});
					describe("Throws exceptions on faulty input", () ->
					{
						it("throws on objects with unsupported fields", () ->
						{
							new ExceptionMatcher(() ->
							{
								IllegalType illegalObject = new IllegalType(-1, true, "");
								illegalObject.databaseID = testObject.databaseID;
								db.saveObject(illegalObject);
							}).toThrow(IllegalArgumentException.class);
						});
						it("throws on objects with uninitialized tables", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.saveObject(new DifferentTable(-1, true, null));
							}).toThrow(IllegalStateException.class);
						});
						it("throws on objects without tablename annotation", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.saveObject(new MissingTableName(-1, true, null));
							}).toThrow(IllegalArgumentException.class);
						});
						it("throws on objects without databaseID field", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.saveObject(new MissingID(-1, true, null));
							}).toThrow(IllegalArgumentException.class);
						});
						it("throws on objects that has not yet been inserted", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.saveObject(new ValidObject(4, true, null));
							}).toThrow(IllegalStateException.class);
						});
					});
					describe("Doesn't save an object that has a databaseID that does not exist in the database(deleted)", () ->
					{
						beforeEach(() ->
						{
							testObject.databaseID = 99;
						});
						it("does not save an object", () ->
						{
							expect(db.saveObject(testObject)).toBeFalse();
						});
					});
					it("saved an object", () ->
					{
						expect(db.saveObject(testObject)).toBeTrue();
					});
				});
				describe("Can get object", () ->
				{
					beforeEach(() ->
					{
						db.createTable(ValidObject.class);
						db.insertObject(new ValidObject(5, true, null));
						db.insertObject(new ValidObject(10, false, null));
						db.insertObject(new ValidObject(15, true, "a"));
					});
					describe("Throws exceptions on faulty input", () ->
					{
						it("throws on objects without tablename annotation", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.getObject(MissingTableName.class, "");
							}).toThrow(IllegalArgumentException.class);
						});
						it("throws on objects witch table is not yet initialized", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.getObject(DifferentTable.class, null);
							}).toThrow(IllegalStateException.class);
						});
						it("throws on objects with illegal fields", () ->
						{
							new ExceptionMatcher(() ->
							{
								db.getObject(IllegalType.class, null);
							}).toThrow(IllegalArgumentException.class);
						});
					});
					it("gets all objects", () ->
					{
						expect(db.getObject(ValidObject.class, "").size()).toEqual(3);
					});
					it("gets some objects with simple where statement", () ->
					{
						expect(db.getObject(ValidObject.class, "intVal >= 10").size()).toEqual(2);
					});
				});
			});
		});
	}
}