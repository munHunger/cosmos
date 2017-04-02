package se.mulander.cosmos.common.database;

import com.mscharhag.oleaster.matcher.matchers.ExceptionMatcher;
import com.mscharhag.oleaster.runner.OleasterRunner;
import org.junit.runner.RunWith;
import se.mulander.cosmos.common.database.testobjects.*;

import java.io.File;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;

/**
 * Integration testing the database
 * Created by marcus on 2017-03-15.
 */
@RunWith(OleasterRunner.class)
public class DatabaseTest
{
	static
	{
		describe("Database", () ->
		{
			Database db = new Database();
			beforeEach(() ->
					db.setDbPath("testDatabase.db"));
			afterEach(() ->
					new File("testDatabase.db").delete()
			);
			describe("Database path", () ->
					describe("Can set path", () ->
					{
						String dbPath = "test";
						beforeEach(() ->
								db.setDbPath(dbPath));
						it("updated a path", () ->
								expect(db.getDbPath()).toEqual(dbPath));
					}));
			describe("Throws exceptions when trying to create faulty tables", () ->
			{
				it("throws on missing databaseID field", () ->
						new ExceptionMatcher(() ->
								db.createTable(MissingID.class)).toThrow(IllegalArgumentException.class));
				it("throws on databaseID field not being int", () ->
						new ExceptionMatcher(() ->
								db.createTable(WrongIDType.class)).toThrow(IllegalArgumentException.class));
				it("throws on missing MaxLength attribute", () ->
						new ExceptionMatcher(() ->
								db.createTable(MissingLength.class)).toThrow(IllegalArgumentException.class));
				it("throws on missing TableName attribute", () ->
						new ExceptionMatcher(() ->
								db.createTable(MissingTableName.class)).toThrow(IllegalArgumentException.class));
				it("throws on unsupported field", () ->
						new ExceptionMatcher(() ->
								db.createTable(IllegalType.class)).toThrow(IllegalArgumentException.class));
				it("throws on missing empty constructor", () ->
						new ExceptionMatcher(() ->
								db.createTable(MissingConstructor.class)).toThrow(IllegalArgumentException.class));
			});
			it("can create a table", () ->
					expect(db.createTable(ValidObject.class)).toBeTrue());
			describe("Has a valid table created", () ->
			{
				ValidObject testObject = new ValidObject(53, true, "testValue");
				beforeEach(() ->
						db.createTable(ValidObject.class));
				it("cannot create the same table again", () ->
						expect(db.createTable(ValidObject.class)).toBeFalse());
				it("can insert a valid object into the table", () ->
						expect(db.insertObject(testObject)).toBeTrue());
				describe("Has a valid object in the database table", () ->
				{
					beforeEach(() ->
							db.insertObject(testObject));
					it("cannot insert the same object again", () ->
							expect(db.insertObject(testObject)).toBeFalse());
					it("can save an object that already exists", () ->
							expect(db.saveObject(testObject)).toBeTrue());
					it("does not save an object that is not already in the database", () ->
					{
						testObject.databaseID = 99;
						expect(db.saveObject(testObject)).toBeFalse();
					});
					describe("Throws exceptions when trying to save illegal objects", () ->
					{
						it("throws on objects with unsupported fields", () ->
								new ExceptionMatcher(() ->
								{
									IllegalType illegalObject = new IllegalType(-1, true, "");
									illegalObject.databaseID = testObject.databaseID;
									db.saveObject(illegalObject);
								}).toThrow(IllegalArgumentException.class));
						it("throws on objects with uninitialized tables", () ->
								new ExceptionMatcher(() ->
										db.saveObject(new DifferentTable(-1, true, null))).toThrow(IllegalStateException.class));
						it("throws on objects without tableName annotation", () ->
								new ExceptionMatcher(() ->
										db.saveObject(new MissingTableName(-1, true, null))).toThrow(IllegalArgumentException.class));
						it("throws on objects without databaseID field", () ->
								new ExceptionMatcher(() ->
										db.saveObject(new MissingID(-1, true, null))).toThrow(IllegalArgumentException.class));
						it("throws on objects that has not yet been inserted", () ->
								new ExceptionMatcher(() ->
										db.saveObject(new ValidObject(4, true, null))).toThrow(IllegalStateException.class));
					});
				});
				describe("Has many objects created", () ->
				{
					beforeEach(() ->
					{
						db.insertObject(new ValidObject(5, true, null));
						db.insertObject(new ValidObject(10, false, null));
						db.insertObject(new ValidObject(15, true, "a"));
					});
					describe("Throws exceptions on faulty input", () ->
					{
						it("throws on objects without tableName annotation", () ->
								new ExceptionMatcher(() ->
										db.getObject(MissingTableName.class, "")).toThrow(IllegalArgumentException.class));
						it("throws on objects witch table is not yet initialized", () ->
								new ExceptionMatcher(() ->
										db.getObject(DifferentTable.class, null)).toThrow(IllegalStateException.class));
						it("throws on objects with illegal fields", () ->
								new ExceptionMatcher(() ->
										db.getObject(IllegalType.class, null)).toThrow(IllegalArgumentException.class));
					});
					it("gets all objects", () ->
							expect(db.getObject(ValidObject.class, "").size()).toEqual(3));
					it("gets some objects with simple where statement", () ->
							expect(db.getObject(ValidObject.class, "intVal >= 10").size()).toEqual(2));
				});
				describe("Throws exceptions when trying to insert illegal objects", () ->
				{
					it("throws on objects with unsupported fields", () ->
							new ExceptionMatcher(() ->
									db.insertObject(new IllegalType(-1, true, null))
							).toThrow(IllegalArgumentException.class));
					it("throws on objects with uninitialized tables", () ->
							new ExceptionMatcher(() ->
									db.insertObject(new DifferentTable(-1, true, null))
							).toThrow(IllegalStateException.class));
					it("throws on objects without tableName annotation", () ->
							new ExceptionMatcher(() ->
									db.insertObject(new MissingTableName(-1, true, null))
							).toThrow(IllegalArgumentException.class));
				});
			});

		});
	}
}