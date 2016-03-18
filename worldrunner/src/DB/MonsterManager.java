package DB;

import android.database.sqlite.SQLiteDatabase;

public class MonsterManager {
	private static final String TABLE_DUNGEON_MONSTER = "dungeon_monster";
	private static final String DUNGEON_ID = "dungeon_id";
	private static final String MONSTER_ID = "monster_id";
	
	private static final String TABLE_ROUTE_MONSTER = "route_monster";
	private static final String ROUTE_ID = "route_id";
	
	public static void create(SQLiteDatabase db) {
		String CREATE_DUNGEON_MOSNTER_TABLE = "CREATE TABLE " + TABLE_DUNGEON_MONSTER + "("
                + DUNGEON_ID + " INTEGER," + MONSTER_ID + " INTEGER," +
				"PRIMARY KEY (" + DUNGEON_ID + "," + MONSTER_ID + "))";
		db.execSQL(CREATE_DUNGEON_MOSNTER_TABLE);
		String CREATE_ROUTE_MONSTER_TABLE = "CREATE TABLE " + TABLE_ROUTE_MONSTER + "("
                + ROUTE_ID + " INTEGER," + MONSTER_ID + " INTEGER," +
				"PRIMARY KEY (" + ROUTE_ID + "," + MONSTER_ID + "))";
		db.execSQL(CREATE_ROUTE_MONSTER_TABLE);
		
	}
	
	public static void drop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DUNGEON_MONSTER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE_MONSTER);
	}
}
