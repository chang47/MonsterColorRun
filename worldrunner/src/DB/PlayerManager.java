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
	private static final String CURRENT_EQUIPMENT = "current_equipment";
	private static final String MAX_EQUIPMENT = "max_equipment";
	private static final String CURRENT_STICKER = "current_sticker";
	private static final String MAX_STICKER = "max_sticker";
	
	public static void create(SQLiteDatabase db) {
		String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYER + "("
                + PID + " INTEGER PRIMARY KEY," + USERNAME + " TEXT,"
                + FNAME + " TEXT," + LNAME + " TEXT," + LEVEL + " INTEGER," +
                EXP +  " INTEGER, " + COIN + " INTEGER," + GEM + " INTEGER," +
                CURRENT_EQUIPMENT + " INTEGER," + MAX_EQUIPMENT + " INTEGER," +
                CURRENT_STICKER + " INTEGER," + MAX_STICKER + " INTEGER" + ")";
		db.execSQL(CREATE_PLAYER_TABLE);
		createInitial(db, new Player(1, "Coralbue", "Josh", "Chang", 4, 0, 4201, 5, 0, 30, 0, 30));
	}
	
	private static void createInitial(SQLiteDatabase db, Player player) {
		ContentValues values = new ContentValues();
		values.put(USERNAME, player.getUsername());
		values.put(FNAME, player.getFname());
		values.put(LNAME, player.getLname());
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
				player.setPid(cursor.getString(0));
				player.setUsername(cursor.getString(1));
				player.setFname(cursor.getString(2));
				player.setLname(cursor.getString(3));
				player.setLevel(cursor.getString(4));
				player.setExp(cursor.getString(5));
				player.setCoin(cursor.getString(6));
				player.setGem(cursor.getString(7));
				player.setCurrentEquipment(cursor.getString(8));
				player.setMaxEquipment(cursor.getString(9));
				player.setCurrentSticker(cursor.getString(10));
				player.setMaxSticker(cursor.getString(11));
				list.add(player);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	public static int updatePlayer(SQLiteDatabase db, Player player) {
		ContentValues values = new ContentValues();
		values = addContent(values, player);
		return db.update(TABLE_PLAYER, values, PID + " = ?", new String[] { String.valueOf(player.getPid()) });
	}
	
	public static void addPlayer(SQLiteDatabase db, Player player) {
		ContentValues values = new ContentValues();
		values.put(USERNAME, player.getUsername());
		values.put(FNAME, player.getFname());
		values.put(LNAME, player.getLname());
		values = addContent(values, player);
		db.insert(TABLE_PLAYER, null, values);
	}
	
	private static ContentValues addContent(ContentValues values, Player player) {
		values.put(LEVEL, player.getLevel());	
		values.put(EXP, player.getExp());
		values.put(COIN, player.getCoin());
		values.put(GEM, player.getGem());
		values.put(CURRENT_EQUIPMENT, player.getCurrentEquipment());
		values.put(MAX_EQUIPMENT, player.getMaxEquipment());
		values.put(CURRENT_STICKER, player.getCurrentSticker());
		values.put(MAX_STICKER, player.getMaxSticker());
		return values;
	}
	
}
