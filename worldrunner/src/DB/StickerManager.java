package DB;

import java.util.ArrayList;
import java.util.List;

import Abilities.DamageAbility;
import DB.Model.Equipment;
import DB.Model.Sticker;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StickerManager {
	private static final String TABLE_STICKER = "Sticker";
	private static final String PSTID = "pstid";
	private static final String PID = "pid";
	private static final String SID = "sid";
	private static final String NAME = "name";
	private static final String COLOR = "color";
	private static final String CURRENT_LEVEL = "current_level";
	private static final String CURRENT_EXP = "current_exp";
	private static final String SPAID = "spaid";
	private static final String SAAID = "saaid";
	private static final String EVOLVE = "evolve";
	private static final String EQUIPPED = "equipped";
	//temp to be used until coupled with equipment
	private static final String POSITION = "position";
	
	private static final String HP = "hp";
	private static final String ATTACK = "attack";
	private static final String DEFENSE = "defense";
	private static final String SPEED = "speed";
	private static final String CAPTURE = "capture";
	/**
	 * Creates the Table for Stickers
	 * @param db
	 */
	public static void create(SQLiteDatabase db) {
		String CREATE_STICKER_TABLE = "CREATE TABLE " + TABLE_STICKER + "("
                + PSTID + " INTEGER PRIMARY KEY," + PID + " INTEGER,"
                + SID + " INTEGER," + NAME + " TEXT," + COLOR + " INTEGER," +
                CURRENT_LEVEL + " INTEGER," + CURRENT_EXP + " INTEGER," + SPAID + " INTEGER, " + SAAID +
                " INTEGER," + EVOLVE + " INTEGER," + EQUIPPED + " INTEGER," + POSITION + " INTEGER," + 
                HP + " INTEGER," + ATTACK + " INTEGER," + DEFENSE + " INTEGER, " + SPEED + " INTEGER," 
                + CAPTURE + " REAL" + ")";
        db.execSQL(CREATE_STICKER_TABLE);
        /*createInitial(db, new Sticker(1, 1, 7, "Red Rock", 1, 1, 0, 100, 100, 1, 1, 1, 0, 0, 2000, 150, 125, 100, 0.0,0,
        		new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 2)));*/
	}
	
	/**
	 * Helper method to help create initial stickers from DB setup
	 * @param db - connection to the DB
	 * @param sticker - Sticker model to be added to the DB
	 */
	private static void createInitial(SQLiteDatabase db, Sticker sticker) {
		ContentValues values = new ContentValues();
		values = addContent(values, sticker);
		db.insert(TABLE_STICKER, null, values);
	}
	
	/**
	 * Drops the Sticker table
	 * @param db
	 */
	public static void drop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STICKER);
	}
	
	/**
	 * Returns an ArrayList of all stickers
	 * @param db
	 * @return list of all stickers
	 */
	public static ArrayList<Sticker> getSticker(SQLiteDatabase db) {
		ArrayList<Sticker> list = new ArrayList<Sticker>();
		String select = "SELECT * FROM " + TABLE_STICKER;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				Sticker sticker = new Sticker();
				sticker.pstid = cursor.getInt(0);
				sticker.pid = cursor.getInt(1);
				sticker.sid = cursor.getInt(2);
				sticker.name = cursor.getString(3);
				sticker.color = cursor.getInt(4);
				sticker.current_level = cursor.getInt(5);
				sticker.current_exp = cursor.getInt(6);
				sticker.spaid = cursor.getInt(7);
				sticker.saaid = cursor.getInt(8);
				sticker.evolve = cursor.getInt(9);
				sticker.equipped = cursor.getInt(10);
				sticker.position = cursor.getInt(11);
				sticker.hp = cursor.getInt(12);
				sticker.attack = cursor.getInt(13);
				sticker.defense = cursor.getInt(14);
				sticker.speed = cursor.getInt(15);
				sticker.capture = cursor.getDouble(16);
				list.add(sticker);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	private static Sticker createSticker(Cursor cursor) {
		Sticker sticker = new Sticker();
		sticker.pstid = cursor.getInt(0);
		sticker.pid = cursor.getInt(1);
		sticker.sid = cursor.getInt(2);
		sticker.name = cursor.getString(3);
		sticker.color = cursor.getInt(4);
		sticker.current_level = cursor.getInt(5);
		sticker.current_exp = cursor.getInt(6);
		sticker.spaid = cursor.getInt(7);
		sticker.saaid = cursor.getInt(8);
		sticker.evolve = cursor.getInt(9);
		sticker.equipped = cursor.getInt(10);
		sticker.position = cursor.getInt(11);
		sticker.hp = cursor.getInt(12);
		sticker.attack = cursor.getInt(13);
		sticker.defense = cursor.getInt(14);
		sticker.speed = cursor.getInt(15);
		sticker.capture = cursor.getDouble(16);
		return sticker;
	}
	
	/**
	 * Updates sticker information
	 * @param db
	 * @param sticker - the sticker who has been updated
	 * @return number of rows that were affected
	 */
	public static int updateSticker(SQLiteDatabase db, Sticker sticker) {
		ContentValues values = new ContentValues();
		values = addContent(values, sticker);
		return db.update(TABLE_STICKER, values, PSTID + " = ?", new String[] { String.valueOf(sticker.pstid) });
	}
	
	/**
	 * add new sticker to the DB
	 * @param db
	 * @param sticker - sticker to be added
	 */
	public static void addSticker(SQLiteDatabase db, Sticker sticker) {
		ContentValues values = new ContentValues();
		values = addContent(values, sticker);
		db.insert(TABLE_STICKER, null, values);
	}
	
	/**
	 * Add in a list of stickers to the dB
	 * @param db
	 * @param stickers - list of stickers to be added into the DB
	 */
	public static void addStickers(SQLiteDatabase db, ArrayList<Sticker> stickers) {
		for (Sticker sticker : stickers) {
			addSticker(db, sticker);
		}
	}
	
	/**
	 * Deletes the give sticker in the DB
	 * @param db
	 * @param sticker - sticker to be deleted
	 */
	public static void deleteSticker(SQLiteDatabase db, Sticker sticker) {
		db.delete(TABLE_STICKER, PSTID + " = ?", new String[] { String.valueOf(sticker.pstid) });
	}
	
	/**
	 * Helps setup the sticker to be used in an add query
	 * @param values - the thing to be added to
	 * @param sticker - the sticker that will be affected
	 * @return a ContentValues that have been updated
	 */
	private static ContentValues addContent(ContentValues values, Sticker sticker) {
		values.put(PID, sticker.pid);
		values.put(SID, sticker.sid);
		values.put(NAME, sticker.name);
		values.put(COLOR, sticker.color);
		values.put(CURRENT_LEVEL, sticker.current_level);
		values.put(CURRENT_EXP, sticker.current_exp);
		values.put(SPAID, sticker.spaid);
		values.put(SAAID, sticker.saaid);
		values.put(EVOLVE, sticker.evolve);
		values.put(EQUIPPED, sticker.equipped);
		values.put(POSITION, sticker.position);
		values.put(HP, sticker.hp);
		values.put(ATTACK, sticker.attack);
		values.put(DEFENSE, sticker.defense);
		values.put(SPEED, sticker.speed);
		values.put(CAPTURE, sticker.capture);
		return values;
	}
	
	/**
	 * Gets a list of the equipped sticker
	 * @param db
	 * @return a list of stickers that are equipped.
	 */
	public static ArrayList<Sticker> getEquippedStickers(SQLiteDatabase db) {
		ArrayList<Sticker> list = new ArrayList<Sticker>();
		String select = "SELECT * FROM " + TABLE_STICKER + " WHERE " + EQUIPPED + "=1";
		Cursor cursor = db.rawQuery(select, null);
		Sticker[] stickers = new Sticker[5];
		if (cursor.moveToFirst()) {
			do {
				Sticker sticker = createSticker(cursor);
				stickers[sticker.position - 1] = sticker;
			} while (cursor.moveToNext());
		}
		// orders the stickers
		for (int i = 0; i < stickers.length; i++) {
			if (stickers[i] == null) {
				list.add(null);
			} else {
				list.add(stickers[i]);
			}
		}
		return list;
	}
	
	/**
	 * Returns a list of equipments that aren't currently being equipped
	 * @param db - connection to the DB
	 * @param category - the specific type of equipment
	 * @return list of stickers with null being the first value because
	 * 			it will be used to create the remove interface for the sticker
	 * 			adapter
	 */
	public static ArrayList<Sticker> getUnequppedStickersWithNull(SQLiteDatabase db) {
		ArrayList<Sticker> list = new ArrayList<Sticker>();
		list.add(null);
		String select = "SELECT * FROM " + TABLE_STICKER + " WHERE " + EQUIPPED + "=0";
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				Sticker sticker = createSticker(cursor);
				list.add(sticker);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
}
