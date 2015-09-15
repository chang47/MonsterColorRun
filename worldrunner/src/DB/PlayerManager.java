package DB;

import java.util.ArrayList;
import java.util.List;

import DB.Model.Player;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PlayerManager {
	private static final String TABLE_PLAYER = "Player";
	private static final String PID = "pid";
	private static final String USERNAME = "username";
	private static final String FNAME = "fname";
	private static final String LNAME = "lname";
	private static final String LEVEL = "level";
	private static final String EXP = "exp";
	private static final String COIN = "coin";
	private static final String GEM = "gem";
	private static final String CURRENT_STICKER = "current_sticker";
	private static final String MAX_STICKER = "max_sticker";
	private static final String CITY = "city";
	
	public static void create(SQLiteDatabase db) {
		String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYER + "("
                + PID + " INTEGER PRIMARY KEY," + USERNAME + " TEXT,"
                + FNAME + " TEXT," + LNAME + " TEXT," + LEVEL + " INTEGER," +
                EXP +  " INTEGER, " + COIN + " INTEGER," + GEM + " INTEGER," +
                CURRENT_STICKER + " INTEGER," + MAX_STICKER + " INTEGER," +
                CITY + " INTEGER" + ")";
		db.execSQL(CREATE_PLAYER_TABLE);
		// pid, username, fname, lname, level, exp, coin, gem, current, max, city
		createInitial(db, new Player(1, "Coralbue", "Josh", "Chang", 1, 0, 0, 5, 0, 30, 1));
	}
	
	private static void createInitial(SQLiteDatabase db, Player player) {
		ContentValues values = new ContentValues();
		values.put(USERNAME, player.username);
		values.put(FNAME, player.fname);
		values.put(LNAME, player.lname);
		values = addContent(values, player);
		db.insert(TABLE_PLAYER, null, values);
	}
	
	public static void drop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
	}
	
	public static List<Player> getPlayer(SQLiteDatabase db) {
		List<Player> list = new ArrayList<Player>();
		String select = "SELECT * FROM " + TABLE_PLAYER;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				Player player = new Player();
				player.pid = cursor.getInt(0);
				player.username = cursor.getString(1);
				player.fname = cursor.getString(2);
				player.lname = cursor.getString(3);
				player.level = cursor.getInt(4);
				player.exp = cursor.getInt(5);
				player.coin = cursor.getInt(6);
				player.gem = cursor.getInt(7);
				player.currentSticker = cursor.getInt(8);
				player.maxSticker = cursor.getInt(9);
				player.city = cursor.getInt(10);
				list.add(player);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	public static int updatePlayer(SQLiteDatabase db, Player player) {
		ContentValues values = new ContentValues();
		values = addContent(values, player);
		return db.update(TABLE_PLAYER, values, PID + " = ?", new String[] { String.valueOf(player.pid) });
	}
	
	public static void addPlayer(SQLiteDatabase db, Player player) {
		ContentValues values = new ContentValues();
		values.put(USERNAME, player.username);
		values.put(FNAME, player.fname);
		values.put(LNAME, player.lname);
		values = addContent(values, player);
		db.insert(TABLE_PLAYER, null, values);
	}
	
	private static ContentValues addContent(ContentValues values, Player player) {
		values.put(LEVEL, player.level);
		values.put(EXP, player.exp);
		values.put(COIN, player.coin);
		values.put(GEM, player.gem);
		values.put(CURRENT_STICKER, player.currentSticker);
		values.put(MAX_STICKER, player.maxSticker);
		values.put(CITY, player.city);
		return values;
	}
	
}
