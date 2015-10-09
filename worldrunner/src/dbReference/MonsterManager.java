package dbReference;

import java.util.ArrayList;
import java.util.List;

import DB.Model.Sticker;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MonsterManager {
	private static final String MONSTERS_REFERENCE = "monstersReference";
	private static final String UID = "uid";
	private static final String SID = "sid";
	private static final String NAME = "name";
	private static final String ELEMENT = "color";
	private static final String SPAID = "spaid"; // special passive ability id
	private static final String SAAID = "saaid"; // special active ability id
	private static final String EVOLVE = "evolve";
	
	private static final String HP = "hp";
	private static final String ATK = "atk";
	private static final String DEF = "def";
	private static final String SPD = "spd";
	private static final String BASE_EXP = "base_exp";
	
	/**
	 * Creates the Table for Stickers
	 * @param db
	 */
	public static void create(SQLiteDatabase db) {
		String CREATE_STICKER_TABLE = "CREATE TABLE " + MONSTERS_REFERENCE + "("
                + UID + "INTEGER PRIMARY KEY," + SID + " INTEGER," + NAME + " TEXT," + ELEMENT + " INTEGER," +
                 SPAID + " INTEGER, " + SAAID + " INTEGER," + EVOLVE + " INTEGER," + 
                HP + " INTEGER," + ATK + " INTEGER," + DEF + " INTEGER, " + SPD  + " INTEGER,"+ 
                 BASE_EXP +" INTEGER" + ")";
        db.execSQL(CREATE_STICKER_TABLE);
        // id, name, element, spaid, saaid, evolve, hp, atk, def, spd, exp
        createInitial(db, createReferenceMonster(1, "Firtin", 0, 1, 1, 1, 45, 60, 50, 55, 20));
        createInitial(db, createReferenceMonster(2, "Artabbit", 1, 2, 2, 1, 50, 55, 55, 60, 20));
        createInitial(db, createReferenceMonster(3, "Roseer", 2, 3, 3, 1, 55, 50, 60, 50, 20)); 
        createInitial(db, createReferenceMonster(4, "Roly", 3, 4, 4, 1, 40, 25, 30, 40, 20)); // original 60, 45, 50, 60, 20));
        createInitial(db, createReferenceMonster(5, "Barat", 4, 5, 5, 1, 30, 45, 45, 30, 20)); // original 50, 65, 65, 50, 20
        createInitial(db, createReferenceMonster(6, "Aqurtle", 1, 6, 6, 1, 45, 30, 45, 40, 14)); // 45, 30, 65, 40, 14
        createInitial(db, createReferenceMonster(7, "Grake", 2, 7, 7, 1, 40, 50, 45, 65, 16)); // 40, 50, 45, 65, 16
        createInitial(db, createReferenceMonster(8, "Serse", 1, 8, 8, 1, 35, 65, 35, 40, 18));
        createInitial(db, createReferenceMonster(9, "Pyrig", 0, 9, 9, 1, 40, 50, 50, 50, 20));
	}
	
	private static Sticker createReferenceMonster(int sid, String name, int element, int spaid, int saaid, int evo,
			int hp, int atk, int def, int spd, int baseExp) {
		return new Sticker(-1, -1, sid, name, element, -1, baseExp, spaid, saaid, evo, -1, -1, hp, atk, def, spd, -1);
	}
	
	/**
	 * Helper method to help create initial stickers from DB setup
	 * @param db - connection to the DB
	 * @param sticker - Sticker model to be added to the DB
	 */
	private static void createInitial(SQLiteDatabase db, Sticker sticker) {
		ContentValues values = new ContentValues();
		values = addContent(values, sticker);
		db.insert(MONSTERS_REFERENCE, null, values);
	}
	
	/**
	 * Helps setup the sticker to be used in an add query
	 * @param values - the thing to be added to
	 * @param sticker - the sticker that will be affected
	 * @return a ContentValues that have been updated
	 */
	private static ContentValues addContent(ContentValues values, Sticker sticker) {
		values.put(SID, sticker.sid);
		values.put(NAME, sticker.name);
		values.put(ELEMENT, sticker.element);
		values.put(SPAID, sticker.spaid);
		values.put(SAAID, sticker.saaid);
		values.put(EVOLVE, sticker.evolve);
		values.put(HP, sticker.hp);
		values.put(ATK, sticker.attack);
		values.put(DEF, sticker.defense);
		values.put(SPD, sticker.speed);
		values.put(BASE_EXP, sticker.current_exp);
		return values;
	}
	
	/**
	 * Drops the Sticker table
	 * @param db
	 */
	public static void drop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + MONSTERS_REFERENCE);
	}
	
	/**
	 * Returns an ArrayList of all stickers
	 * @param db
	 * @return list of all stickers
	 */
	public static ArrayList<Sticker> getSticker(SQLiteDatabase db) {
		ArrayList<Sticker> list = new ArrayList<Sticker>();
		String select = "SELECT * FROM " + MONSTERS_REFERENCE;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				Sticker sticker = new Sticker();
				sticker.pstid = cursor.getInt(0);
				sticker.pid = -1;
				sticker.sid = cursor.getInt(1); // then what's 1?
				sticker.name = cursor.getString(2);
				sticker.element = cursor.getInt(3);
				sticker.current_level = -1;
				sticker.current_exp = cursor.getInt(11); // exp
				sticker.spaid = cursor.getInt(4);
				sticker.saaid = cursor.getInt(5);
				sticker.evolve = cursor.getInt(6);
				sticker.equipped = -1;
				sticker.position = -1;
				sticker.hp = cursor.getInt(7);
				sticker.attack = cursor.getInt(8);
				sticker.defense = cursor.getInt(9);
				sticker.speed = cursor.getInt(10);
				sticker.capture = -1;
				list.add(sticker);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	/**
	 * add new sticker to the DB
	 * @param db
	 * @param sticker - sticker to be added
	 */
	public static void addSticker(SQLiteDatabase db, Sticker sticker) {
		ContentValues values = new ContentValues();
		values = addContent(values, sticker);
		db.insert(MONSTERS_REFERENCE, null, values);
	}
	
	/**
	 * Add in a list of stickers to the dB
	 * @param db
	 * @param stickers - list of stickers to be added into the DB
	 */
	public static void addStickers(SQLiteDatabase db, List<Sticker> stickers) {
		for (Sticker sticker : stickers) {
			addSticker(db, sticker);
		}
	}
	
	/**
	 * Deletes the give sticker in the DB
	 * @param db
	 * @param sticker - sticker to be deleted
	 */
	public static void deleteSticker(SQLiteDatabase db, int uid) {
		db.delete(MONSTERS_REFERENCE, SID + " = ?", new String[] { String.valueOf(uid) });
	}
	
	
}
