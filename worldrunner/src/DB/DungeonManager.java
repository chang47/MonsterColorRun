package DB;

import java.util.ArrayList;

import DB.Model.Dungeon;
import DB.Model.Route;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DungeonManager {
	private static final String TABLE_DUNGEON = "dungeon";
	private static final String DUNGEON_ID = "dungeon_id";
	private static final String DUNGEON_NAME = "dungeon_name";
	private static final String CITY_ID = "city_id";
	
	public static void create(SQLiteDatabase db) {
		String CREATE_DUNGEON_TABLE = "CREATE TABLE " + TABLE_DUNGEON + "("
                + DUNGEON_ID + " INTEGER PRIMARY KEY," + DUNGEON_NAME + " TEXT," +
				CITY_ID + " INTEGER)";
		db.execSQL(CREATE_DUNGEON_TABLE);
		createInitial(db, new Dungeon(1, "Beginner Dungeon", 1));
		createInitial(db, new Dungeon(2, "Expert Dungeon", 2));
	}
	
	private static void createInitial(SQLiteDatabase db, Dungeon dungeon) {
		ContentValues values = new ContentValues();
		values.put(DUNGEON_ID, dungeon.dungeonId);
		values.put(DUNGEON_NAME, dungeon.dungeonName);
		values.put(CITY_ID, dungeon.cityId);
		db.insert(TABLE_DUNGEON, null, values);
	}
	
	public static void drop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DUNGEON);
	}
	
	public static ArrayList<Dungeon> getDungeons(SQLiteDatabase db) {
		ArrayList<Dungeon> list = new ArrayList<Dungeon>();
		String select = "SELECT * FROM " + TABLE_DUNGEON;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				int dungeonId = cursor.getInt(0);
				String dungeonName = cursor.getString(1);
				int cityId = cursor.getInt(2);
				Dungeon dungeon= new Dungeon(dungeonId, dungeonName, cityId);
				list.add(dungeon);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
}
