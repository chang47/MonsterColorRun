package DB;

import java.util.ArrayList;
import java.util.HashMap;

import metaModel.Dungeon;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;
import android.util.SparseIntArray;
//@TODO double check if correct and if you even need this anymore
public class CityMappingManager {
	private static final String TABLE_CITY_TO_DUNGEON = "city_to_dungeon";
	private static final String CITY_ID = "city_id";
	private static final String DUNGEON_ID= "dungeon_id";
	
	private static final String TABLE_CITY_TO_ROUTE = "city_to_route";
	//private static final String CITY_ID = "city_id";
	private static final String ROUTE_ID = "route_id";
	
	public static void create(SQLiteDatabase db) {
		String CREATE_CITYTODUNGEON_TABLE = "CREATE TABLE " + TABLE_CITY_TO_DUNGEON + " ("
                + CITY_ID + " INTEGER," + DUNGEON_ID + " INTEGER," +
				"PRIMARY KEY (" + CITY_ID + "," + DUNGEON_ID + "))";
		db.execSQL(CREATE_CITYTODUNGEON_TABLE);
		String CREATE_CITYTOROUTE_TABLE = "CREATE TABLE " + TABLE_CITY_TO_ROUTE + "("
                + CITY_ID + " INTEGER," + ROUTE_ID + " INTEGER," +
				"PRIMARY KEY (" + CITY_ID + "," + ROUTE_ID + "))";
		db.execSQL(CREATE_CITYTOROUTE_TABLE);
		// note route: city id and their route id
		createInitialRoute(db, 1, 1);
		createInitialRoute(db, 2, 2);
		createInitialDungeon(db, 1, 1);
		createInitialDungeon(db, 2, 2);
	}
	
	// double check if this correct
	private static void createInitialRoute(SQLiteDatabase db, int city, int route) {
		ContentValues values = new ContentValues();
		values.put(CITY_ID, city);
		values.put(ROUTE_ID, route);
		db.insert(TABLE_CITY_TO_ROUTE, null, values);
	}
	
	private static void createInitialDungeon(SQLiteDatabase db, int city, int dungeon) {
		ContentValues values = new ContentValues();
		values.put(CITY_ID, city);
		values.put(DUNGEON_ID, dungeon);
		db.insert(TABLE_CITY_TO_DUNGEON, null, values);
	}
	
	public static void drop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY_TO_DUNGEON);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY_TO_ROUTE);
	}
	
	public static SparseArray<ArrayList<Integer>> getDungeonMapping(SQLiteDatabase db) {
		SparseArray<ArrayList<Integer>> map = new SparseArray<ArrayList<Integer>>();
		String select = "SELECT * FROM " + TABLE_CITY_TO_DUNGEON;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				int city = cursor.getInt(0);
				int dungeon = cursor.getInt(1);
				ArrayList<Integer> dungeonList = map.get(city);
				if (dungeonList != null) {
					dungeonList.add(dungeon);
				} else {
					map.put(city, new ArrayList<Integer>(dungeon));
				}
			} while (cursor.moveToNext());
		}
		return map;
	}
	
	public static SparseArray<ArrayList<Integer>> getRouteMapping(SQLiteDatabase db) {
		SparseArray<ArrayList<Integer>> map = new SparseArray<ArrayList<Integer>>();
		String select = "SELECT * FROM " + TABLE_CITY_TO_ROUTE;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				int city = cursor.getInt(0);
				int route = cursor.getInt(1);
				ArrayList<Integer> routeList = map.get(city);
				if (routeList != null) {
					routeList.add(route);
				} else {
					map.put(city, new ArrayList<Integer>(route));
				}
			} while (cursor.moveToNext());
		}
		return map;
	}
}
