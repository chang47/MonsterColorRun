package dbReference;

import java.util.ArrayList;

import metaModel.Dungeon;
import metaModel.Route;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DungeonManager {
	private static final String TABLE_DUNGEON = "dungeon";
	private static final String DUNGEON_ID = "dungeon_id";
	private static final String CITY_ID = "city_id";
	private static final String DUNGEON_NAME = "dungeon_name";
	private static final String DUNGEON_DESCRIPTION = "dungeon_description";
	private static final String MIN = "min";
	private static final String MAX = "max";
	
	public static void create(SQLiteDatabase db) {
		String CREATE_DUNGEON_TABLE = "CREATE TABLE " + TABLE_DUNGEON + "("
                + DUNGEON_ID + " INTEGER PRIMARY KEY," + CITY_ID + " INTEGER,"
                + DUNGEON_NAME + " TEXT," + DUNGEON_DESCRIPTION + " TEXT," 
                + MIN + " INTEGER," + MAX + " INTEGER" + ")";
		db.execSQL(CREATE_DUNGEON_TABLE);
		createInitial(db, new Dungeon(1, 1, "Little Forest", "Home to critters that loves to bathe under the sunlight in the shade", 1, 3));
		createInitial(db, new Dungeon(2, 1, "Dark Woods", "Thick trees so high covers the sun casting the woods into eternal darkness", 1, 3));
		createInitial(db, new Dungeon(3, 2, "Peak Shore", "A wide beach area that's litered with shells and other sea life", 2, 4));
	}
	
	private static void createInitial(SQLiteDatabase db, Dungeon dungeon) {
		ContentValues values = new ContentValues();
		values.put(DUNGEON_ID, dungeon.dungeonId);
		values.put(DUNGEON_NAME, dungeon.dungeonName);
		values.put(CITY_ID, dungeon.cityId);
		values.put(DUNGEON_DESCRIPTION, dungeon.description);
		values.put(MIN, dungeon.min);
		values.put(MAX, dungeon.max);
		
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
				int cityId = cursor.getInt(1);
				String dungeonName = cursor.getString(2);
				String description = cursor.getString(3);
				int min = cursor.getInt(4);
				int max = cursor.getInt(5);
				Dungeon dungeon= new Dungeon(dungeonId, cityId, dungeonName, description, min, max);
				list.add(dungeon);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
}
